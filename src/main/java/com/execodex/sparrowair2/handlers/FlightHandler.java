package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Flight;
import com.execodex.sparrowair2.services.FlightService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class FlightHandler {

    private final FlightService flightService;

    public FlightHandler(FlightService flightService) {
        this.flightService = flightService;
    }

    // Get all flights
    public Mono<ServerResponse> getAllFlights(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(flightService.getAllFlights(), Flight.class)
                .onErrorResume(this::handleError);
    }

    // Get flight by ID
    public Mono<ServerResponse> getFlightById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return flightService.getFlightById(id)
                .flatMap(flight -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(flight))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Create a new flight
    public Mono<ServerResponse> createFlight(ServerRequest request) {
        return request.bodyToMono(Flight.class)
                .flatMap(flightService::createFlight)
                .flatMap(flight -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(flight))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("Flight with ID already exists");
                    }
                    return handleError(e);
                });
    }

    // Update an existing flight
    public Mono<ServerResponse> updateFlight(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(Flight.class)
                .flatMap(flight -> flightService.updateFlight(id, flight))
                .flatMap(updatedFlight -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedFlight))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete a flight
    public Mono<ServerResponse> deleteFlight(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return flightService.getFlightById(id)
                .flatMap(flight -> flightService.deleteFlight(id)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in FlightHandler occurred: " + error.getMessage());
    }

    public Mono<ServerResponse> getFlightsByAirlineIcaoCode(ServerRequest request) {
        String airlineIcaoCode = request.pathVariable("airlineIcaoCode");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(flightService.getFlightsByAirlineIcaoCode(airlineIcaoCode), Flight.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Get all flights from a specific airport code
    public Mono<ServerResponse> getAllFlightsFromAirportCode(ServerRequest request) {
        String airportCode = request.pathVariable("airportCode");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(flightService.getAllFlightsFromAirportCode(airportCode), Flight.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> getAllFlightsToAirportCode(ServerRequest request) {
        String airportCode = request.pathVariable("airportCode");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(flightService.getAllFlightsToAirportCode(airportCode), Flight.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }
}
