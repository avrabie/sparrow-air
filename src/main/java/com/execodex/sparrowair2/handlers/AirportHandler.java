package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Airport;
import com.execodex.sparrowair2.repositories.AirportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AirportHandler {

    private final AirportRepository airportRepository;

    public AirportHandler(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    // Get all airports
    public Mono<ServerResponse> getAllAirports(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(airportRepository.findAll(), Airport.class);
    }

    // Get airport by ICAO code
    public Mono<ServerResponse> getAirportByIcaoCode(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return airportRepository.findById(icaoCode)
                .flatMap(airport -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airport))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    // Create a new airport
    public Mono<ServerResponse> createAirport(ServerRequest request) {
        return request.bodyToMono(Airport.class)
                .flatMap(airportRepository::insert)
                .flatMap(airport -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airport))
                .onErrorResume(e -> {
                            if (e instanceof org.springframework.dao.DuplicateKeyException) {
                                return ServerResponse
                                        .status(HttpStatus.CONFLICT)
                                        .bodyValue("Airport with ICAO code already exists");
                            }
                            return ServerResponse
                                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .bodyValue("Could not create airport for unknown reasons: " + e.getMessage());
                        }
                );
    }


    // Update an existing airport
    public Mono<ServerResponse> updateAirport(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return request.bodyToMono(Airport.class)
                .flatMap(airport -> {
                    airport.setIcaoCode(icaoCode); // Ensure the ID is set correctly
                    return airportRepository.save(airport);
                })
                .flatMap(updatedAirport -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedAirport))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    // Delete an airport
    public Mono<ServerResponse> deleteAirport(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return airportRepository.findById(icaoCode)
                .flatMap(airport -> airportRepository.delete(airport)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}