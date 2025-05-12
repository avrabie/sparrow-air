package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.SeatHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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
    public RouterFunction<ServerResponse> seatRoutesFunction() {
        return RouterFunctions.route()
                .path("/seats", builder -> builder
                        // GET /seats - Get all seats
                        .GET("", accept(MediaType.APPLICATION_JSON), seatHandler::getAllSeats)
                        .GET("/flights/{flightId}", accept(MediaType.APPLICATION_JSON), seatHandler::getSeatsByFlightId)
                        .GET("airline/{airlineIcao}/flightNumber/{flightNumber}", accept(MediaType.APPLICATION_JSON), seatHandler::getSeatsByAirlineIcaoAndFlightNumber)
                )
                .build();
    }
}