package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.BookingSegment;
import com.execodex.sparrowair2.entities.Flight;
import com.execodex.sparrowair2.entities.Seat;
import com.execodex.sparrowair2.repositories.BookingSegmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BookingSegmentService {
    private final static Logger logger = LoggerFactory.getLogger(BookingSegmentService.class);
    private final BookingSegmentRepository bookingSegmentRepository;
    private final SeatService seatService;
    private final FlightService flightService;

    public BookingSegmentService(BookingSegmentRepository bookingSegmentRepository, SeatService seatService, FlightService flightService) {
        this.bookingSegmentRepository = bookingSegmentRepository;
        this.seatService = seatService;
        this.flightService = flightService;
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
                .flatMap(flight -> {
                    if (flight == null) {
                        return Mono.error(new RuntimeException("Flight not found with ID: " + bookingSegment.getFlightId()));
                    }
                    return Mono.just(flight);
                })
                .doOnError(e -> System.err.println("Error retrieving flight with ID " + bookingSegment.getFlightId() + ": " + e.getMessage()))
                .onErrorResume(e -> {
                    System.err.println("Error retrieving flight with ID " + bookingSegment.getFlightId() + ": " + e.getMessage());
                    return Mono.error(e);
                });
        //find the seat by seat ID
        Mono<Seat> seatById = seatService.getSeatById(bookingSegment.getSeatId())
                .flatMap(seat -> {
                    if (seat == null) {
                        return Mono.error(new RuntimeException("Seat not found with ID: " + bookingSegment.getSeatId()));
                    }
                    return Mono.just(seat);
                })
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
                    if (flight == null) {
                        return Mono.error(new RuntimeException("Flight not found with ID: " + bookingSegment.getFlightId()));
                    }
                    if (seat == null) {
                        return Mono.error(new RuntimeException("Seat not found with ID: " + bookingSegment.getSeatId()));
                    }
                    // If both flight and seat are available, proceed to check for existing booking segments
                    return checkAndSaveBookingSegment(bookingSegment);
                });


    }

    private Mono<BookingSegment> checkAndSaveBookingSegment(BookingSegment bookingSegment) {
        return bookingSegmentRepository.findByFlightIdAndSeatId(bookingSegment.getFlightId(), bookingSegment.getSeatId())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("Booking segment already exists for flight ID: " + bookingSegment.getFlightId() + " and seat ID: " + bookingSegment.getSeatId()));
                    } else {
                        return bookingSegmentRepository.insert(bookingSegment)
                                .doOnSuccess(savedSegment -> System.out.println("Created booking segment with ID: " + savedSegment.getId()))
                                .doOnError(e -> System.err.println("Error creating booking segment: " + e.getMessage()));
                    }
                });
    }

    public Mono<BookingSegment> updateBookingSegment(long l, BookingSegment bookingSegment) {
        return bookingSegmentRepository.findById(l)
                .flatMap(existingSegment -> {
                    if (existingSegment != null) {
                        bookingSegment.setFareClass(existingSegment.getFareClass());
                        bookingSegment.setSeatId(existingSegment.getSeatId());
                        return bookingSegmentRepository.save(bookingSegment)
                                .doOnSuccess(updatedSegment -> {
                                    System.out.println("Updated booking segment with ID: " + updatedSegment.getId());
                                    // Update the seat status if necessary
                                    seatService.updateSeatStatus(bookingSegment.getSeatId(), "AVAILABLE")
                                            .doOnSuccess(seat -> System.out.println("Updated seat status for seat ID: " + seat.getId()))
                                            .doOnError(e -> System.err.println("Error updating seat status: " + e.getMessage()));
                                })
                                .doOnError(e -> System.err.println("Error updating booking segment: " + e.getMessage()));


                    } else {
                        return Mono.error(new RuntimeException("Booking segment not found with ID: " + l));
                    }
                });
    }

    public Mono<BookingSegment> createBookingSegment2(BookingSegment bookingSegment) {
        Mono<Flight> flightMono = flightService.getFlightById(bookingSegment.getFlightId())
                .flatMap(flight -> {
                    if (flight == null) {
                        return Mono.error(new RuntimeException("Flight not found with ID: " + bookingSegment.getFlightId()));
                    }
                    return Mono.just(flight);
                })
                .doOnError(e -> System.err.println("Error retrieving flight with ID " + bookingSegment.getFlightId() + ": " + e.getMessage()))
                .onErrorResume(e -> {
                    System.err.println("Error retrieving flight with ID " + bookingSegment.getFlightId() + ": " + e.getMessage());
                    return Mono.error(e);
                });
        Mono<Seat> seatMono = seatService.getSeatById(bookingSegment.getSeatId())
                .flatMap(seat -> {
                    if (seat == null) {
                        return Mono.error(new RuntimeException("Seat not found with ID: " + bookingSegment.getSeatId()));
                    }
                    return Mono.just(seat);
                })
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

//        return bookingSegmentRepository.insert(bookingSegment)
//                .doOnSuccess(savedSegment -> System.out.println("Created booking segment with ID: " + savedSegment.getId()))
//                .doOnError(e -> System.err.println("Error creating booking segment: " + e.getMessage()));
    }

    public Mono<BookingSegment> getBookingSegmentByFlightIdAndSeatId(Long flightId, Long seatId) {
        return bookingSegmentRepository.findByFlightIdAndSeatId(flightId, seatId)
                .doOnError(e -> System.err.println("Error retrieving booking segment with flight ID " + flightId + " and seat ID " + seatId + ": " + e.getMessage()))
                .onErrorResume(e -> {
                    System.err.println("Error retrieving booking segment with flight ID " + flightId + " and seat ID " + seatId + ": " + e.getMessage());
                    return Mono.error(e);
                });
    }
}
