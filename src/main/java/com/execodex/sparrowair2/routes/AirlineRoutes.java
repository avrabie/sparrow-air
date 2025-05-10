package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.AirlineHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AirlineRoutes {

    private final AirlineHandler airlineHandler;

    public AirlineRoutes(AirlineHandler airlineHandler) {
        this.airlineHandler = airlineHandler;
    }

    @Bean
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