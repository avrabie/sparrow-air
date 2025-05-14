package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.Flight;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FlightRepository extends ReactiveCrudRepository<Flight, Long> {
    // The primary key of Flight is id, which is a Long
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types

    @Query("INSERT INTO flights (airline_icao_code, flight_number, departure_airport_icao, arrival_airport_icao, " +
            "scheduled_departure, scheduled_arrival, airline_fleet_id, status) " +
            "VALUES (:#{#flight.airlineIcaoCode}, :#{#flight.flightNumber}, :#{#flight.departureAirportIcao}, " +
            ":#{#flight.arrivalAirportIcao}, :#{#flight.scheduledDeparture}, :#{#flight.scheduledArrival}, " +
            ":#{#flight.airlineFleetId}, :#{#flight.status}) " +
            "RETURNING *")
    Mono<Flight> insert(Flight flight);

    Flux<Flight> findByFlightNumber(String flightNumber);
    Mono<Flight> findByAirlineIcaoCodeAndFlightNumber(String airlineIcaoCode, String flightNumber);

    Flux<Flight> findByAirlineIcaoCode(String airlineIcaoCode);
}
