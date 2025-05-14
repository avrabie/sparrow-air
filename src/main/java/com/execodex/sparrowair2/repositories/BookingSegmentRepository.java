package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.BookingSegment;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BookingSegmentRepository extends ReactiveCrudRepository<BookingSegment, Long> {
    @Query("SELECT * FROM booking_segments WHERE flight_id = :flightId AND seat_id = :seatId")
    Mono<BookingSegment> findByFlightIdAndSeatId(Long flightId, Long seatId);

    @Query("INSERT INTO booking_segments (booking_id, flight_id, seat_id, fare_class, ticket_number) " +
            "VALUES (:#{#bookingSegment.bookingId}, :#{#bookingSegment.flightId}, :#{#bookingSegment.seatId}, " +
            ":#{#bookingSegment.fareClass}, :#{#bookingSegment.ticketNumber}) RETURNING *")
    Mono<BookingSegment> insert(BookingSegment bookingSegment);
    // Custom query methods can be defined here if needed
}
