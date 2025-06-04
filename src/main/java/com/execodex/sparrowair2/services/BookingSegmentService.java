package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.BookingSegment;
import com.execodex.sparrowair2.entities.Flight;
import com.execodex.sparrowair2.entities.Seat;
import com.execodex.sparrowair2.repositories.BookingSegmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BookingSegmentService {
    private final static Logger logger = LoggerFactory.getLogger(BookingSegmentService.class);
    private final BookingSegmentRepository bookingSegmentRepository;
    private final SeatService seatService;
    private final FlightService flightService;
    private final TransactionalOperator transactionalOperator;

    public BookingSegmentService(BookingSegmentRepository bookingSegmentRepository,
                                 SeatService seatService,
                                 FlightService flightService,
                                 ReactiveTransactionManager transactionManager) {
        this.bookingSegmentRepository = bookingSegmentRepository;
        this.seatService = seatService;
        this.flightService = flightService;
        this.transactionalOperator = TransactionalOperator.create(transactionManager);
    }

    public Flux<BookingSegment> getAllBookingSegments() {
        return bookingSegmentRepository.findAll()
                .doOnError(e -> System.err.println("Error retrieving all booking segments: " + e.getMessage()))
                .onErrorResume(e -> {
                    System.err.println("Error retrieving all booking segments: " + e.getMessage());
                    return Flux.error(e);
                });
    }

    public Mono<BookingSegment> getBookingSegmentById(Long id) {
        return bookingSegmentRepository.findById(id)
                .doOnError(e -> System.err.println("Error retrieving booking segment with ID " + id + ": " + e.getMessage()))
                .onErrorResume(e -> {
                    System.err.println("Error retrieving booking segment with ID " + id + ": " + e.getMessage());
                    return Mono.error(e);
                });
    }

    public Mono<BookingSegment> createBookingSegment(BookingSegment bookingSegment) {
        // before saving it, find if we have the same booking segment by flight ID and seat ID
        // find the flight by flight ID
        Mono<Flight> flightById = flightService.getFlightById(bookingSegment.getFlightId())
                .switchIfEmpty(Mono.error(new RuntimeException("Flight not found with ID: " + bookingSegment.getFlightId())))
//                .flatMap(flight -> {
//                    if (flight == null) {
//                        return Mono.error(new RuntimeException("Flight not found with ID: " + bookingSegment.getFlightId()));
//                    }
//                    return Mono.just(flight);
//                })
                .doOnError(e -> System.err.println("Error retrieving flight with ID " + bookingSegment.getFlightId() + ": " + e.getMessage()))
                .onErrorResume(e -> {
                    System.err.println("Error retrieving flight with ID " + bookingSegment.getFlightId() + ": " + e.getMessage());
                    return Mono.error(e);
                });
        //find the seat by seat ID
        Mono<Seat> seatById = seatService.getSeatById(bookingSegment.getSeatId())
                .switchIfEmpty(Mono.error(new RuntimeException("Seat not found with ID: " + bookingSegment.getSeatId())))
                .filter(seat -> seat.getFlightId().equals(bookingSegment.getFlightId()))
                .doOnNext(seat -> {
                    if (!seat.getFlightId().equals(bookingSegment.getFlightId())) {
                        throw new RuntimeException("Seat with ID " + bookingSegment.getSeatId() + " does not belong to flight ID " + bookingSegment.getFlightId());
                    }
                })
//                .flatMap(seat -> {
//                    if (seat == null) {
//                        return Mono.error(new RuntimeException("Seat not found with ID: " + bookingSegment.getSeatId()));
//                    }
//                    return Mono.just(seat);
//                })
                .doOnError(e -> System.err.println("Error retrieving seat with ID " + bookingSegment.getSeatId() + ": " + e.getMessage()))
                .onErrorResume(e -> {
                    System.err.println("Error retrieving seat with ID " + bookingSegment.getSeatId() + ": " + e.getMessage());
                    return Mono.error(e);
                });
        // check if the flight and seat are available
        return Mono.zip(flightById, seatById)
                .flatMap(tuple -> {
                    Flight flight = tuple.getT1();
                    Seat seat = tuple.getT2();
                    // Check if seat flight ID matches the booking segment flight ID
                    if (!seat.getFlightId().equals(bookingSegment.getFlightId())) {
                        return Mono.error(new RuntimeException("Seat with ID " + bookingSegment.getSeatId() + " does not belong to flight ID " + bookingSegment.getFlightId()));
                    }
                    // check if seat is available
                    if (!seat.getStatus().equals(com.execodex.sparrowair2.entities.SeatStatus.AVAILABLE)) {
                        return Mono.error(new RuntimeException("Seat with ID " + bookingSegment.getSeatId() + " is not available"));
                    }
                    // check if flight is not cancelled
                    if (flight.getStatus().equals("CANCELLED")) {
                        return Mono.error(new RuntimeException("Flight with ID " + bookingSegment.getFlightId() + " is cancelled"));
                    }

                    // If both flight and seat are available, proceed to check for existing booking segments
                    return checkAndSaveBookingSegment(bookingSegment);
                });


    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    protected Mono<BookingSegment> checkAndSaveBookingSegment(BookingSegment bookingSegment) {
        // Create a transaction with SERIALIZABLE isolation level to ensure no two customers can reserve the same seat
        System.out.println("Booking segment data: " + bookingSegment.toString());
        return bookingSegmentRepository
                .findByFlightIdAndSeatId(bookingSegment.getFlightId(), bookingSegment.getSeatId())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("Booking segment already exists for flight ID: " + bookingSegment.getFlightId() + " and seat ID: " + bookingSegment.getSeatId()));
                    } else {
                        // log infor about booking segment data

                        // Insert the booking segment
                        return bookingSegmentRepository.insert(bookingSegment)
                                .flatMap(savedSegment -> {
//                                    System.out.println("Created booking segment with ID: " + savedSegment.getId());
                                    // Update the seat status to RESERVED as part of the same transaction
                                    return seatService.updateSeatStatus(bookingSegment.getSeatId(), "RESERVED")
                                            .doOnSuccess(seat -> System.out.println("Updated seat status to RESERVED for seat ID: " + seat.getId()))
                                            .doOnError(e -> System.err.println("Error updating seat status: " + e.getMessage()))
                                            .thenReturn(savedSegment); // Return the saved booking segment
                                })
                                .doOnError(e -> System.err.println("Error creating booking segment in checkAndSaveBookingSegment: " + e.getMessage()));
                    }
                })
                // The @Transactional annotation will ensure this is executed in a transaction with SERIALIZABLE isolation level
                ;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Mono<BookingSegment> updateBookingSegment(long id, BookingSegment bookingSegment) {
        return bookingSegmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Booking segment not found with ID: " + id)))
                .flatMap(existingSegment -> {

//                    bookingSegment.setFareClass(existingSegment.getFareClass());
//                    bookingSegment.setSeatId(existingSegment.getSeatId());
                    return bookingSegmentRepository.save(bookingSegment)
                            .doOnSuccess(updatedSegment -> {
                                System.out.println("Updated booking segment with ID: " + updatedSegment.getId());
                                // Update the seat status if necessary
                                seatService.updateSeatStatus(existingSegment.getSeatId(), "AVAILABLE")
                                        .doOnSuccess(seat -> System.out.println("Updated seat status for seat ID: " + seat.getId()))
                                        .doOnError(e -> System.err.println("Error updating seat status: " + e.getMessage()));
                            })
                            .doOnError(e -> System.err.println("Error updating booking segment: " + e.getMessage()));


                });
    }

    public Mono<BookingSegment> createBookingSegment2(BookingSegment bookingSegment) {
        Mono<Flight> flightMono = flightService.getFlightById(bookingSegment.getFlightId())
                .switchIfEmpty(Mono.error(new RuntimeException("Flight not found with ID: " + bookingSegment.getFlightId())))
                .doOnError(e -> System.err.println("Error retrieving flight with ID " + bookingSegment.getFlightId() + ": " + e.getMessage()))
                .onErrorResume(e -> {
                    System.err.println("Error retrieving flight with ID " + bookingSegment.getFlightId() + ": " + e.getMessage());
                    return Mono.error(e);
                });
        Mono<Seat> seatMono = seatService.getSeatById(bookingSegment.getSeatId())
                .switchIfEmpty(Mono.error(new RuntimeException("Seat not found with ID: " + bookingSegment.getSeatId())))
                .doOnError(e -> System.err.println("Error retrieving seat with ID " + bookingSegment.getSeatId() + ": " + e.getMessage()))
                .onErrorResume(e -> {
                    System.err.println("Error retrieving seat with ID " + bookingSegment.getSeatId() + ": " + e.getMessage());
                    return Mono.error(e);
                });
        return Mono.zip(flightMono, seatMono)
                .flatMap(tuple -> {
                    Flight flight = tuple.getT1();
                    Seat seat = tuple.getT2();
                    if (flight == null) {
                        return Mono.error(new RuntimeException("Flight not found with ID: " + bookingSegment.getFlightId()));
                    }
                    if (seat == null) {
                        return Mono.error(new RuntimeException("Seat not found with ID: " + bookingSegment.getSeatId()));
                    }
                    // If both flight and seat are available, proceed to check for existing booking segments
                    return bookingSegmentRepository.insert(bookingSegment)
                            .doOnSuccess(savedSegment -> System.out.println("Created booking segment with ID: " + savedSegment.getId()))
                            .doOnError(e -> System.err.println("Error creating booking segment: " + e.getMessage()));
                });

    }

    public Mono<BookingSegment> getBookingSegmentByFlightIdAndSeatId(Long flightId, Long seatId) {
        return bookingSegmentRepository.findByFlightIdAndSeatId(flightId, seatId)
//                .switchIfEmpty(Mono.error(new RuntimeException("Booking segment not found with flight ID: " + flightId + " and seat ID: " + seatId)))
                .doOnError(e -> System.err.println("Error retrieving booking segment with flight ID " + flightId + " and seat ID " + seatId + ": " + e.getMessage()))
                .onErrorResume(e -> {
                    System.err.println("Error retrieving booking segment with flight ID " + flightId + " and seat ID " + seatId + ": " + e.getMessage());
                    return Mono.error(e);
                });
    }

    public Mono<BookingSegment> deleteBookingSegment(long l) {
        return bookingSegmentRepository.findById(l)
                .switchIfEmpty(Mono.error(new RuntimeException("Booking segment not found with ID: " + l)))
                .flatMap(existingSegment -> {
                    Long seatId = existingSegment.getSeatId();
                    // Update the seat status to "AVAILABLE" before deleting the booking segment
                    return seatService.updateSeatStatus(seatId, "AVAILABLE")
                            .doOnSuccess(seat -> System.out.println("Updated seat status for seat ID: " + seat.getId()))
                            .doOnError(e -> System.err.println("Error updating seat status: " + e.getMessage()))
                            .then(Mono.just(existingSegment)); // Return the existing segment for further processing
                })
                .flatMap(existingSegment -> bookingSegmentRepository
                        .delete(existingSegment)
                        .then(Mono.just(existingSegment))
                        .doOnSuccess(deletedSegment -> System.out.println("Deleted booking segment with ID: " + deletedSegment.getId()))
                        .doOnError(e -> System.err.println("Error deleting booking segment: " + e.getMessage())));
    }
}
