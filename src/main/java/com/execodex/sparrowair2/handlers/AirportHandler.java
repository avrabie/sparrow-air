package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Airport;
import com.execodex.sparrowair2.services.AirportService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AirportHandler {

    private final AirportService airportService;

    public AirportHandler(AirportService airportService) {
        this.airportService = airportService;
    }

    // Get all airports
    public Mono<ServerResponse> getAllAirports(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(airportService.getAllAirports(), Airport.class)
                .onErrorResume(this::handleError);
    }

    // Get airport by ICAO code
    public Mono<ServerResponse> getAirportByIcaoCode(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return airportService.getAirportByIcaoCode(icaoCode)
                .flatMap(airport -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airport))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Create a new airport
    public Mono<ServerResponse> createAirport(ServerRequest request) {
        return request.bodyToMono(Airport.class)
                .flatMap(airportService::createAirport)
                .flatMap(airport -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airport))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("Airport with ICAO code already exists");
                    }
                    return handleError(e);
                });
    }

    // Update an existing airport
    public Mono<ServerResponse> updateAirport(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return request.bodyToMono(Airport.class)
                .flatMap(airport -> airportService.updateAirport(icaoCode, airport))
                .flatMap(updatedAirport -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedAirport))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete an airport
    public Mono<ServerResponse> deleteAirport(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return airportService.getAirportByIcaoCode(icaoCode)
                .flatMap(airport -> airportService.deleteAirport(icaoCode)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in AirportHandler occurred: " + error.getMessage());
    }
}
