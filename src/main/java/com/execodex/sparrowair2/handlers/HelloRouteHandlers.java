package com.execodex.sparrowair2.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class HelloRouteHandlers {



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
}
