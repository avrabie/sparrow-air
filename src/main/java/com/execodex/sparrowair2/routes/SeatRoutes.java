package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.SeatHandler;
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
public class SeatRoutes {

    private final SeatHandler seatHandler;

    public SeatRoutes(SeatHandler seatHandler) {
        this.seatHandler = seatHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/seats",
                    method = RequestMethod.GET,
                    beanClass = SeatHandler.class,
                    beanMethod = "getAllSeats",
                    operation = @Operation(
                            operationId = "getAllSeats",
                            summary = "Get all seats",
                            description = "Returns a list of all seats",
                            tags = {"Seats \uD83D\uDCBA"},  // Custom tag here
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
                    path = "/seats/{seatId}",
                    method = RequestMethod.GET,
                    beanClass = SeatHandler.class,
                    beanMethod = "getSeatById",
                    operation = @Operation(
                            operationId = "getSeatById",
                            summary = "Get seat by ID",
                            description = "Returns a seat by ID",
                            tags = {"Seats \uD83D\uDCBA"},
                            parameters = {
                                    @Parameter(
                                            name = "seatId",
                                            in = ParameterIn.PATH,
                                            description = "ID of the seat to be fetched",
                                            required = true,
                                            schema = @Schema(type = "integer")
                                    )
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
                    path = "/seats/flights/{flightId}",
                    method = RequestMethod.GET,
                    beanClass = SeatHandler.class,
                    beanMethod = "getSeatsByFlightId",
                    operation = @Operation(
                            operationId = "getSeatsByFlightId",
                            summary = "Get seats by flight ID",
                            description = "Returns a list of seats by flight ID",
                            tags = {"Seats \uD83D\uDCBA"},
                            parameters = {
                                    @Parameter(
                                            name = "flightId",
                                            in = ParameterIn.PATH,
                                            description = "ID of the flight to fetch seats for",
                                            required = true,
                                            schema = @Schema(type = "integer")
                                    )
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
                    path = "/seats/flightNumber/{flightNumber}",
                    method = RequestMethod.GET,
                    beanClass = SeatHandler.class,
                    beanMethod = "getSeatsByFlightNumber",
                    operation = @Operation(
                            operationId = "getSeatsByFlightNumber",
                            summary = "Get seats by flight number",
                            description = "Returns a list of seats by flight number",
                            tags = {"Seats \uD83D\uDCBA"},
                            parameters = {
                                    @Parameter(
                                            name = "flightNumber",
                                            in = ParameterIn.PATH,
                                            description = "Flight number to fetch seats for",
                                            required = true,
                                            schema = @Schema(type = "string")
                                    )
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
                    path = "/seats/airline/{airlineIcao}/flightNumber/{flightNumber}",
                    method = RequestMethod.GET,
                    beanClass = SeatHandler.class,
                    beanMethod = "getSeatsByAirlineIcaoAndFlightNumber",
                    operation = @Operation(
                            operationId = "getSeatsByAirlineIcaoAndFlightNumber",
                            summary = "Get seats by airline ICAO and flight number",
                            description = "Returns a list of seats by airline ICAO and flight number",
                            tags = {"Seats \uD83D\uDCBA"},
                            parameters = {
                                    @Parameter(
                                            name = "airlineIcao",
                                            in = ParameterIn.PATH,
                                            description = "ICAO code of the airline",
                                            required = true,
                                            schema = @Schema(type = "string")
                                    ),
                                    @Parameter(
                                            name = "flightNumber",
                                            in = ParameterIn.PATH,
                                            description = "Flight number",
                                            required = true,
                                            schema = @Schema(type = "string")
                                    )
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
                    path = "/seats/{seatId}",
                    method = RequestMethod.PUT,
                    beanClass = SeatHandler.class,
                    beanMethod = "updateSeat",
                    operation = @Operation(
                            operationId = "updateSeat",
                            summary = "Update status seat by ID",
                            description = "Updates a seat by ID",
                            tags = {"Seats \uD83D\uDCBA"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Seat object to be updated",
                                    required = true,
                                    content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = com.execodex.sparrowair2.entities.Seat.class))
                            ),
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
    public RouterFunction<ServerResponse> seatRoutesFunction() {
        return RouterFunctions.route()
                .path("/seats", builder -> builder
                        // GET /seats - Get all seats
                        .GET("", accept(MediaType.APPLICATION_JSON), seatHandler::getAllSeats)
                        .GET("{seatId}", accept(MediaType.APPLICATION_JSON), seatHandler::getSeatById)
                        .GET("/flights/{flightId}", accept(MediaType.APPLICATION_JSON), seatHandler::getSeatsByFlightId)
                        // not really the proper way, because you might have multiple flights with the same flight number from different airlines
                        .GET("/flightNumber/{flightNumber}", accept(MediaType.APPLICATION_JSON), seatHandler::getSeatsByFlightNumber)
                        // proper way, but you need to know the airline ICAO code
                        .GET("airline/{airlineIcao}/flightNumber/{flightNumber}", accept(MediaType.APPLICATION_JSON), seatHandler::getSeatsByAirlineIcaoAndFlightNumber)
                        // update seat
                        .PUT("{seatId}", accept(MediaType.APPLICATION_JSON), seatHandler::updateSeat)

                )
                .build();
    }
}