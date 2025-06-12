package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.caa.FaaAircraftRegistration;
import com.execodex.sparrowair2.services.FaaAircraftRegistrationService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class FaaAircraftRegistrationHandler {

    private final FaaAircraftRegistrationService faaAircraftRegistrationService;

    public FaaAircraftRegistrationHandler(FaaAircraftRegistrationService faaAircraftRegistrationService) {
        this.faaAircraftRegistrationService = faaAircraftRegistrationService;
    }

    // Get all FAA aircraft registrations
    public Mono<ServerResponse> getAllFaaAircraftRegistrations(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getAllFaaAircraftRegistrations(), FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registration by N-Number
    public Mono<ServerResponse> getFaaAircraftRegistrationByNNumber(ServerRequest request) {
        String nNumber = request.pathVariable("nNumber");
        return faaAircraftRegistrationService.getFaaAircraftRegistrationByNNumber(nNumber)
                .flatMap(registration -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(registration))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registrations by engine manufacturer model code
    public Mono<ServerResponse> getFaaAircraftRegistrationsByEngineMfrModelCode(ServerRequest request) {
        String engineMfrModelCode = request.pathVariable("engineMfrModelCode");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getFaaAircraftRegistrationsByEngineMfrModelCode(engineMfrModelCode), 
                        FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registrations by type aircraft
    public Mono<ServerResponse> getFaaAircraftRegistrationsByTypeAircraft(ServerRequest request) {
        String typeAircraft = request.pathVariable("typeAircraft");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getFaaAircraftRegistrationsByTypeAircraft(typeAircraft), 
                        FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registrations by aircraft manufacturer model code
    public Mono<ServerResponse> getFaaAircraftRegistrationsByAircraftMfrModelCode(ServerRequest request) {
        String aircraftMfrModelCode = request.pathVariable("aircraftMfrModelCode");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getFaaAircraftRegistrationsByAircraftMfrModelCode(aircraftMfrModelCode), 
                        FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registrations by Mode S Code
    public Mono<ServerResponse> getFaaAircraftRegistrationsByModeSCode(ServerRequest request) {
        String modeSCode = request.pathVariable("modeSCode");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getFaaAircraftRegistrationsByModeSCode(modeSCode), 
                        FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registrations by Mode S Code Hex
    public Mono<ServerResponse> getFaaAircraftRegistrationsByModeScodeHex(ServerRequest request) {
        String modeScodeHex = request.pathVariable("modeScodeHex");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getFaaAircraftRegistrationsByModeScodeHex(modeScodeHex), 
                        FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Create a new FAA aircraft registration
    public Mono<ServerResponse> createFaaAircraftRegistration(ServerRequest request) {
        return request.bodyToMono(FaaAircraftRegistration.class)
                .flatMap(faaAircraftRegistrationService::createFaaAircraftRegistration)
                .flatMap(registration -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(registration))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("FAA aircraft registration with N-Number already exists");
                    }
                    return handleError(e);
                });
    }

    // Update an existing FAA aircraft registration
    public Mono<ServerResponse> updateFaaAircraftRegistration(ServerRequest request) {
        String nNumber = request.pathVariable("nNumber");
        return request.bodyToMono(FaaAircraftRegistration.class)
                .flatMap(registration -> faaAircraftRegistrationService.updateFaaAircraftRegistration(nNumber, registration))
                .flatMap(updatedRegistration -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedRegistration))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete a FAA aircraft registration
    public Mono<ServerResponse> deleteFaaAircraftRegistration(ServerRequest request) {
        String nNumber = request.pathVariable("nNumber");
        return faaAircraftRegistrationService.getFaaAircraftRegistrationByNNumber(nNumber)
                .flatMap(registration -> faaAircraftRegistrationService.deleteFaaAircraftRegistration(nNumber)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in FaaAircraftRegistrationHandler occurred: " + error.getMessage());
    }
}
