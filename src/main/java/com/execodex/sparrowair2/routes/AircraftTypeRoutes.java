package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.AircraftTypeHandler;
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
public class AircraftTypeRoutes {

    private final AircraftTypeHandler aircraftTypeHandler;

    public AircraftTypeRoutes(AircraftTypeHandler aircraftTypeHandler) {
        this.aircraftTypeHandler = aircraftTypeHandler;
    }


    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/aircraft-types",
                    method = RequestMethod.GET,
                    beanClass = AircraftTypeHandler.class,
                    beanMethod = "getAllAircraftTypes",
                    operation = @Operation(
                            operationId = "getAllAircraftTypes",
                            summary = "Get all aircraft types",
                            tags = {"Aircraft Types ✈\uFE0F "},  // Custom tag here
                            description = "Returns a list of all aircraft types",
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
                    path = "/aircraft-types/{icaoCode}",
                    method = RequestMethod.GET,
                    beanClass = AircraftTypeHandler.class,
                    beanMethod = "getAircraftTypeByIcaoCode",
                    operation = @Operation(
                            operationId = "getAircraftTypeByIcaoCode",
                            summary = "Get aircraft type by ICAO code",
                            tags = {"Aircraft Types ✈\uFE0F "},
                            parameters = {
                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Aircraft type not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/aircraft-types",
                    method = RequestMethod.POST,
                    beanClass = AircraftTypeHandler.class,
                    beanMethod = "createAircraftType",
                    operation = @Operation(
                            operationId = "createAircraftType",
                            summary = "Create a new aircraft type",
                            tags = {"Aircraft Types ✈\uFE0F "},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Aircraft type to create",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.AircraftType.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Aircraft type created",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/aircraft-types/{icaoCode}",
                    method = RequestMethod.PUT,
                    beanClass = AircraftTypeHandler.class,
                    beanMethod = "updateAircraftType",
                    operation = @Operation(
                            operationId = "updateAircraftType",
                            summary = "Update an existing aircraft type",
                            tags = {"Aircraft Types ✈\uFE0F "},
                            parameters = {
                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true)
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Aircraft type to create",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.AircraftType.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Aircraft type updated",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Aircraft type not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/aircraft-types/{icaoCode}",
                    method = RequestMethod.DELETE,
                    beanClass = AircraftTypeHandler.class,
                    beanMethod = "deleteAircraftType",
                    operation = @Operation(
                            operationId = "deleteAircraftType",
                            summary = "Delete aircraft type by ICAO code",
                            tags = {"Aircraft Types ✈\uFE0F "},
                            parameters = {
                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true)
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Aircraft type deleted"),
                                    @ApiResponse(responseCode = "404", description = "Aircraft type not found")
                            }
                    )
            )



    })
    public RouterFunction<ServerResponse> aircraftTypeRoutesFunction() {
        return RouterFunctions.route()
                .path("/aircraft-types", builder -> builder
                        // GET /aircraft-types - Get all aircraft types
                        .GET("", accept(MediaType.APPLICATION_JSON), aircraftTypeHandler::getAllAircraftTypes)
                        // GET /aircraft-types/{icaoCode} - Get aircraft type by ICAO code
                        .GET("/{icaoCode}", accept(MediaType.APPLICATION_JSON), aircraftTypeHandler::getAircraftTypeByIcaoCode)
                        // POST /aircraft-types - Create a new aircraft type
                        .POST("", accept(MediaType.APPLICATION_JSON), aircraftTypeHandler::createAircraftType)
                        // PUT /aircraft-types/{icaoCode} - Update an existing aircraft type
                        .PUT("/{icaoCode}", accept(MediaType.APPLICATION_JSON), aircraftTypeHandler::updateAircraftType)
                        // DELETE /aircraft-types/{icaoCode} - Delete an aircraft type
                        .DELETE("/{icaoCode}", aircraftTypeHandler::deleteAircraftType)
                )
                .build();
    }
}