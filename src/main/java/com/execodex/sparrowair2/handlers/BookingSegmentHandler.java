package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.BookingSegment;
import com.execodex.sparrowair2.services.BookingSegmentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class BookingSegmentHandler {

    private final BookingSegmentService bookingSegmentService;

    public BookingSegmentHandler(BookingSegmentService bookingSegmentService) {
        this.bookingSegmentService = bookingSegmentService;
    }

    public Mono<ServerResponse> getAllBookingSegments(ServerRequest request) {
        Flux<BookingSegment> allBookingSegments = bookingSegmentService.getAllBookingSegments();
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(allBookingSegments, BookingSegment.class)
                .onErrorResume(e -> {
                    System.err.println("Error retrieving all booking segments: " + e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error retrieving all booking segments");
                });
    }

    public Mono<ServerResponse> getBookingSegmentById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(bookingSegmentService.getBookingSegmentById(Long.parseLong(id)), BookingSegment.class)
                .onErrorResume(e -> {
                    System.err.println("Error retrieving booking segment with ID " + id + ": " + e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error retrieving booking segment with ID " + id);
                });
    }

    public Mono<ServerResponse> createBookingSegment(ServerRequest request) {
        return request.bodyToMono(BookingSegment.class)
                .flatMap(bookingSegmentService::createBookingSegment)
                .flatMap(bookingSegment -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(bookingSegment))
                .onErrorResume(e -> {
                    System.err.println("Error creating booking segment: " + e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error creating booking segment");
                });
    }

    public Mono<ServerResponse> updateBookingSegment(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(BookingSegment.class)
                .flatMap(bookingSegment -> bookingSegmentService.updateBookingSegment(Long.parseLong(id), bookingSegment))
                .flatMap(bookingSegment -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(bookingSegment))
                .onErrorResume(e -> {
                    System.err.println("Error updating booking segment with ID " + id + ": " + e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error updating booking segment with ID " + id);
                });
    }
}
