package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.Booking;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingRepository extends ReactiveCrudRepository<Booking, Long> {

    // insert a booking
    @Query("INSERT INTO bookings (id, flight_id, passenger_id, seat_id, status) " +
            "VALUES (:#{#booking.id}, :#{#booking.flightId}, :#{#booking.passengerId}, :#{#booking.seatId}, :#{#booking.status}) RETURNING *")
    Mono<Booking> insert(Booking booking);
    // FIND BY flight id
    @Query("SELECT * FROM bookings WHERE flight_id = :flightId RETURNING *")
    Flux<Booking> findByFlightId(Long flightId);
}
