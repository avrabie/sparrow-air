package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Aircraft;
import com.execodex.sparrowair2.model.AircraftIcaoRequest;
import com.execodex.sparrowair2.services.utilities.ParseAircraftHtml;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Component
public class HelloRouteHandlers {
    private final ParseAircraftHtml parseAircraftHtml;

    public HelloRouteHandlers(ParseAircraftHtml parseAircraftHtml) {
        this.parseAircraftHtml = parseAircraftHtml;
    }


    public Mono<ServerResponse> handleHelloRequest(ServerRequest serverRequest) {
        Mono<String> hello = Mono.just("Hello World");

        return ServerResponse.ok()
                .body(hello, String.class);
    }

    public Mono<ServerResponse> handleAnnaRequest(ServerRequest request) {
        Flux<String> stringFlux = Flux.just("Hello ", "Anna", " Can ", "you ", "hear ", "me?")
                .delayElements(Duration.ofSeconds(1));

        return ServerResponse.ok()
                .header("Content-Type", "text/event-stream")
                .body(stringFlux, String.class)
                .onErrorResume(e -> {
                    System.err.println("Error retrieving hello Anna: " + e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error retrieving hello Anna");
                });
    }

    public Mono<ServerResponse> handlePopulateAircrafts(ServerRequest request) {
        Flux<Aircraft> aircraftFlux = request.bodyToMono(AircraftIcaoRequest.class)
                .doOnNext(aircraftIcaoRequest -> {
                    System.out.println("Received aircraft ICAO request: " + aircraftIcaoRequest);
                    // Here you would typically call a service to process the request
                })
                .flatMapMany(aircraftIcaoRequest -> Flux.fromIterable(aircraftIcaoRequest.getIcaoCodes()))
                .flatMap(icaoCode -> parseAircraftHtml.parseOnlineAircraft(icaoCode)
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
}
