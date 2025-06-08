package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.AirportNew;
import com.execodex.sparrowair2.services.AirportNewService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AirportNewHandler {

    private final AirportNewService airportNewService;

    public AirportNewHandler(AirportNewService airportNewService) {
        this.airportNewService = airportNewService;
    }

    // Get all airports
    public Mono<ServerResponse> getAllAirports(ServerRequest request) {
        Integer page = request.queryParam("page").map(Integer::parseInt).orElse(null);
        Integer size = request.queryParam("size").map(Integer::parseInt).orElse(null);

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(airportNewService.getAllAirports(page, size), AirportNew.class)
                .onErrorResume(this::handleError);
    }

    // Get airport by ICAO code
    public Mono<ServerResponse> getAirportByIcaoCode(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return airportNewService.getAirportByIcaoCode(icaoCode)
                .flatMap(airport -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(airport))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Create a new airport
    public Mono<ServerResponse> createAirport(ServerRequest request) {
        return request.bodyToMono(AirportNew.class)
                .flatMap(airportNewService::createAirport)
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
        return request.bodyToMono(AirportNew.class)
                .flatMap(airport -> airportNewService.updateAirport(icaoCode, airport))
                .flatMap(updatedAirport -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedAirport))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete an airport
    public Mono<ServerResponse> deleteAirport(ServerRequest request) {
        String icaoCode = request.pathVariable("icaoCode");
        return airportNewService.getAirportByIcaoCode(icaoCode)
                .flatMap(airport -> airportNewService.deleteAirport(icaoCode)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Calculate distance between two airports
    public Mono<ServerResponse> calculateDistance(ServerRequest request) {
        String fromIcaoCode = request.queryParam("from").orElse("");
        String toIcaoCode = request.queryParam("to").orElse("");

        if (fromIcaoCode.isEmpty() || toIcaoCode.isEmpty()) {
            return ServerResponse.badRequest()
                    .bodyValue("Both 'from' and 'to' ICAO codes are required");
        }

        Mono<AirportNew> fromAirport = airportNewService.getAirportByIcaoCode(fromIcaoCode);
        Mono<AirportNew> toAirport = airportNewService.getAirportByIcaoCode(toIcaoCode);

        return Mono.zip(fromAirport, toAirport)
                .flatMap(tuple -> {
                    AirportNew airport1 = tuple.getT1();
                    AirportNew airport2 = tuple.getT2();
                    double distance = airportNewService.distance(airport1, airport2);
                    return ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .bodyValue(new DistanceResponse(fromIcaoCode, toIcaoCode, distance));
                })
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Response class for distance calculation
    private static class DistanceResponse {
        private final String fromAirport;
        private final String toAirport;
        private final double distanceKm;

        public DistanceResponse(String fromAirport, String toAirport, double distanceKm) {
            this.fromAirport = fromAirport;
            this.toAirport = toAirport;
            this.distanceKm = distanceKm;
        }

        public String getFromAirport() {
            return fromAirport;
        }

        public String getToAirport() {
            return toAirport;
        }

        public double getDistanceKm() {
            return distanceKm;
        }
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in AirportNewHandler occurred: " + error.getMessage());
    }
}
