package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Passenger;
import com.execodex.sparrowair2.services.PassengerService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class PassengerHandler {

    private final PassengerService passengerService;

    public PassengerHandler(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    // Get all passengers
    public Mono<ServerResponse> getAllPassengers(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(passengerService.getAllPassengers(), Passenger.class)
                .onErrorResume(this::handleError);
    }

    // Get passenger by ID
    public Mono<ServerResponse> getPassengerById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return passengerService.getPassengerById(id)
                .flatMap(passenger -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(passenger))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Get passengers by first name and last name
    public Mono<ServerResponse> getPassengersByFirstNameAndLastName(ServerRequest request) {
        String firstName = request.queryParam("firstName").orElse("");
        String lastName = request.queryParam("lastName").orElse("");

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(passengerService.getPassengersByFirstNameAndLastName(firstName, lastName), Passenger.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Create a new passenger
    public Mono<ServerResponse> createPassenger(ServerRequest request) {
        return request.bodyToMono(Passenger.class)
                .flatMap(passengerService::createPassenger)
                .flatMap(passenger -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(passenger))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("Passenger with passport number already exists");
                    }
                    return handleError(e);
                });
    }

    // Update an existing passenger
    public Mono<ServerResponse> updatePassenger(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(Passenger.class)
                .flatMap(passenger -> passengerService.updatePassenger(id, passenger))
                .flatMap(updatedPassenger -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedPassenger))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete a passenger
    public Mono<ServerResponse> deletePassenger(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return passengerService.getPassengerById(id)
                .flatMap(passenger -> passengerService.deletePassenger(id)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in PassengerHandler occurred: " + error.getMessage());
    }
}
