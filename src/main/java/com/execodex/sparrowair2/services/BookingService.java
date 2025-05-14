package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Booking;
import com.execodex.sparrowair2.repositories.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookingService {

    // Assuming you have a BookingRepository similar to SeatRepository
    private final BookingRepository bookingRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Flux<Booking> getAllBookings() {
        return bookingRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all bookings", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all bookings", e);
                    return Flux.error(e);
                });
    }

    public Mono<Booking> createBooking(Booking booking) {
        return bookingRepository.insert(booking)
                .doOnSuccess(b -> logger.info("Created booking with ID: {}", b.getId()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating booking with ID: {}", booking.getId(), e);
                    } else {
                        logger.error("Error creating booking with ID: {}", booking.getId(), e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    public Mono<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id)
                .doOnError(e -> logger.error("Error retrieving booking with ID: {}", id, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving booking with ID: {}", id, e);
                    return Mono.error(e);
                });
    }

    public Mono<Booking> getBookingByReference(String bookingReference) {
        return bookingRepository.findByBookingReference(bookingReference)
                .doOnError(e -> logger.error("Error retrieving booking with reference: {}", bookingReference, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving booking with reference: {}", bookingReference, e);
                    return Mono.error(e);
                });
    }
}
