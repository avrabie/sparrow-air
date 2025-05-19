package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.FlightsComputingHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class FlightsComputingRoutes {

    private final FlightsComputingHandler flightsComputingHandler;

    public FlightsComputingRoutes(FlightsComputingHandler flightsComputingHandler) {
        this.flightsComputingHandler = flightsComputingHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/flights-computing/airport-to-airports",
                    method = RequestMethod.GET,
                    beanClass = FlightsComputingHandler.class,
                    beanMethod = "getAirportToAirportsFlights",
                    operation = @Operation(
                            operationId = "getAirportToAirportsFlights",
                            summary = "Get airport to airports flights mapping",
                            description = "Returns a mapping of departure airports to their destination airports",
                            tags = {"Flights Computing ✈️ \uD83C\uDF10"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/flights-computing/airports-to-flights",
                    method = RequestMethod.GET,
                    beanClass = FlightsComputingHandler.class,
                    beanMethod = "getAirportsToFlights",
                    operation = @Operation(
                            operationId = "getAirportsToFlights",
                            summary = "Get airports to flights mapping",
                            description = "Returns a mapping of airports to their flights",
                            tags = {"Flights Computing ✈️ \uD83C\uDF10"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/flights-computing/route",
                    method = RequestMethod.GET,
                    beanClass = FlightsComputingHandler.class,
                    beanMethod = "getRoute",
                    operation = @Operation(
                            operationId = "getRoute",
                            summary = "Get route between two airports",
                            description = "Returns a list of flights that form a route between the specified departure and arrival airports",
                            tags = {"Flights Computing ✈️ \uD83C\uDF10"},
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(name = "departure", description = "ICAO code of the departure airport", required = true),
                                    @io.swagger.v3.oas.annotations.Parameter(name = "arrival", description = "ICAO code of the arrival airport", required = true)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request - missing or invalid parameters"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> flightsComputingRoutesFunction() {
        return RouterFunctions.route()
                .path("/flights-computing", builder -> builder
                        // GET /flights-computing/airport-to-airports - Get airport to airports flights mapping
                        .GET("/airport-to-airports", accept(MediaType.APPLICATION_JSON), flightsComputingHandler::getAirportToAirportsFlights)
                        .GET("/airports-to-flights", accept(MediaType.APPLICATION_JSON), flightsComputingHandler::getAirportsToFlights)
                        // GET /flights-computing/route - Get route between two airports
                        .GET("/route", accept(MediaType.APPLICATION_JSON), flightsComputingHandler::getRoute)
                )
                .build();
    }
}
