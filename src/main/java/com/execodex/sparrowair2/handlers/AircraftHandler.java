package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.skybrary.Aircraft;
import com.execodex.sparrowair2.services.AircraftService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AircraftHandler {

    private final AircraftService aircraftService;

    public AircraftHandler(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    // Get all aircraft
    public Mono<ServerResponse> getAllAircraft(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(aircraftService.getAllAircraft(), Aircraft.class)
                .onErrorResume(this::handleError);
    }

    // Get aircraft by ICAO code
    public Mono<ServerResponse> getAircraftByIcaoCode(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return aircraftService.getAircraftByIcaoCode(icaoCode)
                .flatMap(aircraft -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(aircraft))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Create a new aircraft
    public Mono<ServerResponse> createAircraft(ServerRequest request) {
        return request.bodyToMono(Aircraft.class)
                .flatMap(aircraftService::createAircraft)
                .flatMap(aircraft -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(aircraft))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("Aircraft with ICAO code already exists");
                    }
                    return handleError(e);
                });
    }

    // Update an existing aircraft
    public Mono<ServerResponse> updateAircraft(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return request.bodyToMono(Aircraft.class)
                .flatMap(aircraft -> aircraftService.updateAircraft(icaoCode, aircraft))
                .flatMap(updatedAircraft -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedAircraft))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete an aircraft
    public Mono<ServerResponse> deleteAircraft(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return aircraftService.getAircraftByIcaoCode(icaoCode)
                .flatMap(aircraft -> aircraftService.deleteAircraft(icaoCode)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Parse online aircraft from Skybrary
    public Mono<ServerResponse> parseOnlineAircraft(ServerRequest request) {
        String aircraftIcaoCode = request.pathVariable("icaoCode");
        return aircraftService.parseOnlineAircraft(aircraftIcaoCode)
                .flatMap(aircraft -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(aircraft))
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in AircraftHandler occurred: " + error.getMessage());
    }
}