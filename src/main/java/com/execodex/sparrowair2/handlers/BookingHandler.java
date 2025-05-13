package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Booking;
import com.execodex.sparrowair2.services.BookingService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class BookingHandler {
    private final BookingService bookingService;

    public BookingHandler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public Mono<ServerResponse> handleGetAllBookings(ServerRequest request) {
        return ServerResponse.ok().body(bookingService.getAllBookings(), Booking.class);
    }

    // Add more methods as needed for other booking operations
}
