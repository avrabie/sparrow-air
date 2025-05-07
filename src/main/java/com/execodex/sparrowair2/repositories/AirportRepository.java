package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.Airport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AirportRepository extends ReactiveCrudRepository<Airport, String> {
    // The primary key of Airport is icaoCode, which is a String
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types


        @Query("INSERT INTO airports (icao_code, name, city, country, timezone, latitude, longitude) " +
                "VALUES (:#{#airport.icaoCode}, :#{#airport.name}, :#{#airport.city}, :#{#airport.country}, " +
                ":#{#airport.timezone}, :#{#airport.latitude}, :#{#airport.longitude}) RETURNING *")
        Mono<Airport> insert(Airport airport);

}