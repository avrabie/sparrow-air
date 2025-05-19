package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.services.computing.FlightsComputing;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class FlightsComputingHandler {

    private final FlightsComputing flightsComputing;

    public FlightsComputingHandler(FlightsComputing flightsComputing) {
        this.flightsComputing = flightsComputing;
    }

    // Get airport to airports flights mapping
    public Mono<ServerResponse> getAirportToAirportsFlights(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(flightsComputing.airpotToAirportsIcao(), Object.class)
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in FlightsComputingHandler occurred: " + error.getMessage());
    }

    public Mono<ServerResponse> getAirportsToFlights(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(flightsComputing.airpotsToFlights(), Object.class)
                .onErrorResume(this::handleError);
    }
}