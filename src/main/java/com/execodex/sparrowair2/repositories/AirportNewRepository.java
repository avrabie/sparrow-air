package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.skybrary.AirportNew;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AirportNewRepository extends ReactiveCrudRepository<AirportNew, String> {
    // The primary key of AirportNew is icaoCode, which is a String
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types

    @Query("INSERT INTO airports_new (icao_code, iata_code, name, icao_region, icao_territory, location, city, country, elevation, latitude, longitude, kc_code, airport_bs, airport_los, airport_re) " +
           "VALUES (:#{#airport.icaoCode}, :#{#airport.iataCode}, :#{#airport.name}, :#{#airport.icaoRegion}, :#{#airport.icaoTerritory}, :#{#airport.location}, " +
           ":#{#airport.city}, :#{#airport.country}, :#{#airport.elevation}, :#{#airport.latitude}, :#{#airport.longitude}, :#{#airport.KCCode}, " +
           ":#{#airport.airportBS}, :#{#airport.airportLOS}, :#{#airport.airportRE})")
    Mono<Void> insertWithoutReturning(AirportNew airport);

    // Use this method for inserting an AirportNew entity
    // It uses the custom insertWithoutReturning method and then returns the same entity
    default Mono<AirportNew> insert(AirportNew airport) {
        return insertWithoutReturning(airport).thenReturn(airport);
    }

    // Method for pagination
    Flux<AirportNew> findAllBy(Pageable pageable);
    // Custom query to find airports by country
    @Query("SELECT * FROM airports_new WHERE country = :country")
    Flux<AirportNew> findByCountry(String country, Pageable pageable);
}
