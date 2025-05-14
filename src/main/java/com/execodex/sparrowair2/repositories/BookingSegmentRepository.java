package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.BookingSegment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BookingSegmentRepository extends ReactiveCrudRepository<BookingSegment, Long> {
    Mono<BookingSegment> findByFlightIdAndSeatId(Long flightId, Long seatId);
    // Custom query methods can be defined here if needed
}
