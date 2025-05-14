package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.PassengerHandler;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class PassengerRoutes {

    private final PassengerHandler passengerHandler;

    public PassengerRoutes(PassengerHandler passengerHandler) {
        this.passengerHandler = passengerHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/passengers",
                    method = RequestMethod.GET,
                    beanClass = PassengerHandler.class,
                    beanMethod = "getAllPassengers",
                    operation = @Operation(
                            operationId = "getAllPassengers",
                            summary = "Get all passengers",
                            description = "Returns a list of all passengers",
                            tags = {"Passengers 👨‍💼"},  // Custom tag here
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
                    path = "/passengers/{id}",
                    method = RequestMethod.GET,
                    beanClass = PassengerHandler.class,
                    beanMethod = "getPassengerById",
                    operation = @Operation(
                            operationId = "getPassengerById",
                            summary = "Get passenger by ID",
                            description = "Returns a passenger by ID",
                            tags = {"Passengers 👨‍💼"},
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "ID of the passenger",
                                            in = ParameterIn.PATH,
                                            required = true,
                                            schema = @Schema(type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Passenger not found",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/passengers/search/dateOfBirth/{dateOfBirth}",
                    method = RequestMethod.GET,
                    beanClass = PassengerHandler.class,
                    beanMethod = "getPassengersByFirstNameAndLastName",
                    operation = @Operation(
                            operationId = "getPassengersByDateOfBirth",
                            summary = "Get passengers by date of birth",
                            description = "Returns a list of passengers by date of birth",
                            tags = {"Passengers 👨‍💼"},
                            parameters = {
                                    @Parameter(
                                            name = "dateOfBirth",
                                            description = "Date of birth of the passenger",
                                            in = ParameterIn.PATH,
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
                    path = "/passengers/search",
                    method = RequestMethod.GET,
                    beanClass = PassengerHandler.class,
                    beanMethod = "getPassengersByFirstNameAndLastName",
                    operation = @Operation(
                            operationId = "getPassengersByFirstNameAndLastName",
                            summary = "Get passengers by first name and last name",
                            description = "Returns a list of passengers by first name and last name",
                            tags = {"Passengers 👨‍💼"},
                            parameters = {
                                    @Parameter(
                                            name = "firstName",
                                            description = "First name of the passenger",
                                            in = ParameterIn.QUERY,
                                            required = true,
                                            schema = @Schema(type = "string")
                                    ),
                                    @Parameter(
                                            name = "lastName",
                                            description = "Last name of the passenger",
                                            in = ParameterIn.QUERY,
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
                    path = "/passengers",
                    method = RequestMethod.POST,
                    beanClass = PassengerHandler.class,
                    beanMethod = "createPassenger",
                    operation = @Operation(
                            operationId = "createPassenger",
                            summary = "Create a new passenger",
                            description = "Creates a new passenger",
                            tags = {"Passengers 👨‍💼"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Passenger object to be created",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.Passenger.class))

                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Passenger created successfully",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/passengers/{id}",
                    method = RequestMethod.PUT,
                    beanClass = PassengerHandler.class,
                    beanMethod = "updatePassenger",
                    operation = @Operation(
                            operationId = "updatePassenger",
                            summary = "Update an existing passenger",
                            description = "Updates an existing passenger",
                            tags = {"Passengers 👨‍💼"},
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "ID of the passenger to update",
                                            in = ParameterIn.PATH,
                                            required = true,
                                            schema = @Schema(type = "string")
                                    )
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Passenger object to be updated",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.Passenger.class))

                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Passenger updated successfully",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Passenger not found"
                                    )
                            }
                    )
            )
            ,
            @RouterOperation(
                    path = "/passengers/{id}",
                    method = RequestMethod.DELETE,
                    beanClass = PassengerHandler.class,
                    beanMethod = "deletePassenger",
                    operation = @Operation(
                            operationId = "deletePassenger",
                            summary = "Delete a passenger",
                            description = "Deletes a passenger by ID",
                            tags = {"Passengers 👨‍💼"},
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "ID of the passenger to delete",
                                            in = ParameterIn.PATH,
                                            required = true,
                                            schema = @Schema(type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "Passenger deleted successfully"
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Passenger not found"
                                    )
                            }
                    )
            )


    })
    public RouterFunction<ServerResponse> passengerRoutesFunction() {
        return RouterFunctions.route()
                .path("/passengers", builder -> builder
                        // GET /passengers - Get all passengers
                        .GET("", accept(MediaType.APPLICATION_JSON), passengerHandler::getAllPassengers)
                        .GET("", accept(MediaType.APPLICATION_XML), passengerHandler::getAllPassengers)
                        // GET /passengers/{id} - Get passenger by ID
                        .GET("/{id}", accept(MediaType.APPLICATION_JSON), passengerHandler::getPassengerById)
                        // GET /passengers/search?firstName=John&lastName=Doe - Get passengers by first name and last name
                        // GET /passangers/dateOfBirth - Get passengers by date of birth
                        .GET("/search/dateOfBirth/{dateOfBirth}", accept(MediaType.APPLICATION_JSON), passengerHandler::getPassengersByDateOfBirth)
                        .GET("/search", accept(MediaType.APPLICATION_JSON), passengerHandler::getPassengersByFirstNameAndLastName)
                        // POST /passengers - Create a new passenger
                        .POST("", accept(MediaType.APPLICATION_JSON), passengerHandler::createPassenger)
                        // PUT /passengers/{id} - Update an existing passenger
                        .PUT("/{id}", accept(MediaType.APPLICATION_JSON), passengerHandler::updatePassenger)
                        // DELETE /passengers/{id} - Delete a passenger
                        .DELETE("/{id}", passengerHandler::deletePassenger)
                )
                .build();
    }
}
