package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Booking;
import com.execodex.sparrowair2.services.BookingService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
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

    public Mono<ServerResponse> createBooking(ServerRequest request) {
        return request.bodyToMono(Booking.class)
                .flatMap(bookingService::createBooking)
                .flatMap(booking -> ServerResponse.ok().bodyValue(booking))
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("Booking with ID already exists");
                    }
                    return handleError(e);
                });
    }

    private Mono<? extends ServerResponse> handleError(Throwable e) {
        if (e instanceof DuplicateKeyException) {
            return ServerResponse.status(HttpStatus.CONFLICT)
                    .bodyValue("Duplicate key error: " + e.getMessage());
        } else {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .bodyValue("Error creating booking: " + e.getMessage());
        }
    }

    public Mono<ServerResponse> handleGetBookingById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return bookingService.getBookingById(id)
                .flatMap(booking -> ServerResponse.ok().bodyValue(booking))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Add more methods as needed for other booking operations
}
