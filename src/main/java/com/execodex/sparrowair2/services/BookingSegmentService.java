package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.BookingSegment;
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

    public BookingSegmentService(BookingSegmentRepository bookingSegmentRepository, SeatService seatService) {
        this.bookingSegmentRepository = bookingSegmentRepository;
        this.seatService = seatService;
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
        return bookingSegmentRepository
                .findByFlightIdAndSeatId(bookingSegment.getFlightId(), bookingSegment.getSeatId())
                .flatMap(existingSegment -> {
                    // If we find an existing segment, we can either update it or return it
                    if (existingSegment != null) {
                        logger.error("Booking segment already exists with ID: " + existingSegment.getId());
                        return Mono.error(new RuntimeException("Booking segment already exists with ID: " + existingSegment.getId()));
                    } else {
                        // If no existing segment is found, save the new one
                        return bookingSegmentRepository.save(bookingSegment)
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
}
