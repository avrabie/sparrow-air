package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.Country;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CountryRepository extends ReactiveCrudRepository<Country, String> {
    // The primary key of Country is code, which is a String
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types

    @Query("INSERT INTO countries (code, name, capital, continent, currency, language) " +
            "VALUES (:#{#country.code}, :#{#country.name}, :#{#country.capital}, " +
            ":#{#country.continent}, :#{#country.currency}, :#{#country.language}) RETURNING *")
    Mono<Country> insert(Country country);

    // Find countries by name similarity
    @Query("SELECT * FROM countries WHERE LOWER(name) LIKE CONCAT('%', LOWER(:name), '%')")
    Flux<Country> findByNameContainingIgnoreCase(String name);
}
