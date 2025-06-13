package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.CaaRouteHandlers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.execodex.sparrowair2.entities.caa.MdaAircraftRegistration;

/**
 * Routes for CAA (Civil Aviation Authority) related endpoints.
 */
@Configuration
public class CaaRoutes {

    private final CaaRouteHandlers handlers;

    public CaaRoutes(CaaRouteHandlers handlers) {
        this.handlers = handlers;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/caa/moldova/aircraft-registrations",
                    beanClass = CaaRouteHandlers.class,
                    beanMethod = "handleGetMoldavianAircraftRegistrations",
                    operation = @Operation(
                            operationId = "getMoldavianAircraftRegistrations",
                            summary = "Get Moldavian Aircraft Registrations",
                            description = "Retrieves aircraft registration data from the Moldavian CAA PDF",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = MdaAircraftRegistration.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(mediaType = "text/plain")
                                    )
                            }
                    ))
    })
    public RouterFunction<ServerResponse> caaRoutesFunction() {
        return RouterFunctions.route()
                .GET("/caa/moldova/aircraft-registrations", handlers::handleGetMoldavianAircraftRegistrations)
                .build();
    }
}