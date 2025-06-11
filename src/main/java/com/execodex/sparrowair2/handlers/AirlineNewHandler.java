package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.kaggle.AirlineNew;
import com.execodex.sparrowair2.services.AirlineNewService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AirlineNewHandler {

    private final AirlineNewService airlineNewService;

    public AirlineNewHandler(AirlineNewService airlineNewService) {
        this.airlineNewService = airlineNewService;
    }

    // Get all airlines
    public Mono<ServerResponse> getAllAirlines(ServerRequest request) {
        Integer page = request.queryParam("page").map(Integer::parseInt).orElse(null);
        Integer size = request.queryParam("size").map(Integer::parseInt).orElse(null);

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(airlineNewService.getAllAirlines(page, size), AirlineNew.class)
                .onErrorResume(this::handleError);
    }

    // Get airlines by country
    public Mono<ServerResponse> getAirlinesByCountry(ServerRequest request) {
        String country = request.pathVariable("country");
        Integer page = request.queryParam("page").map(Integer::parseInt).orElse(null);
        Integer size = request.queryParam("size").map(Integer::parseInt).orElse(null);

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(airlineNewService.getAirlinesByCountry(country, page, size), AirlineNew.class)
                .onErrorResume(this::handleError);
    }

    // Get airlines by active status
    public Mono<ServerResponse> getAirlinesByActive(ServerRequest request) {
        String active = request.pathVariable("active");
        Integer page = request.queryParam("page").map(Integer::parseInt).orElse(null);
        Integer size = request.queryParam("size").map(Integer::parseInt).orElse(null);

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(airlineNewService.getAirlinesByActive(active, page, size), AirlineNew.class)
                .onErrorResume(this::handleError);
    }

    // Get airline by ID
    public Mono<ServerResponse> getAirlineById(ServerRequest request) {
        Integer airlineId = Integer.parseInt(request.pathVariable("airlineId"));
        return airlineNewService.getAirlineById(airlineId)
                .flatMap(airline -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airline))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Create a new airline
    public Mono<ServerResponse> createAirline(ServerRequest request) {
        return request.bodyToMono(AirlineNew.class)
                .flatMap(airlineNewService::createAirline)
                .flatMap(airline -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airline))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("Airline with ID already exists");
                    }
                    return handleError(e);
                });
    }

    // Update an existing airline
    public Mono<ServerResponse> updateAirline(ServerRequest request) {
        Integer airlineId = Integer.parseInt(request.pathVariable("airlineId"));
        return request.bodyToMono(AirlineNew.class)
                .flatMap(airline -> airlineNewService.updateAirline(airlineId, airline))
                .flatMap(updatedAirline -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedAirline))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete an airline
    public Mono<ServerResponse> deleteAirline(ServerRequest request) {
        Integer airlineId = Integer.parseInt(request.pathVariable("airlineId"));
        return airlineNewService.getAirlineById(airlineId)
                .flatMap(airline -> airlineNewService.deleteAirline(airlineId)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Get airline by ICAO code
    public Mono<ServerResponse> getAirlineByIcaoCode(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return airlineNewService.getAirlineByIcaoCode(icaoCode)
                .flatMap(airline -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airline))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Get airline by IATA code
    public Mono<ServerResponse> getAirlineByIataCode(ServerRequest request) {
        String iataCode = request.pathVariable("iataCode");
        return airlineNewService.getAirlineByIataCode(iataCode)
                .flatMap(airline -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airline))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Get airlines by name containing a string
    public Mono<ServerResponse> getAirlinesByNameContaining(ServerRequest request) {
        String nameContains = request.pathVariable("nameContains");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(airlineNewService.getAirlinesByNameContaining(nameContains), AirlineNew.class)
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in AirlineNewHandler occurred: " + error.getMessage());
    }
}
