package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.BookingHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                     beanClass = BookingHandler.class,
                     beanMethod = "handleGetAllBookings",
                     operation = @Operation(
                             operationId = "getAllBookings",
                             summary = "Get all bookings",
                             description = "Returns a list of all bookings",
                             tags = {"Bookings \uD83D\uDC68\u200D✈\uFE0F "},  // Custom tag here
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
     public RouterFunction<ServerResponse> bookingRoutes(BookingHandler bookingHandler) {
         return RouterFunctions.route()
                 .GET("/bookings", bookingHandler::handleGetAllBookings)
    //             .POST("/api/bookings", handlers::handleCreateBooking)
                 .build();
     }
}
