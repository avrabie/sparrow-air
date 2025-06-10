package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.kaggle.AirlineNew;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AirlineNewRepository extends ReactiveCrudRepository<AirlineNew, Integer> {
    // The primary key of AirlineNew is airlineId, which is an Integer
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types

    @Query("INSERT INTO airlines_new (airline_id, name, alias, iata, icao_code, callsign, country, active) " +
           "VALUES (:#{#airline.airlineId}, :#{#airline.name}, :#{#airline.alias}, :#{#airline.iata}, :#{#airline.icaoCode}, :#{#airline.callsign}, " +
           ":#{#airline.country}, :#{#airline.active})")
    Mono<Void> insertWithoutReturning(AirlineNew airline);

    // Use this method for inserting an AirlineNew entity
    // It uses the custom insertWithoutReturning method and then returns the same entity
    default Mono<AirlineNew> insert(AirlineNew airline) {
        return insertWithoutReturning(airline).thenReturn(airline);
    }

    // Method for pagination
    Flux<AirlineNew> findAllBy(Pageable pageable);

    // Custom query to find airlines by country
    @Query("SELECT * FROM airlines_new WHERE country = :country")
    Flux<AirlineNew> findByCountry(String country, Pageable pageable);

    // Custom query to find airlines by active status
    @Query("SELECT * FROM airlines_new WHERE active = :active")
    Flux<AirlineNew> findByActive(String active, Pageable pageable);

    // Custom query to find airline by ICAO code
    @Query("SELECT * FROM airlines_new WHERE icao_code = :icaoCode")
    Mono<AirlineNew> findByIcaoCode(String icaoCode);

    // Custom query to find airline by IATA code
    @Query("SELECT * FROM airlines_new WHERE iata = :iataCode")
    Mono<AirlineNew> findByIataCode(String iataCode);
}
