package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Airline;
import com.execodex.sparrowair2.services.AirlineService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AirlineHandler {

    private final AirlineService airlineService;

    public AirlineHandler(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    // Get all airlines
    public Mono<ServerResponse> getAllAirlines(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(airlineService.getAllAirlines(), Airline.class)
                .onErrorResume(this::handleError);
    }

    // Get airline by ICAO code
    public Mono<ServerResponse> getAirlineByIcaoCode(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return airlineService.getAirlineByIcaoCode(icaoCode)
                .flatMap(airline -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airline))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Create a new airline
    public Mono<ServerResponse> createAirline(ServerRequest request) {
        return request.bodyToMono(Airline.class)
                .flatMap(airlineService::createAirline)
                .flatMap(airline -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airline))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("Airline with ICAO code already exists");
                    }
                    return handleError(e);
                });
    }

    // Update an existing airline
    public Mono<ServerResponse> updateAirline(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return request.bodyToMono(Airline.class)
                .flatMap(airline -> airlineService.updateAirline(icaoCode, airline))
                .flatMap(updatedAirline -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedAirline))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete an airline
    public Mono<ServerResponse> deleteAirline(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return airlineService.getAirlineByIcaoCode(icaoCode)
                .flatMap(airline -> airlineService.deleteAirline(icaoCode)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in AirlineHandler occurred: " + error.getMessage());
    }
}