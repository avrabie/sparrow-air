package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.Seat;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SeatRepository extends ReactiveCrudRepository<Seat, Long> {
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types

    // Find seats by flight ID
    Flux<Seat> findByFlightId(Long flightId);

    // Delete seats by flight ID
    @Modifying
    @Query("DELETE FROM seats WHERE flight_id = :flightId")
    Mono<Void> deleteByFlightId(Long flightId);
}
