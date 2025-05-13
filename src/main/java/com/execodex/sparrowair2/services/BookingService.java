package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Booking;
import com.execodex.sparrowair2.repositories.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
}
