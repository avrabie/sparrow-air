package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.AirportHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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
    public RouterFunction<ServerResponse> airportRoutesFunction() {
        return RouterFunctions.route()
                .path("/airports", builder -> builder
                        // GET /airports - Get all airports
                        .GET("", accept(MediaType.APPLICATION_JSON), airportHandler::getAllAirports)
                        .GET("", accept(MediaType.APPLICATION_XML), airportHandler::getAllAirports)
                        // GET /airports/{icaoCode} - Get airport by ICAO code
                        .GET("/{icaoCode}", accept(MediaType.APPLICATION_JSON), airportHandler::getAirportByIcaoCode)
                        // POST /airports - Create a new airport
                        .POST("", accept(MediaType.APPLICATION_JSON), airportHandler::createAirport)
                        // PUT /airports/{icaoCode} - Update an existing airport
                        .PUT("/{icaoCode}", accept(MediaType.APPLICATION_JSON), airportHandler::updateAirport)
                        // DELETE /airports/{icaoCode} - Delete an airport
                        .DELETE("/{icaoCode}", airportHandler::deleteAirport)
                )
                .build();
    }
}
