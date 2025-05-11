package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.AirlineFleetHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AirlineFleetRoutes {

    private final AirlineFleetHandler airlineFleetHandler;

    public AirlineFleetRoutes(AirlineFleetHandler airlineFleetHandler) {
        this.airlineFleetHandler = airlineFleetHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> airlineFleetRoutesFunction() {
        return RouterFunctions.route()
                .path("/airline-fleet", builder -> builder
                        // GET /airline-fleet - Get all aircraft in the fleet
                        .GET("", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::getAllAirlineFleet)
                        // GET /airline-fleet/{id} - Get aircraft by ID
                        .GET("/{id}", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::getAirlineFleetById)
                        // GET /airline-fleet/airline/{airlineIcao} - Get all aircraft for a specific airline
                        .GET("/airline/{airlineIcao}", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::getAirlineFleetByAirlineIcao)
                        // GET /airline-fleet/aircraft-type/{aircraftTypeIcao} - Get all aircraft of a specific type
                        .GET("/aircraft-type/{aircraftTypeIcao}", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::getAirlineFleetByAircraftTypeIcao)
                        // POST /airline-fleet - Create a new aircraft in the fleet
                        .POST("", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::createAirlineFleet)
                        // PUT /airline-fleet/{id} - Update an existing aircraft in the fleet
                        .PUT("/{id}", accept(MediaType.APPLICATION_JSON), airlineFleetHandler::updateAirlineFleet)
                        // DELETE /airline-fleet/{id} - Delete an aircraft from the fleet
                        .DELETE("/{id}", airlineFleetHandler::deleteAirlineFleet)
                )
                .build();
    }
}