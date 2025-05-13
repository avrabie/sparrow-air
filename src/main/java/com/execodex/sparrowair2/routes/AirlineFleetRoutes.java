package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.AirlineFleetHandler;
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
public class AirlineFleetRoutes {

    private final AirlineFleetHandler airlineFleetHandler;

    public AirlineFleetRoutes(AirlineFleetHandler airlineFleetHandler) {
        this.airlineFleetHandler = airlineFleetHandler;
    }

    @RouterOperations({
            @RouterOperation(
                    path = "/airline-fleet",
                    method = RequestMethod.GET,
                    beanClass = AirlineFleetHandler.class,
                    beanMethod = "getAllAirlineFleet",
                    operation = @Operation(
                            operationId = "getAllAirlineFleet",
                            summary = "Get all aircraft in the fleet",
                            description = "Returns a list of all aircraft in the fleet",
                            tags = {"Airline Fleet \uD83D\uDC68\u200D✈\uFE0F ✈️"},
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
                    path = "/airline-fleet/{id}",
                    method = RequestMethod.GET,
                    beanClass = AirlineFleetHandler.class,
                    beanMethod = "getAirlineFleetById",
                    operation = @Operation(
                            operationId = "getAirlineFleetById",
                            summary = "Get aircraft by ID",
                            description = "Returns an aircraft by ID",
                            tags = {"Airline Fleet \uD83D\uDC68\u200D✈\uFE0F ✈️"},
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Aircraft ID", required = true)
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
                    path = "/airline-fleet/airline/{airlineIcao}",
                    method = RequestMethod.GET,
                    beanClass = AirlineFleetHandler.class,
                    beanMethod = "getAirlineFleetByAirlineIcao",
                    operation = @Operation(
                            operationId = "getAirlineFleetByAirlineIcao",
                            summary = "Get all aircraft for a specific airline",
                            description = "Returns a list of all aircraft for a specific airline",
                            tags = {"Airline Fleet \uD83D\uDC68\u200D✈\uFE0F ✈️"},
                            parameters = {
                                    @Parameter(name = "airlineIcao", in = ParameterIn.PATH, description = "ICAO code of the airline", required = true)
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
                    path = "/airline/{airlineIcao}/count",
                    method = RequestMethod.GET,
                    beanClass = AirlineFleetHandler.class,
                    beanMethod = "getTotalAircraftCount",
                    operation = @Operation(
                            operationId = "getTotalAircraftCount",
                            summary = "Get total aircraft count for a specific airline",
                            description = "Returns the total number of aircraft for a specific airline",
                            tags = {"Airline Fleet \uD83D\uDC68\u200D✈\uFE0F ✈️"},
                            parameters = {
                                    @Parameter(name = "airlineIcao", in = ParameterIn.PATH, description = "ICAO code of the airline", required = true)
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
                    path = "/airline-fleet/aircraft-type/{aircraftTypeIcao}",
                    method = RequestMethod.GET,
                    beanClass = AirlineFleetHandler.class,
                    beanMethod = "getAirlineFleetByAircraftTypeIcao",
                    operation = @Operation(
                            operationId = "getAirlineFleetByAircraftTypeIcao",
                            summary = "Get all aircraft of a specific type",
                            description = "Returns a list of all aircraft of a specific type",
                            tags = {"Airline Fleet \uD83D\uDC68\u200D✈\uFE0F ✈️"},
                            parameters = {
                                    @Parameter(name = "aircraftTypeIcao", in = ParameterIn.PATH, description = "ICAO code of the aircraft type", required = true)
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
                    path = "/airline-fleet",
                    method = RequestMethod.POST,
                    beanClass = AirlineFleetHandler.class,
                    beanMethod = "createAirlineFleet",
                    operation = @Operation(
                            operationId = "createAirlineFleet",
                            summary = "Create a new aircraft in the fleet",
                            description = "Creates a new aircraft in the fleet",
                            tags = {"Airline Fleet \uD83D\uDC68\u200D✈\uFE0F ✈️"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Aircraft object to be created",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                                    implementation = com.execodex.sparrowair2.entities.AirlineFleet.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Aircraft created successfully",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airline-fleet/{id}",
                    method = RequestMethod.PUT,
                    beanClass = AirlineFleetHandler.class,
                    beanMethod = "updateAirlineFleet",
                    operation = @Operation(
                            operationId = "updateAirlineFleet",
                            summary = "Update an existing aircraft in the fleet",
                            description = "Updates an existing aircraft in the fleet",
                            tags = {"Airline Fleet \uD83D\uDC68\u200D✈\uFE0F ✈️"},
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Aircraft ID", required = true)
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Updated aircraft object",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                                    implementation = com.execodex.sparrowair2.entities.AirlineFleet.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Aircraft updated successfully",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airline-fleet/{id}",
                    method = RequestMethod.DELETE,
                    beanClass = AirlineFleetHandler.class,
                    beanMethod = "deleteAirlineFleet",
                    operation = @Operation(
                            operationId = "deleteAirlineFleet",
                            summary = "Delete an aircraft from the fleet",
                            description = "Deletes an aircraft from the fleet",
                            tags = {"Airline Fleet \uD83D\uDC68\u200D✈\uFE0F ✈️"},
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Aircraft ID", required = true)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "Aircraft deleted successfully"
                                    )
                            }
                    )
            )

    })
    @Bean
    public RouterFunction<ServerResponse> airlineFleetRoutesFunction() {
        return RouterFunctions.route()
                .path("/airline-fleet", builder -> builder
                        // GET /airline-fleet - Get all aircraft in the fleet
                        .GET("", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::getAllAirlineFleet)
                        // GET /airline-fleet/{id} - Get aircraft by ID
                        .GET("/{id}", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::getAirlineFleetById)
                        // GET /airline-fleet/airline/{airlineIcao} - Get all aircraft for a specific airline
                        .GET("/airline/{airlineIcao}", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::getAirlineFleetByAirlineIcao)
                        // GET /airline/{airlineIcao}/count - Get total aircraft count for a specific airline
                        .GET("/airline/{airlineIcao}/count", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::getTotalAircraftCount)
                        // GET /airline-fleet/aircraft-type/{aircraftTypeIcao} - Get all aircraft of a specific type
                        .GET("/aircraft-type/{aircraftTypeIcao}", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::getAirlineFleetByAircraftTypeIcao)
                        // POST /airline-fleet - Create a new aircraft in the fleet
                        .POST("", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::createAirlineFleet)
                        // PUT /airline-fleet/{id} - Update an existing aircraft in the fleet
                        .PUT("/{id}", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::updateAirlineFleet)
                        // DELETE /airline-fleet/{id} - Delete an aircraft from the fleet
                        .DELETE("/{id}", airlineFleetHandler::deleteAirlineFleet)
                        // GET /registration/{registration} - Get aircraft by registration
                        .GET("/registration/{registration}", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::getAirlineFleetByRegistration)
                )
                .build();
    }
}