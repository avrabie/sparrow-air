package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.PassengerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class PassengerRoutes {

    private final PassengerHandler passengerHandler;

    public PassengerRoutes(PassengerHandler passengerHandler) {
        this.passengerHandler = passengerHandler;
    }

    @Bean
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
