package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.AircraftType;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AircraftTypeRepository extends ReactiveCrudRepository<AircraftType, String> {
    // The primary key of AircraftType is icaoCode, which is a String
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types

    @Query("INSERT INTO aircraft_types (icao_code, model_name, manufacturer, max_range_km, mtow) " +
            "VALUES (:#{#aircraftType.icaoCode}, :#{#aircraftType.modelName}, :#{#aircraftType.manufacturer}, " +
            ":#{#aircraftType.maxRangeKm}, :#{#aircraftType.mtow}) " +
            "RETURNING *")
    Mono<AircraftType> insert(AircraftType aircraftType);

    @Query("SELECT * FROM aircraft_types WHERE LOWER(model_name) LIKE CONCAT('%', LOWER(:searchQuery), '%')")
    Flux<AircraftType> findByModelNameContainingIgnoreCase(String searchQuery);
}
