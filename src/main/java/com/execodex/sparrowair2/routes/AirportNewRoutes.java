package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.entities.skybrary.AirportNew;
import com.execodex.sparrowair2.handlers.AirportNewHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class AirportNewRoutes {

    private final AirportNewHandler airportNewHandler;

    public AirportNewRoutes(AirportNewHandler airportNewHandler) {
        this.airportNewHandler = airportNewHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/airportsnew",
                    method = RequestMethod.GET,
                    beanClass = AirportNewHandler.class,
                    beanMethod = "getAllAirports",
                    operation = @Operation(
                            operationId = "getAllAirportsNew",
                            summary = "Get all new airports",
                            description = "Returns a list of all new airports ✈️🌍. Supports pagination with optional 'page' and 'size' query parameters.",
                            tags = {"Airports New \uD83D\uDEEC\uD83C\uDF0E"},
                            parameters = {
                                    @Parameter(name = "page", in = ParameterIn.QUERY, required = false, description = "Page number (zero-based)",
                                              content = @Content(schema = @Schema(type = "integer"))),
                                    @Parameter(name = "size", in = ParameterIn.QUERY, required = false, description = "Page size",
                                              content = @Content(schema = @Schema(type = "integer")))
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
                    path = "/airportsnew/country/{country}",
                    method = RequestMethod.GET,
                    beanClass = AirportNewHandler.class,
                    beanMethod = "getAirportsByCountry",
                    operation = @Operation(
                            operationId = "getAirportsByCountry",
                            summary = "Get airports by country",
                            description = "Returns a list of airports in the specified country. Supports pagination with optional 'page' and 'size' query parameters.",
                            tags = {"Airports New \uD83D\uDEEC\uD83C\uDF0E"},
                            parameters = {
                                    @Parameter(name = "country", in = ParameterIn.PATH, required = true, description = "Country name",
                                              content = @Content(schema = @Schema(type = "string"))),
                                    @Parameter(name = "page", in = ParameterIn.QUERY, required = false, description = "Page number (zero-based)",
                                              content = @Content(schema = @Schema(type = "integer"))),
                                    @Parameter(name = "size", in = ParameterIn.QUERY, required = false, description = "Page size",
                                              content = @Content(schema = @Schema(type = "integer")))
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
                    path = "/airportsnew/distance",
                    method = RequestMethod.GET,
                    beanClass = AirportNewHandler.class,
                    beanMethod = "calculateDistance",
                    operation = @Operation(
                            operationId = "calculateDistanceNew",
                            summary = "Calculate distance between two new airports",
                            description = "Returns the distance in kilometers between two new airports identified by their ICAO codes",
                            tags = {"Airports New \uD83D\uDEEC\uD83C\uDF0E"},
                            parameters = {
                                    @Parameter(name = "from", in = ParameterIn.QUERY, required = true, description = "ICAO code of the departure airport",
                                              content = @Content(schema = @Schema(type = "string"))),
                                    @Parameter(name = "to", in = ParameterIn.QUERY, required = true, description = "ICAO code of the arrival airport",
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Missing required parameters"
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "One or both airports not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airportsnew/{icaoCode}",
                    method = RequestMethod.GET,
                    beanClass = AirportNewHandler.class,
                    beanMethod = "getAirportByIcaoCode",
                    operation = @Operation(
                            operationId = "getAirportNewByIcaoCode",
                            summary = "Get new airport by ICAO code",
                            description = "Returns a new airport \uD83D\uDEEC by ICAO code",
                            tags = {"Airports New \uD83D\uDEEC\uD83C\uDF0E"},
                            parameters = {
                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Airport not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airportsnew",
                    method = RequestMethod.POST,
                    beanClass = AirportNewHandler.class,
                    beanMethod = "createAirport",
                    operation = @Operation(
                            operationId = "createAirportNew",
                            summary = "Create a new airport",
                            tags = {"Airports New \uD83D\uDEEC\uD83C\uDF0E"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Airport to create",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AirportNew.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Airport created",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airportsnew/{icaoCode}",
                    method = RequestMethod.PUT,
                    beanClass = AirportNewHandler.class,
                    beanMethod = "updateAirport",
                    operation = @Operation(
                            operationId = "updateAirportNew",
                            summary = "Update an existing new airport",
                            tags = {"Airports New \uD83D\uDEEC\uD83C\uDF0E"},
                            parameters = {
                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true, 
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Airport to update",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AirportNew.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Airport updated",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Airport not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airportsnew/{icaoCode}",
                    method = RequestMethod.DELETE,
                    beanClass = AirportNewHandler.class,
                    beanMethod = "deleteAirport",
                    operation = @Operation(
                            operationId = "deleteAirportNew",
                            summary = "Delete new airport by ICAO code",
                            tags = {"Airports New \uD83D\uDEEC\uD83C\uDF0E"},
                            parameters = {
                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Airport deleted"),
                                    @ApiResponse(responseCode = "404", description = "Airport not found")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> airportNewRoutesFunction() {
        return RouterFunctions.route()
                .GET("airportsnew", accept(MediaType.APPLICATION_JSON), airportNewHandler::getAllAirports)
                .GET("airportsnew/country/{country}", accept(MediaType.APPLICATION_JSON), airportNewHandler::getAirportsByCountry)
                .GET("airportsnew/distance", accept(MediaType.APPLICATION_JSON), airportNewHandler::calculateDistance)
                .GET("airportsnew/{icaoCode}", accept(MediaType.APPLICATION_JSON), airportNewHandler::getAirportByIcaoCode)
                .POST("airportsnew", accept(MediaType.APPLICATION_JSON), airportNewHandler::createAirport)
                .PUT("airportsnew/{icaoCode}", accept(MediaType.APPLICATION_JSON), airportNewHandler::updateAirport)
                .DELETE("airportsnew/{icaoCode}", airportNewHandler::deleteAirport)
                .build();
    }
}
