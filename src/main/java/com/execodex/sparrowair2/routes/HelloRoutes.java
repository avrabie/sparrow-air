package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.HelloRouteHandlers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class HelloRoutes {

    private final HelloRouteHandlers handlers;

    public HelloRoutes(HelloRouteHandlers handlers) {
        this.handlers = handlers;
    }


    @Bean
    @RouterOperation(
            path = "/hello",
            beanClass = HelloRoutes.class,
            beanMethod = "helloRoute",
            operation = @Operation(
                    operationId = "getHello",
                    summary = "Get Hello",
                    description = "Returns a hello message",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(mediaType = "text/plain")
                            )
                    }
            ))
    public RouterFunction<ServerResponse> helloRoute() {
        return RouterFunctions.route()
                .GET("/hello", handlers::handleHelloRequest)
                .GET("/anna", accept(MediaType.TEXT_EVENT_STREAM), handlers::handleAnnaRequest)
                .build();
    }


}
