package com.execodex.sparrowair2.configs;

import com.execodex.sparrowair2.handlers.AircraftTypeHandler;
import com.execodex.sparrowair2.routes.AircraftTypeRoutes;
import com.execodex.sparrowair2.routes.AirportRoutes;
import com.execodex.sparrowair2.routes.HelloRoutes;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FunctionalEndpointsOpenApiConfig {


//    @Bean
//    @RouterOperations({
//            @RouterOperation(
//                    path = "/aircraft-types",
//                    method = RequestMethod.GET,
//                    beanClass = AircraftTypeHandler.class,
//                    beanMethod = "getAllAircraftTypes",
//                    operation = @Operation(
//                            operationId = "getAllAircraftTypes",
//                            summary = "Get all aircraft types",
//                            description = "Returns a list of all aircraft types",
//                            responses = {
//                                    @ApiResponse(
//                                            responseCode = "200",
//                                            description = "Successful operation",
//                                            content = @Content(mediaType = "application/json")
//                                    )
//                            }
//                    )
//            ),
//            @RouterOperation(
//                    path = "/aircraft-types/{icaoCode}",
//                    method = RequestMethod.GET,
//                    beanClass = AircraftTypeHandler.class,
//                    beanMethod = "getAircraftTypeByIcaoCode",
//                    operation = @Operation(
//                            operationId = "getAircraftTypeByIcaoCode",
//                            summary = "Get aircraft type by ICAO code",
//                            parameters = {
//                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true)
//                            },
//                            responses = {
//                                    @ApiResponse(
//                                            responseCode = "200",
//                                            description = "Successful operation",
//                                            content = @Content(mediaType = "application/json")
//                                    ),
//                                    @ApiResponse(
//                                            responseCode = "404",
//                                            description = "Aircraft type not found"
//                                    )
//                            }
//                    )
//            ),
//            @RouterOperation(
//                    path = "/aircraft-types",
//                    method = RequestMethod.POST,
//                    beanClass = AircraftTypeHandler.class,
//                    beanMethod = "createAircraftType",
//                    operation = @Operation(
//                            operationId = "createAircraftType",
//                            summary = "Create a new aircraft type",
//                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                                    description = "Aircraft type to create",
//                                    required = true,
//                                    content = @Content(
//                                            mediaType = "application/json",
//                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.AircraftType.class)
//                                    )
//                            ),
//                            responses = {
//                                    @ApiResponse(
//                                            responseCode = "201",
//                                            description = "Aircraft type created",
//                                            content = @Content(mediaType = "application/json")
//                                    )
//                            }
//                    )
//            ),
//            @RouterOperation(
//                    path = "/aircraft-types/{icaoCode}",
//                    method = RequestMethod.PUT,
//                    beanClass = AircraftTypeHandler.class,
//                    beanMethod = "updateAircraftType",
//                    operation = @Operation(
//                            operationId = "updateAircraftType",
//                            summary = "Update an existing aircraft type",
//                            parameters = {
//                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true)
//                            },
//                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                                    description = "Aircraft type to create",
//                                    required = true,
//                                    content = @Content(
//                                            mediaType = "application/json",
//                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.AircraftType.class)
//                                    )
//                            ),
//                            responses = {
//                                    @ApiResponse(
//                                            responseCode = "200",
//                                            description = "Aircraft type updated",
//                                            content = @Content(mediaType = "application/json")
//                                    ),
//                                    @ApiResponse(
//                                            responseCode = "404",
//                                            description = "Aircraft type not found"
//                                    )
//                            }
//                    )
//            ),
//            @RouterOperation(
//                    path = "/aircraft-types/{icaoCode}",
//                    method = RequestMethod.DELETE,
//                    beanClass = AircraftTypeHandler.class,
//                    beanMethod = "deleteAircraftType",
//                    operation = @Operation(
//                            operationId = "deleteAircraftType",
//                            summary = "Delete aircraft type by ICAO code",
//                            parameters = {
//                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true)
//                            },
//                            responses = {
//                                    @ApiResponse(responseCode = "204", description = "Aircraft type deleted"),
//                                    @ApiResponse(responseCode = "404", description = "Aircraft type not found")
//                            }
//                    )
//            )
//
//
//
//
//    })
//    public RouterFunction<ServerResponse> combinedRoutes(
//            HelloRoutes helloRoutes,
//            AircraftTypeRoutes aircraftTypeRoutes,
//            AirportRoutes airportRoutes) {
//        return aircraftTypeRoutes.aircraftTypeRoutesFunction()
//                .and(helloRoutes.helloRoute())
//                .and(airportRoutes.airportRoutesFunction());

//    }

//    @Bean
//    @RouterOperations({
//            @RouterOperation(
//                    path = "/airports",
//                    method = RequestMethod.GET,
//                    beanClass = AirportRoutes.class,
//                    beanMethod = "getAllAirports",
//                    operation = @Operation(
//                            operationId = "getAllAirports",
//                            summary = "Get all airports",
//                            description = "Returns a list of all airports",
//                            responses = {
//                                    @ApiResponse(
//                                            responseCode = "200",
//                                            description = "Successful operation",
//                                            content = @Content(mediaType = "application/json")
//                                    )
//                            }
//                    )
//            )
//            ,
//            @RouterOperation(
//                    path = "/airports/{icaoCode}",
//                    method = RequestMethod.GET,
//                    beanClass = AirportRoutes.class,
//                    beanMethod = "getAirportByIcaoCode",
//                    operation = @Operation(
//                            operationId = "getAirportByIcaoCode",
//                            summary = "Get airport by ICAO code",
//                            parameters = {
//                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true)
//                            },
//                            responses = {
//                                    @ApiResponse(
//                                            responseCode = "200",
//                                            description = "Successful operation",
//                                            content = @Content(mediaType = "application/json")
//                                    ),
//                                    @ApiResponse(
//                                            responseCode = "404",
//                                            description = "Airport not found"
//                                    )
//                            }
//                    )
//            ),
//            @RouterOperation(
//                    path = "/airports",
//                    method = RequestMethod.POST,
//                    beanClass = AirportRoutes.class,
//                    beanMethod = "createAirport",
//                    operation = @Operation(
//                            operationId = "createAirport",
//                            summary = "Create a new airport",
//                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                                    description = "Airport to create",
//                                    required = true,
//                                    content = @Content(
//                                            mediaType = "application/json",
//                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.Airport.class)
//                                    )
//                            ),
//                            responses = {
//                                    @ApiResponse(
//                                            responseCode = "201",
//                                            description = "Airport created",
//                                            content = @Content(mediaType = "application/json")
//                                    )
//                            }
//                    )
//            ),
//            @RouterOperation(
//                    path = "/airports/{icaoCode}",
//                    method = RequestMethod.PUT,
//                    beanClass = AirportRoutes.class,
//                    beanMethod = "updateAirport",
//                    operation = @Operation(
//                            operationId = "updateAirport",
//                            summary = "Update an existing airport",
//                            parameters = {
//                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true)
//                            },
//                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                                    description = "Airport to create",
//                                    required = true,
//                                    content = @Content(
//                                            mediaType = "application/json",
//                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.Airport.class)
//                                    )
//                            ),
//                            responses = {
//                                    @ApiResponse(
//                                            responseCode = "200",
//                                            description = "Airport updated",
//                                            content = @Content(mediaType = "application/json")
//                                    ),
//                                    @ApiResponse(
//                                            responseCode = "404",
//                                            description = "Airport not found"
//                                    )
//                            }
//                    )
//            ),
//            @RouterOperation(
//                    path = "/airports/{icaoCode}",
//                    method = RequestMethod.DELETE,
//                    beanClass = AirportRoutes.class,
//                    beanMethod = "deleteAirport",
//                    operation = @Operation(
//                            operationId = "deleteAirport",
//                            summary = "Delete airport by ICAO code",
//                            parameters = {
//                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true)
//                            },
//                            responses = {
//                                    @ApiResponse(responseCode = "204", description = "Airport deleted"),
//                                    @ApiResponse(responseCode = "404", description = "Airport not found")
//                            }
//                    )
//            )
//    })
//    public RouterFunction<ServerResponse> airportRoutes(AirportRoutes airportRoutes) {
//        return airportRoutes.airportRoutesFunction();
//    }

}
