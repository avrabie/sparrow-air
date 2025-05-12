package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.AirlineFleet;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AirlineFleetRepository extends ReactiveCrudRepository<AirlineFleet, Long> {
    // The primary key of AirlineFleet is id, which is a Long
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types

    @Query("INSERT INTO airline_fleet (aircraft_type_icao, airline_icao, aircraft_age, seat_configuration, " +
            "has_wifi, has_power_outlets, has_entertainment_system, first_class_seats, business_seats, economy_seats, premium_economy_seats) " +
            "VALUES (:#{#airlineFleet.aircraftTypeIcao}, :#{#airlineFleet.airlineIcao}, :#{#airlineFleet.aircraftAge}, " +
            ":#{#airlineFleet.seatConfiguration}, :#{#airlineFleet.hasWifi}, :#{#airlineFleet.hasPowerOutlets}, " +
            ":#{#airlineFleet.hasEntertainmentSystem}, :#{#airlineFleet.firstClassSeats}, :#{#airlineFleet.businessSeats}, " +
            ":#{#airlineFleet.economySeats}, :#{#airlineFleet.premiumEconomySeats}) " +
            "RETURNING *")
    Mono<AirlineFleet> insert(AirlineFleet airlineFleet);

    // Find all aircraft in a specific airline's fleet
    Flux<AirlineFleet> findByAirlineIcao(String airlineIcao);

    // Find all aircraft of  a specific type across all airlines
    Flux<AirlineFleet> findByAircraftTypeIcao(String aircraftTypeIcao);

    Mono<Long> countByAirlineIcao(String airlineIcao);
}
