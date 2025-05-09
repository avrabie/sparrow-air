package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.AircraftTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AircraftTypeRoutes {

    private final AircraftTypeHandler aircraftTypeHandler;

    public AircraftTypeRoutes(AircraftTypeHandler aircraftTypeHandler) {
        this.aircraftTypeHandler = aircraftTypeHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> aircraftTypeRoutesFunction() {
        return RouterFunctions.route()
                .path("/aircraft-types", builder -> builder
                        // GET /aircraft-types - Get all aircraft types
                        .GET("", accept(MediaType.APPLICATION_JSON), aircraftTypeHandler::getAllAircraftTypes)
                        .GET("", accept(MediaType.APPLICATION_XML), aircraftTypeHandler::getAllAircraftTypes)
                        // GET /aircraft-types/{icaoCode} - Get aircraft type by ICAO code
                        .GET("/{icaoCode}", accept(MediaType.APPLICATION_JSON), aircraftTypeHandler::getAircraftTypeByIcaoCode)
                        // POST /aircraft-types - Create a new aircraft type
                        .POST("", accept(MediaType.APPLICATION_JSON), aircraftTypeHandler::createAircraftType)
                        // PUT /aircraft-types/{icaoCode} - Update an existing aircraft type
                        .PUT("/{icaoCode}", accept(MediaType.APPLICATION_JSON), aircraftTypeHandler::updateAircraftType)
                        // DELETE /aircraft-types/{icaoCode} - Delete an aircraft type
                        .DELETE("/{icaoCode}", aircraftTypeHandler::deleteAircraftType)
                )
                .build();
    }
}