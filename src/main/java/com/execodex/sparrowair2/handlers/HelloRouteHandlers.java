package com.execodex.sparrowair2.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class HelloRouteHandlers {



    public Mono<ServerResponse> handleHelloRequest(ServerRequest serverRequest) {
        Mono<String> hello = Mono.just("Hello World");
        return ServerResponse.ok()
                .body(hello, String.class);
    }
}
