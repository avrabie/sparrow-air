package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.AirlineFleet;
import com.execodex.sparrowair2.services.AirlineFleetService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AirlineFleetHandler {

    private final AirlineFleetService airlineFleetService;

    public AirlineFleetHandler(AirlineFleetService airlineFleetService) {
        this.airlineFleetService = airlineFleetService;
    }

    // Get all aircraft in the fleet
    public Mono<ServerResponse> getAllAirlineFleet(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(airlineFleetService.getAllAirlineFleet(), AirlineFleet.class)
                .onErrorResume(this::handleError);
    }

    // Get aircraft by ID
    public Mono<ServerResponse> getAirlineFleetById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return airlineFleetService.getAirlineFleetById(id)
                .flatMap(airlineFleet -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airlineFleet))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Get all aircraft for a specific airline
    public Mono<ServerResponse> getAirlineFleetByAirlineIcao(ServerRequest request) {
        String airlineIcao = request.pathVariable("airlineIcao");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(airlineFleetService.getAirlineFleetByAirlineIcao(airlineIcao), AirlineFleet.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Get all aircraft of a specific type
    public Mono<ServerResponse> getAirlineFleetByAircraftTypeIcao(ServerRequest request) {
        String aircraftTypeIcao = request.pathVariable("aircraftTypeIcao");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(airlineFleetService.getAirlineFleetByAircraftTypeIcao(aircraftTypeIcao), AirlineFleet.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Create a new aircraft in the fleet
    public Mono<ServerResponse> createAirlineFleet(ServerRequest request) {
        return request.bodyToMono(AirlineFleet.class)
                .flatMap(airlineFleetService::createAirlineFleet)
                .flatMap(airlineFleet -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airlineFleet))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("Aircraft already exists in the fleet");
                    }
                    return handleError(e);
                });
    }

    // Update an existing aircraft in the fleet
    public Mono<ServerResponse> updateAirlineFleet(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(AirlineFleet.class)
                .flatMap(airlineFleet -> airlineFleetService.updateAirlineFleet(id, airlineFleet))
                .flatMap(updatedAirlineFleet -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedAirlineFleet))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete an aircraft from the fleet
    public Mono<ServerResponse> deleteAirlineFleet(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return airlineFleetService.getAirlineFleetById(id)
                .flatMap(airlineFleet -> airlineFleetService.deleteAirlineFleet(id)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> getTotalAircraftCount(ServerRequest request) {
        String airlineIcao = request.pathVariable("airlineIcao");
        return airlineFleetService.getTotalAircraftCountByAirlineIcao(airlineIcao)
                .flatMap(count -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(count))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in AirlineFleetHandler occurred: " + error.getMessage());
    }

    public Mono<ServerResponse> getAirlineFleetByRegistration(ServerRequest request) {
        String registration = request.pathVariable("registration");
        return airlineFleetService.getAirlineFleetByRegistration(registration)
                .flatMap(airlineFleet -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airlineFleet))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }
}