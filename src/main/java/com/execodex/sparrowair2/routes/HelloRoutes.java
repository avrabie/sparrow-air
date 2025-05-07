package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.HelloRouteHandlers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class HelloRoutes {

    private final HelloRouteHandlers handlers;

    public HelloRoutes(HelloRouteHandlers handlers) {
        this.handlers = handlers;
    }


    @Bean
     public RouterFunction<ServerResponse> helloRoute() {
         return RouterFunctions.route()
                 .GET("/hello", handlers::handleHelloRequest)
                 .build();
     }


}
