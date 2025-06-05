package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Aircraft;
import com.execodex.sparrowair2.entities.AircraftType;
import com.execodex.sparrowair2.services.AircraftTypeService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AircraftTypeHandler {

    private final AircraftTypeService aircraftTypeService;

    public AircraftTypeHandler(AircraftTypeService aircraftTypeService) {
        this.aircraftTypeService = aircraftTypeService;
    }

    // Get all aircraft types, optionally filtered by model name
    public Mono<ServerResponse> getAllAircraftTypes(ServerRequest request) {
        String searchQuery = request.queryParam("search").orElse(null);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(aircraftTypeService.getAllAircraftTypes(searchQuery), AircraftType.class)
                .onErrorResume(this::handleError);
    }

    // Get aircraft type by ICAO code
    public Mono<ServerResponse> getAircraftTypeByIcaoCode(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return aircraftTypeService.getAircraftTypeByIcaoCode(icaoCode)
                .flatMap(aircraftType -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(aircraftType))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Create a new aircraft type
    public Mono<ServerResponse> createAircraftType(ServerRequest request) {
        return request.bodyToMono(AircraftType.class)
                .flatMap(aircraftTypeService::createAircraftType)
                .flatMap(aircraftType -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(aircraftType))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("Aircraft type with ICAO code already exists");
                    }
                    return handleError(e);
                });
    }

    // Update an existing aircraft type
    public Mono<ServerResponse> updateAircraftType(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return request.bodyToMono(AircraftType.class)
                .flatMap(aircraftType -> aircraftTypeService.updateAircraftType(icaoCode, aircraftType))
                .flatMap(updatedAircraftType -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedAircraftType))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete an aircraft type
    public Mono<ServerResponse> deleteAircraftType(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return aircraftTypeService.getAircraftTypeByIcaoCode(icaoCode)
                .flatMap(aircraftType -> aircraftTypeService.deleteAircraftType(icaoCode)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in AircraftTypeHandler occurred: " + error.getMessage());
    }



    // Parse online aircraft from Skybrary
    public Mono<ServerResponse> parseOnlineAircraft(ServerRequest request) {
        String aircraftIcaoCode = request.pathVariable("icaoCode");
        return aircraftTypeService.parseOnlineAircraft(aircraftIcaoCode)
                .flatMap(aircraft -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(aircraft))
                .onErrorResume(this::handleError);
    }
}
