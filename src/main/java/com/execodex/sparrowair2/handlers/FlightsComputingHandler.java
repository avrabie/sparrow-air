package com.execodex.sparrowair2.handlers;

//import com.execodex.sparrowair2.entities.Airport;

import com.execodex.sparrowair2.entities.skybrary.AirportNew;
import com.execodex.sparrowair2.services.AirportNewService;
import com.execodex.sparrowair2.services.computing.FlightsComputing;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class FlightsComputingHandler {

    private final FlightsComputing flightsComputing;
    private final AirportNewService airportService;

    public FlightsComputingHandler(FlightsComputing flightsComputing, AirportNewService airportService) {
        this.flightsComputing = flightsComputing;
        this.airportService = airportService;
    }

    // Get airport to airports flights mapping
    public Mono<ServerResponse> getAirportToAirportsFlights(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(flightsComputing.airpotToAirportsIcao(), Object.class)
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in FlightsComputingHandler occurred: " + error.getMessage());
    }

    public Mono<ServerResponse> getAirportsToFlights(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(flightsComputing.airpotsToFlights(), Object.class)
                .onErrorResume(this::handleError);
    }

    // Get route between two airports
    public Mono<ServerResponse> getRoute(ServerRequest request) {
        String departureIcao = request.queryParam("departure").orElse("");
        String arrivalIcao = request.queryParam("arrival").orElse("");

        if (departureIcao.isEmpty() || arrivalIcao.isEmpty()) {
            return ServerResponse.badRequest()
                    .bodyValue("Both departure and arrival airport ICAO codes are required");
        }

        return Mono.zip(
                        airportService.getAirportByIcaoCode(departureIcao),
                        airportService.getAirportByIcaoCode(arrivalIcao)
                )
                .flatMap(tuple -> {
                    AirportNew departureAirport = tuple.getT1();
                    AirportNew arrivalAirport = tuple.getT2();

                    if (departureAirport == null || arrivalAirport == null) {
                        return ServerResponse.badRequest()
                                .bodyValue("One or both of the specified airports could not be found");
                    }

                    return flightsComputing.getRoute(departureAirport, arrivalAirport)
                            .flatMap(route -> ServerResponse.ok()
                                    .contentType(APPLICATION_JSON)
                                    .bodyValue(route));
                })
                .onErrorResume(this::handleError);
    }

    // Get minimum cost route between two airports
    public Mono<ServerResponse> getRouteMinimumCost(ServerRequest request) {
        String departureIcao = request.queryParam("departure").orElse("");
        String arrivalIcao = request.queryParam("arrival").orElse("");

        if (departureIcao.isEmpty() || arrivalIcao.isEmpty()) {
            return ServerResponse.badRequest()
                    .bodyValue("Both departure and arrival airport ICAO codes are required");
        }

        return Mono.zip(
                        airportService.getAirportByIcaoCode(departureIcao),
                        airportService.getAirportByIcaoCode(arrivalIcao)
                )
                .flatMap(tuple -> {
                    AirportNew departureAirport = tuple.getT1();
                    AirportNew arrivalAirport = tuple.getT2();

                    if (departureAirport == null || arrivalAirport == null) {
                        return ServerResponse.badRequest()
                                .bodyValue("One or both of the specified airports could not be found");
                    }

                    return flightsComputing.getRouteMinimumCost(departureAirport, arrivalAirport)
                            .flatMap(route -> ServerResponse.ok()
                                    .contentType(APPLICATION_JSON)
                                    .bodyValue(route));
                })
                .onErrorResume(this::handleError);
    }
}
