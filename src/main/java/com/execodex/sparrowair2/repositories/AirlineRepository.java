package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.Airline;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AirlineRepository extends ReactiveCrudRepository<Airline, String> {
    // The primary key of Airline is icaoCode, which is a String
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types

    @Query("INSERT INTO airlines (icao_code, name, headquarters, contact_number, website) " +
            "VALUES (:#{#airline.icaoCode}, :#{#airline.name}, :#{#airline.headquarters}, " +
            ":#{#airline.contactNumber}, :#{#airline.website}) RETURNING *")
    Mono<Airline> insert(Airline airline);
}