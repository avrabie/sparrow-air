package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.BookingSegmentHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
public class BookingSegmentRoute {

    private final BookingSegmentHandler bookingSegmentHandler;

    public BookingSegmentRoute(BookingSegmentHandler bookingSegmentHandler) {
        this.bookingSegmentHandler = bookingSegmentHandler;
    }

    @RouterOperations({
            @RouterOperation(
                    path = "/booking-segments",
                    method = RequestMethod.GET,
                    beanClass = BookingSegmentHandler.class,
                    beanMethod = "getAllBookingSegments",
                    operation = @Operation(
                            operationId = "getAllBookingSegments",
                            summary = "Get all booking segments",
                            description = "Returns a list of all booking segments",
                            tags = {"Booking Segments \uD83C\uDFAB\uD83D\uDCBA"},  // Custom tag here
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
                    path = "/booking-segments/{id}",
                    method = RequestMethod.GET,
                    beanClass = BookingSegmentHandler.class,
                    beanMethod = "getBookingSegmentById",
                    operation = @Operation(
                            operationId = "getBookingSegmentById",
                            summary = "Get booking segment by ID",
                            description = "Returns a booking segment by ID",
                            tags = {"Booking Segments \uD83C\uDFAB\uD83D\uDCBA"},  // Custom tag here
                            //todo finish with parameters for id here
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
    @Bean
    public RouterFunction<ServerResponse> bookingSegmentRoutes() {
        return RouterFunctions.route()
                .path("/booking-segments", builder -> builder
                        .GET("", bookingSegmentHandler::getAllBookingSegments)
                        .GET("/{id}", bookingSegmentHandler::getBookingSegmentById)
                        .POST("", bookingSegmentHandler::createBookingSegment)
                        .PUT("/{id}", bookingSegmentHandler::updateBookingSegment)
//                        .DELETE("/{id}", bookingSegmentHandler::deleteBookingSegment)

                )
                        .build();
    }
}
