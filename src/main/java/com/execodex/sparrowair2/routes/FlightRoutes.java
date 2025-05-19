package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.FlightHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class FlightRoutes {

    private final FlightHandler flightHandler;

    public FlightRoutes(FlightHandler flightHandler) {
        this.flightHandler = flightHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/flights",
                    method = RequestMethod.GET,
                    beanClass = FlightHandler.class,
                    beanMethod = "getAllFlights",
                    operation = @Operation(
                            operationId = "getAllFlights",
                            summary = "Get all flights",
                            description = "Returns a list of all flights",
                            tags = {"Flights \uD83D\uDEEB\uD83D\uDEEC"},
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
                    path = "/flights/{id}",
                    method = RequestMethod.GET,
                    beanClass = FlightHandler.class,
                    beanMethod = "getFlightById",
                    operation = @Operation(
                            operationId = "getFlightById",
                            summary = "Get flight by ID",
                            description = "Returns a flight by ID",
                            tags = {"Flights \uD83D\uDEEB\uD83D\uDEEC"},
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Flight ID", required = true)
                            },
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
                    path = "/flights/airline/{airlineIcaoCode}",
                    method = RequestMethod.GET,
                    beanClass = FlightHandler.class,
                    beanMethod = "getFlightsByAirlineIcaoCode",
                    operation = @Operation(
                            operationId = "getFlightsByAirlineIcaoCode",
                            summary = "Get flights by airline ICAO code",
                            description = "Returns a list of flights by airline ICAO code",
                            tags = {"Flights \uD83D\uDEEB\uD83D\uDEEC"},
                            parameters = {
                                    @Parameter(name = "airlineIcaoCode", in = ParameterIn.PATH, description = "Airline ICAO code", required = true)
                            },
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
                    path = "/flights",
                    method = RequestMethod.POST,
                    beanClass = FlightHandler.class,
                    beanMethod = "createFlight",
                    operation = @Operation(
                            operationId = "createFlight",
                            summary = "Create a new flight",
                            description = "Creates a new flight",
                            tags = {"Flights \uD83D\uDEEB\uD83D\uDEEC"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Flight object to be created",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                                    implementation = com.execodex.sparrowair2.entities.Flight.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Flight created successfully",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/flights/{id}",
                    method = RequestMethod.PUT,
                    beanClass = FlightHandler.class,
                    beanMethod = "updateFlight",
                    operation = @Operation(
                            operationId = "updateFlight",
                            summary = "Update an existing flight",
                            description = "Updates an existing flight",
                            tags = {"Flights \uD83D\uDEEB\uD83D\uDEEC"},
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Flight ID", required = true)
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Flight object to be updated",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                                    implementation = com.execodex.sparrowair2.entities.Flight.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Flight updated successfully",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/flights/{id}",
                    method = RequestMethod.DELETE,
                    beanClass = FlightHandler.class,
                    beanMethod = "deleteFlight",
                    operation = @Operation(
                            operationId = "deleteFlight",
                            summary = "Delete a flight",
                            description = "Deletes a flight by ID",
                            tags = {"Flights \uD83D\uDEEB\uD83D\uDEEC"},
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Flight ID", required = true)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "Flight deleted successfully"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/flights/airport/{airportCode}",
                    method = RequestMethod.GET,
                    beanClass = FlightHandler.class,
                    beanMethod = "getAllFlightsFromAirportCode",
                    operation = @Operation(
                            operationId = "getAllFlightsFromAirportCode",
                            summary = "Get all flights from a specific airport",
                            description = "Returns a list of flights departing from the specified airport code",
                            tags = {"Flights \uD83D\uDEEB\uD83D\uDEEC"},
                            parameters = {
                                    @Parameter(name = "airportCode", in = ParameterIn.PATH, description = "Airport ICAO code", required = true)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> flightRoutesFunction() {
        return RouterFunctions.route()
                .path("/flights", builder -> builder
                        // GET /flights - Get all flights
                        .GET("", accept(MediaType.APPLICATION_JSON), flightHandler::getAllFlights)
                        .GET("", accept(MediaType.APPLICATION_XML), flightHandler::getAllFlights)
                        // GET /flights/{id} - Get flight by ID
                        .GET("/{id}", accept(MediaType.APPLICATION_JSON), flightHandler::getFlightById)
                        // GET all the flights from a specific Airline Icao Code
                        .GET("/airline/{airlineIcaoCode}", accept(MediaType.APPLICATION_JSON), flightHandler::getFlightsByAirlineIcaoCode)
                        // POST /flights - Create a new flight
                        .POST("", accept(MediaType.APPLICATION_JSON), flightHandler::createFlight)
                        // PUT /flights/{id} - Update an existing flight
                        .PUT("/{id}", accept(MediaType.APPLICATION_JSON), flightHandler::updateFlight)
                        // DELETE /flights/{id} - Delete a flight
                        .DELETE("/{id}", flightHandler::deleteFlight)
                        // GET /flights/airport/{airportCode} - Get all flights from a specific airport
                        .GET("/airport/{airportCode}", accept(MediaType.APPLICATION_JSON), flightHandler::getAllFlightsFromAirportCode)
                )
                .build();
    }
}
