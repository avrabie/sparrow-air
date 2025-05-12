package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.FlightHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class FlightRoutes {

    private final FlightHandler flightHandler;

    public FlightRoutes(FlightHandler flightHandler) {
        this.flightHandler = flightHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> flightRoutesFunction() {
        return RouterFunctions.route()
                .path("/flights", builder -> builder
                        // GET /flights - Get all flights
                        .GET("", accept(MediaType.APPLICATION_JSON), flightHandler::getAllFlights)
                        .GET("", accept(MediaType.APPLICATION_XML), flightHandler::getAllFlights)
                        // GET /flights/{id} - Get flight by ID
                        .GET("/{id}", accept(MediaType.APPLICATION_JSON), flightHandler::getFlightById)
                        // GET all the flights from a specific Airline Icao Code
                        .GET("/airline/{airlineIcaoCode}", accept(MediaType.APPLICATION_JSON), flightHandler::getFlightsByAirlineIcaoCode)
                        // POST /flights - Create a new flight
                        .POST("", accept(MediaType.APPLICATION_JSON), flightHandler::createFlight)
                        // PUT /flights/{id} - Update an existing flight
                        .PUT("/{id}", accept(MediaType.APPLICATION_JSON), flightHandler::updateFlight)
                        // DELETE /flights/{id} - Delete a flight
                        .DELETE("/{id}", flightHandler::deleteFlight)
                )
                .build();
    }
}