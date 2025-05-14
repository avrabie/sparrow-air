package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Seat;
import com.execodex.sparrowair2.services.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class SeatHandler {

    private final SeatService seatService;

    public SeatHandler(SeatService seatService) {
        this.seatService = seatService;
    }

    // Get all seats
    public Mono<ServerResponse> getAllSeats(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(seatService.getAllSeats(), Seat.class)
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> getSeatsByFlightId(ServerRequest request) {
        String flightId = request.pathVariable("flightId");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(seatService.getSeatsByFlightId(Long.parseLong(flightId)), Seat.class)
                .onErrorResume(this::handleError);
    }




    public Mono<ServerResponse> getSeatsByAirlineIcaoAndFlightNumber(ServerRequest request) {
        String flightNumber = request.pathVariable("flightNumber");
        String airlineIcao = request.pathVariable("airlineIcao");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(seatService.getSeatsByFlightNumber(airlineIcao, flightNumber), Seat.class)
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> getSeatById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("seatId"));
        return seatService.getSeatById(id)
                .flatMap(seat -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(seat))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> updateSeat(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("seatId"));
        return request.bodyToMono(Seat.class)
                .flatMap(seat -> seatService.updateSeat(id, seat))
                .flatMap(updatedSeat -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedSeat))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> getSeatsByFlightNumber(ServerRequest request) {
        String flightNumber = request.pathVariable("flightNumber");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(seatService.getSeatsByFlightNumber(flightNumber), Seat.class)
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in SeatHandler occurred: " + error.getMessage());
    }
}