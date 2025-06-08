package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.AircraftHandler;
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
public class AircraftRoutes {

    private final AircraftHandler aircraftHandler;

    public AircraftRoutes(AircraftHandler aircraftHandler) {
        this.aircraftHandler = aircraftHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/aircraft",
                    method = RequestMethod.GET,
                    beanClass = AircraftHandler.class,
                    beanMethod = "getAllAircraft",
                    operation = @Operation(
                            operationId = "getAllAircraft",
                            summary = "Get all aircraft",
                            description = "Returns a list of all aircraft ✈️",
                            tags = {"Aircraft ✈️"},
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
                    path = "/aircraft/{icaoCode}",
                    method = RequestMethod.GET,
                    beanClass = AircraftHandler.class,
                    beanMethod = "getAircraftByIcaoCode",
                    operation = @Operation(
                            operationId = "getAircraftByIcaoCode",
                            summary = "Get aircraft by ICAO code",
                            description = "Returns an aircraft ✈️ by ICAO code",
                            tags = {"Aircraft ✈️"},
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
                                            description = "Aircraft not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/aircraft",
                    method = RequestMethod.POST,
                    beanClass = AircraftHandler.class,
                    beanMethod = "createAircraft",
                    operation = @Operation(
                            operationId = "createAircraft",
                            summary = "Create a new aircraft",
                            tags = {"Aircraft ✈️"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Aircraft to create",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.Aircraft.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Aircraft created",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/aircraft/{icaoCode}",
                    method = RequestMethod.PUT,
                    beanClass = AircraftHandler.class,
                    beanMethod = "updateAircraft",
                    operation = @Operation(
                            operationId = "updateAircraft",
                            summary = "Update an existing aircraft",
                            tags = {"Aircraft ✈️"},
                            parameters = {
                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true, 
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Aircraft to update",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.Aircraft.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Aircraft updated",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Aircraft not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/aircraft/{icaoCode}",
                    method = RequestMethod.DELETE,
                    beanClass = AircraftHandler.class,
                    beanMethod = "deleteAircraft",
                    operation = @Operation(
                            operationId = "deleteAircraft",
                            summary = "Delete aircraft by ICAO code",
                            tags = {"Aircraft ✈️"},
                            parameters = {
                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Aircraft deleted"),
                                    @ApiResponse(responseCode = "404", description = "Aircraft not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/aircraft/skybrary/{icaoCode}",
                    method = RequestMethod.GET,
                    beanClass = AircraftHandler.class,
                    beanMethod = "parseOnlineAircraft",
                    operation = @Operation(
                            operationId = "parseOnlineAircraft",
                            summary = "Parse online aircraft data by ICAO code",
                            tags = {"Aircraft ✈️"},
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
                                            description = "Aircraft not found"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> aircraftRoutesFunction() {
        return RouterFunctions.route()
                .GET("aircraft", accept(MediaType.APPLICATION_JSON), aircraftHandler::getAllAircraft)
                .GET("aircraft/{icaoCode}", accept(MediaType.APPLICATION_JSON), aircraftHandler::getAircraftByIcaoCode)
                .POST("aircraft", accept(MediaType.APPLICATION_JSON), aircraftHandler::createAircraft)
                .PUT("aircraft/{icaoCode}", accept(MediaType.APPLICATION_JSON), aircraftHandler::updateAircraft)
                .DELETE("aircraft/{icaoCode}", aircraftHandler::deleteAircraft)
                .GET("aircraft/skybrary/{icaoCode}", accept(MediaType.APPLICATION_JSON), aircraftHandler::parseOnlineAircraft)
                .build();
    }
}