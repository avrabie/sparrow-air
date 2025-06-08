package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.HelloRouteHandlers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
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
    @RouterOperations({
            @RouterOperation(
                    path = "/populate/aircrafts",
                    beanClass = HelloRouteHandlers.class,
                    beanMethod = "handlePopulateAircrafts",
                    operation = @Operation(
                            operationId = "populateAircrafts",
                            summary = "Populate Aircrafts",
                            description = "Populates aircraft data from Skybrary",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(mediaType = "text/plain")
                                    )
                            }
                    )),
            @RouterOperation(
                    path = "/populate/airports",
                    beanClass = HelloRouteHandlers.class,
                    beanMethod = "handlePopulateAirports",
                    operation = @Operation(
                            operationId = "populateAirports",
                            summary = "Populate Airports",
                            description = "Populates airport data from Skybrary",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(mediaType = "text/plain")
                                    )
                            }
                    )),
            @RouterOperation(
                    path = "/airport-links",
                    beanClass = HelloRouteHandlers.class,
                    beanMethod = "handleGetAirportLinks",
                    operation = @Operation(
                            operationId = "getAirportLinks",
                            summary = "Get Airport Links",
                            description = "Retrieves links between airports",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(mediaType = "text/plain")
                                    )
                            }
                    )),
            @RouterOperation(
                    path = "/hello",
                    beanClass = HelloRouteHandlers.class,
                    beanMethod = "handleHelloRequest",
                    operation = @Operation(
                            operationId = "hello",
                            summary = "Hello Endpoint",
                            description = "Returns a greeting message",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "text/plain")
                                    )
                            }
                    ))
    })

    public RouterFunction<ServerResponse> helloRoute() {
        return RouterFunctions.route()
                .GET("/hello", handlers::handleHelloRequest)
                .POST("populate/aircrafts", handlers::handlePopulateAircrafts)
                .POST("populate/airports", handlers::handlePopulateAirports)
                .GET("/airport-links", handlers::handleGetAirportLinks)
                .build();
    }


}
