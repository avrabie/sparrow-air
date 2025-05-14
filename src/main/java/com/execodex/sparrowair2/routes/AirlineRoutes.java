package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.AirlineHandler;
import io.swagger.v3.oas.annotations.Operation;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class AirlineRoutes {

    private final AirlineHandler airlineHandler;

    public AirlineRoutes(AirlineHandler airlineHandler) {
        this.airlineHandler = airlineHandler;
    }


    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/airlines",
                    method = RequestMethod.GET,
                    beanClass = AirlineHandler.class,
                    beanMethod = "getAllAirlines",
                    operation = @Operation(
                            operationId = "getAllAirlines",
                            summary = "Get all airlines",
                            description = "Returns a list of all airlines",
                            tags = {"Airlines \uD83D\uDEE9 \uD83E\uDDD1\uD83C\uDFFC\u200D✈\uFE0F"},  // Custom tag here
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
                    path = "/airlines/{icaoCode}",
                    method = RequestMethod.GET,
                    beanClass = AirlineHandler.class,
                    beanMethod = "getAirlineByIcaoCode",
                    operation = @Operation(
                            operationId = "getAirlineByIcaoCode",
                            summary = "Get airline by ICAO code",
                            description = "Returns an airline by ICAO code",
                            tags = {"Airlines \uD83D\uDEE9 \uD83E\uDDD1\uD83C\uDFFC\u200D✈\uFE0F"},
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
                    path = "/airlines",
                    method = RequestMethod.POST,
                    beanClass = AirlineHandler.class,
                    beanMethod = "createAirline",
                    operation = @Operation(
                            operationId = "createAirline",
                            summary = "Create a new airline",
                            description = "Creates a new airline",
                            tags = {"Airlines \uD83D\uDEE9 \uD83E\uDDD1\uD83C\uDFFC\u200D✈\uFE0F"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Airline object to be created",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.Airline.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Airline created successfully",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airlines/{icaoCode}",
                    method = RequestMethod.PUT,
                    beanClass = AirlineHandler.class,
                    beanMethod = "updateAirline",
                    operation = @Operation(
                            operationId = "updateAirline",
                            summary = "Update an existing airline",
                            description = "Updates an airline by ICAO code",
                            tags = {"Airlines \uD83D\uDEE9 \uD83E\uDDD1\uD83C\uDFFC\u200D✈\uFE0F"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Updated airline object",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.Airline.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Airline updated successfully",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/airlines/{icaoCode}",
                    method = RequestMethod.DELETE,
                    beanClass = AirlineHandler.class,
                    beanMethod = "deleteAirline",
                    operation = @Operation(
                            operationId = "deleteAirline",
                            summary = "Delete an airline",
                            description = "Deletes an airline by ICAO code",
                            tags = {"Airlines \uD83D\uDEE9 \uD83E\uDDD1\uD83C\uDFFC\u200D✈\uFE0F"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "Airline deleted successfully"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> airlineRoutesFunction() {
        return RouterFunctions.route()
                .path("/airlines", builder -> builder
                        // GET /airlines - Get all airlines
                        .GET("", accept(MediaType.APPLICATION_JSON), airlineHandler::getAllAirlines)
                        .GET("", accept(MediaType.APPLICATION_XML), airlineHandler::getAllAirlines)
                        // GET /airlines/{icaoCode} - Get airline by ICAO code
                        .GET("/{icaoCode}", accept(MediaType.APPLICATION_JSON), airlineHandler::getAirlineByIcaoCode)
                        // POST /airlines - Create a new airline
                        .POST("", accept(MediaType.APPLICATION_JSON), airlineHandler::createAirline)
                        // PUT /airlines/{icaoCode} - Update an existing airline
                        .PUT("/{icaoCode}", accept(MediaType.APPLICATION_JSON), airlineHandler::updateAirline)
                        // DELETE /airlines/{icaoCode} - Delete an airline
                        .DELETE("/{icaoCode}", airlineHandler::deleteAirline)
                )
                .build();
    }
}