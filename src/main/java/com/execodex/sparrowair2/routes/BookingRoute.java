package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.BookingHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class BookingRoute {
    // Define your routes here
    // For example, you can use RouterFunction to define the routes for booking operations

    // Example route for creating a booking
     @Bean
     @RouterOperations({
             @RouterOperation(
                     path = "/bookings",
                     method = RequestMethod.GET,
                     beanClass = BookingHandler.class,
                     beanMethod = "handleGetAllBookings",
                     operation = @Operation(
                             operationId = "getAllBookings",
                             summary = "Get all bookings",
                             description = "Returns a list of all bookings",
                             tags = {"Bookings \uD83C\uDFAB\uD83D\uDCBA"},  // Custom tag here
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
                        path = "/bookings/{id}",
                        method = RequestMethod.GET,
                        beanClass = BookingHandler.class,
                        beanMethod = "handleGetBookingById",
                        operation = @Operation(
                                operationId = "getBookingById",
                                summary = "Get booking by ID",
                                description = "Returns a booking by ID",
                                tags = {"Bookings \uD83C\uDFAB\uD83D\uDCBA"},  // Custom tag here
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
                     path = "/bookings",
                     method = RequestMethod.POST,
                     beanClass = BookingHandler.class,
                     beanMethod = "createBooking",
                     operation = @Operation(
                             operationId = "createBooking",
                             summary = "Create a new booking",
                             description = "Creates a new booking",
                             tags = {"Bookings \uD83C\uDFAB\uD83D\uDCBA"},  // Custom tag here
                             requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                     description = "Booking ID",
                                     required = true,
                                     content = @Content(
                                             mediaType = "application/json",
                                             schema = @Schema(implementation = com.execodex.sparrowair2.entities.Booking.class)  // Assuming you have a Booking class
                                     )
                             ),
                             responses = {
                                     @ApiResponse(
                                             responseCode = "201",
                                             description = "Booking created successfully",
                                             content = @Content(mediaType = "application/json")
                                     )
                             }
                     )
             )
     })
     public RouterFunction<ServerResponse> bookingRoutes(BookingHandler bookingHandler) {
         return RouterFunctions.route()
                 .GET("/bookings", bookingHandler::handleGetAllBookings)
                 .GET("/bookings/{id}", bookingHandler::handleGetBookingById)
                 .POST("/bookings", bookingHandler::createBooking)
                 .build();
     }
}
