package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.entities.kaggle.AirlineNew;
import com.execodex.sparrowair2.handlers.AirlineNewHandler;
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
public class AirlineNewRoutes {

    private final AirlineNewHandler airlineNewHandler;

    public AirlineNewRoutes(AirlineNewHandler airlineNewHandler) {
        this.airlineNewHandler = airlineNewHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/airlinesnew",
                    method = RequestMethod.GET,
                    beanClass = AirlineNewHandler.class,
                    beanMethod = "getAllAirlines",
                    operation = @Operation(
                            operationId = "getAllAirlinesNew",
                            summary = "Get all new airlines",
                            description = "Returns a list of all new airlines ✈️🌍. Supports pagination with optional 'page' and 'size' query parameters.",
                            tags = {"Airlines New ✈️🌍"},
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
                    path = "/airlinesnew/country/{country}",
                    method = RequestMethod.GET,
                    beanClass = AirlineNewHandler.class,
                    beanMethod = "getAirlinesByCountry",
                    operation = @Operation(
                            operationId = "getAirlinesByCountry",
                            summary = "Get airlines by country",
                            description = "Returns a list of airlines in the specified country. Supports pagination with optional 'page' and 'size' query parameters.",
                            tags = {"Airlines New ✈️🌍"},
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
                    path = "/airlinesnew/active/{active}",
                    method = RequestMethod.GET,
                    beanClass = AirlineNewHandler.class,
                    beanMethod = "getAirlinesByActive",
                    operation = @Operation(
                            operationId = "getAirlinesByActive",
                            summary = "Get airlines by active status",
                            description = "Returns a list of airlines with the specified active status. Supports pagination with optional 'page' and 'size' query parameters.",
                            tags = {"Airlines New ✈️🌍"},
                            parameters = {
                                    @Parameter(name = "active", in = ParameterIn.PATH, required = true, description = "Active status (Y/N)",
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
                    path = "/airlinesnew/{airlineId}",
                    method = RequestMethod.GET,
                    beanClass = AirlineNewHandler.class,
                    beanMethod = "getAirlineById",
                    operation = @Operation(
                            operationId = "getAirlineNewById",
                            summary = "Get new airline by ID",
                            description = "Returns a new airline ✈️ by ID",
                            tags = {"Airlines New ✈️🌍"},
                            parameters = {
                                    @Parameter(name = "airlineId", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "integer")))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Airline not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airlinesnew",
                    method = RequestMethod.POST,
                    beanClass = AirlineNewHandler.class,
                    beanMethod = "createAirline",
                    operation = @Operation(
                            operationId = "createAirlineNew",
                            summary = "Create a new airline",
                            tags = {"Airlines New ✈️🌍"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Airline to create",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AirlineNew.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Airline created",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airlinesnew/{airlineId}",
                    method = RequestMethod.PUT,
                    beanClass = AirlineNewHandler.class,
                    beanMethod = "updateAirline",
                    operation = @Operation(
                            operationId = "updateAirlineNew",
                            summary = "Update an existing new airline",
                            tags = {"Airlines New ✈️🌍"},
                            parameters = {
                                    @Parameter(name = "airlineId", in = ParameterIn.PATH, required = true, 
                                              content = @Content(schema = @Schema(type = "integer")))
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Airline to update",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AirlineNew.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Airline updated",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Airline not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airlinesnew/{airlineId}",
                    method = RequestMethod.DELETE,
                    beanClass = AirlineNewHandler.class,
                    beanMethod = "deleteAirline",
                    operation = @Operation(
                            operationId = "deleteAirlineNew",
                            summary = "Delete new airline by ID",
                            tags = {"Airlines New ✈️🌍"},
                            parameters = {
                                    @Parameter(name = "airlineId", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "integer")))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Airline deleted"),
                                    @ApiResponse(responseCode = "404", description = "Airline not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airlinesnew/icao/{icaoCode}",
                    method = RequestMethod.GET,
                    beanClass = AirlineNewHandler.class,
                    beanMethod = "getAirlineByIcaoCode",
                    operation = @Operation(
                            operationId = "getAirlineByIcaoCode",
                            summary = "Get airline by ICAO code",
                            description = "Returns an airline with the specified ICAO code",
                            tags = {"Airlines New ✈️🌍"},
                            parameters = {
                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true, description = "ICAO code",
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
                                            description = "Airline not found"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> airlineNewRoutesFunction() {
        return RouterFunctions.route()
                .GET("airlinesnew", accept(MediaType.APPLICATION_JSON), airlineNewHandler::getAllAirlines)
                .GET("airlinesnew/country/{country}", accept(MediaType.APPLICATION_JSON), airlineNewHandler::getAirlinesByCountry)
                .GET("airlinesnew/active/{active}", accept(MediaType.APPLICATION_JSON), airlineNewHandler::getAirlinesByActive)
                .GET("airlinesnew/icao/{icaoCode}", accept(MediaType.APPLICATION_JSON), airlineNewHandler::getAirlineByIcaoCode)
                .GET("airlinesnew/{airlineId}", accept(MediaType.APPLICATION_JSON), airlineNewHandler::getAirlineById)
                .POST("airlinesnew", accept(MediaType.APPLICATION_JSON), airlineNewHandler::createAirline)
                .PUT("airlinesnew/{airlineId}", accept(MediaType.APPLICATION_JSON), airlineNewHandler::updateAirline)
                .DELETE("airlinesnew/{airlineId}", airlineNewHandler::deleteAirline)
                .build();
    }
}
