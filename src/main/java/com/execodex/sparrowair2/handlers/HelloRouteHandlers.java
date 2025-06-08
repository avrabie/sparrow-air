package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Aircraft;
import com.execodex.sparrowair2.entities.AirportNew;
import com.execodex.sparrowair2.model.AircraftIcaoRequest;
import com.execodex.sparrowair2.model.AirportIcaoRequest;
import com.execodex.sparrowair2.services.utilities.ParseAircraftSkyBrary;
import com.execodex.sparrowair2.services.utilities.ParseAirportSkyBrary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class HelloRouteHandlers {
    private final ParseAircraftSkyBrary parseAircraftSkyBrary;
    private final ParseAirportSkyBrary parseAirportSkyBrary;

    public HelloRouteHandlers(ParseAircraftSkyBrary parseAircraftSkyBrary, ParseAirportSkyBrary parseAirportSkyBrary) {
        this.parseAircraftSkyBrary = parseAircraftSkyBrary;
        this.parseAirportSkyBrary = parseAirportSkyBrary;
    }


    public Mono<ServerResponse> handleHelloRequest(ServerRequest serverRequest) {
        Mono<String> hello = Mono.just("Hello World");

        return ServerResponse.ok()
                .body(hello, String.class);
    }



    public Mono<ServerResponse> handlePopulateAirports(ServerRequest request) {
        Flux<AirportNew> airportFlux = request.bodyToMono(AirportIcaoRequest.class)
                .doOnNext(airportIcaoRequest -> {
                    System.out.println("Received airport ICAO request: " + airportIcaoRequest);
                    // Here you would typically call a service to process the request
                })
                .flatMapMany(airportIcaoRequest -> Flux.fromIterable(airportIcaoRequest.getIcaoCodes()))
                .flatMap(icaoCode -> parseAirportSkyBrary.parseOnlineAirport(icaoCode)
                        .doOnNext(airportNew -> System.out.println("Parsed airport: " + airportNew))
                        .onErrorResume(e -> {
                            System.err.println("Error parsing airport with ICAO code " + icaoCode + ": " + e.getMessage());
                            return Mono.empty(); // Skip this ICAO code on error
                        }));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(airportFlux, AirportNew.class)
                .onErrorResume(e -> {
                    System.err.println("Error retrieving airports: " + e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error retrieving airports");
                });


    }

    public Mono<ServerResponse> handlePopulateAircrafts(ServerRequest request) {
        Flux<Aircraft> aircraftFlux = request.bodyToMono(AircraftIcaoRequest.class)
                .doOnNext(aircraftIcaoRequest -> {
                    System.out.println("Received aircraft ICAO request: " + aircraftIcaoRequest);
                    // Here you would typically call a service to process the request
                })
                .flatMapMany(aircraftIcaoRequest -> Flux.fromIterable(aircraftIcaoRequest.getIcaoCodes()))
                .flatMap(icaoCode -> parseAircraftSkyBrary.parseOnlineAircraft(icaoCode)
                        .doOnNext(aircraft -> System.out.println("Parsed aircraft: " + aircraft))
                        .onErrorResume(e -> {
                            System.err.println("Error parsing aircraft with ICAO code " + icaoCode + ": " + e.getMessage());
                            return Mono.empty(); // Skip this ICAO code on error
                        }));
        return ServerResponse.ok()
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(aircraftFlux, Aircraft.class)
                .onErrorResume(e -> {
                    System.err.println("Error retrieving aircrafts: " + e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error retrieving aircrafts");
                });
    }

    /**
     * Handles requests to get all airport ICAO codes from Skybrary.
     * 
     * @param request The server request
     * @return A server response containing a list of ICAO codes
     */
    public Mono<ServerResponse> handleGetAirportLinks(ServerRequest request) {
        int pages = Integer.parseInt(request.queryParam("pages").orElseGet(()->"2"));
        Flux<String> airportIcaoCodes = parseAirportSkyBrary.crawlAirportLinks(pages)
                .map(icaoCode -> "\""+icaoCode+"\", ") // Ensure ICAO codes are uppercase
                .doOnNext(icaoCode -> System.out.println("Found airport ICAO code: " + icaoCode))
                .onErrorResume(e -> {
                    System.err.println("Error crawling airport links: " + e.getMessage());
                    return Flux.empty(); // Return empty flux on error
                });

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(airportIcaoCodes, String.class)
                .onErrorResume(e -> {
                    System.err.println("Error retrieving airport links: " + e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error retrieving airport links");
                });
    }
}
