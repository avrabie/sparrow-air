package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.BookingSegment;
import com.execodex.sparrowair2.services.BookingSegmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class BookingSegmentHandler {

    private static final Logger logger = LoggerFactory.getLogger(BookingSegmentService.class);
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
        Mono<BookingSegment> bookingSegmentMono = request.bodyToMono(BookingSegment.class);
        return bookingSegmentMono
                .doOnNext(segment -> logger.info("Received booking segment request: {}", segment))
                .flatMap(bookingSegmentService::createBookingSegment)
                .doOnSuccess(segment -> logger.info("Successfully created booking segment: {}", segment))
                .doOnError(e -> logger.error("Failed to create booking segment", e))

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

    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in FlightHandler occurred: " + error.getMessage());
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

    public Mono<ServerResponse> createBookingSegment2(ServerRequest request) {
        Mono<ServerResponse> errorCreatingBookingSegment = request.bodyToMono(BookingSegment.class)
                .flatMap(bookingSegment -> bookingSegmentService.createBookingSegment2(bookingSegment))
                .flatMap(bookingSegment -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(bookingSegment))
                .onErrorResume(e -> {
                    System.err.println("Error creating booking segment: " + e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error creating booking segment");
                });
        return errorCreatingBookingSegment;
    }

    public Mono<ServerResponse> deleteBookingSegment(ServerRequest request) {
        String id = request.pathVariable("id");
        return bookingSegmentService.deleteBookingSegment(Long.parseLong(id))
                .flatMap(deleted -> ServerResponse
                        .status(HttpStatus.NO_CONTENT)
                        .build())
                .onErrorResume(e -> {
                    System.err.println("Error deleting booking segment with ID " + id + ": " + e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error deleting booking segment with ID " + id);
                });
    }
}
