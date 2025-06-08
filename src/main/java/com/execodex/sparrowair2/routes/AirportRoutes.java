package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.AirportHandler;
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
public class AirportRoutes {

    private final AirportHandler airportHandler;

    public AirportRoutes(AirportHandler airportHandler) {
        this.airportHandler = airportHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/airports",
                    method = RequestMethod.GET,
                    beanClass = AirportHandler.class,
                    beanMethod = "getAllAirports",
                    operation = @Operation(
                            operationId = "getAllAirports",
                            summary = "Get all airports",
                            description = "Returns a list of all airports ✈️🌍",  // Emoji here
                            tags = {"Airports \uD83D\uDEEC\uD83C\uDF0E"},  // Custom tag here
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
                    path = "/airports/distance",
                    method = RequestMethod.GET,
                    beanClass = AirportHandler.class,
                    beanMethod = "calculateDistance",
                    operation = @Operation(
                            operationId = "calculateDistance",
                            summary = "Calculate distance between two airports",
                            description = "Returns the distance in kilometers between two airports identified by their ICAO codes",
                            tags = {"Airports \uD83D\uDEEC\uD83C\uDF0E"},
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
                    path = "/airports/{icaoCode}",
                    method = RequestMethod.GET,
                    beanClass = AirportHandler.class,
                    beanMethod = "getAirportByIcaoCode",
                    operation = @Operation(
                            operationId = "getAirportByIcaoCode",
                            summary = "Get airport by ICAO code",
                            description = "Returns an airport \uD83D\uDEEC by ICAO code",
                            tags = {"Airports \uD83D\uDEEC\uD83C\uDF0E"},
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
                    path = "/airports",
                    method = RequestMethod.POST,
                    beanClass = AirportHandler.class,
                    beanMethod = "createAirport",
                    operation = @Operation(
                            operationId = "createAirport",
                            summary = "Create a new airport",
                            tags = {"Airports \uD83D\uDEEC\uD83C\uDF0E"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Airport to create",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.Airport.class)
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
                    path = "/airports/{icaoCode}",
                    method = RequestMethod.PUT,
                    beanClass = AirportHandler.class,
                    beanMethod = "updateAirport",
                    operation = @Operation(
                            operationId = "updateAirport",
                            summary = "Update an existing airport",
                            tags = {"Airports \uD83D\uDEEC\uD83C\uDF0E"},
                            parameters = {
                                    @Parameter(name = "icaoCode", in = ParameterIn.PATH, required = true, 
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Airport to create",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.Airport.class)
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
                    path = "/airports/{icaoCode}",
                    method = RequestMethod.DELETE,
                    beanClass = AirportHandler.class,
                    beanMethod = "deleteAirport",
                    operation = @Operation(
                            operationId = "deleteAirport",
                            summary = "Delete airport by ICAO code",
                            tags = {"Airports \uD83D\uDEEC\uD83C\uDF0E"},
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
    public RouterFunction<ServerResponse> airportRoutesFunction() {
        return RouterFunctions.route()
//                .path("/airports", builder -> builder
                        // GET /airports - Get all airports
                        .GET("airports", accept(MediaType.APPLICATION_JSON), airportHandler::getAllAirports)
                        // GET /airports/distance - Calculate distance between two airports
                        .GET("airports/distance", accept(MediaType.APPLICATION_JSON), airportHandler::calculateDistance)
                        // GET /airports/{icaoCode} - Get airport by ICAO code
                        .GET("airports/{icaoCode}", accept(MediaType.APPLICATION_JSON), airportHandler::getAirportByIcaoCode)
                        // POST /airports - Create a new airport
                        .POST("airports", accept(MediaType.APPLICATION_JSON), airportHandler::createAirport)
                        // PUT /airports/{icaoCode} - Update an existing airport
                        .PUT("airports/{icaoCode}", accept(MediaType.APPLICATION_JSON), airportHandler::updateAirport)
                        // DELETE /airports/{icaoCode} - Delete an airport
                        .DELETE("airports/{icaoCode}", airportHandler::deleteAirport)
//                )
                .build();
    }
}
