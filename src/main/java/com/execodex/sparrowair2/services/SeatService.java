package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Seat;
import com.execodex.sparrowair2.entities.SeatClass;
import com.execodex.sparrowair2.entities.SeatStatus;
import com.execodex.sparrowair2.repositories.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SeatService {

    private static final Logger logger = LoggerFactory.getLogger(SeatService.class);
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    // Get all seats
    public Flux<Seat> getAllSeats() {
        return seatRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all seats", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all seats", e);
                    return Flux.error(e);
                });
    }

    // Get seat by ID
    public Mono<Seat> getSeatById(Long id) {
        return seatRepository.findById(id)
                .doOnError(e -> logger.error("Error retrieving seat with ID: {}", id, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving seat with ID: {}", id, e);
                    return Mono.error(e);
                });
    }

    // Get seats by flight ID
    public Flux<Seat> getSeatsByFlightId(Long flightId) {
        return seatRepository.findByFlightId(flightId)
                .doOnError(e -> logger.error("Error retrieving seats for flight ID: {}", flightId, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving seats for flight ID: {}", flightId, e);
                    return Flux.error(e);
                });
    }

    // Create a new seat
    public Mono<Seat> createSeat(Seat seat) {
        return seatRepository.save(seat)
                .doOnSuccess(s -> logger.info("Created seat with ID: {}", s.getId()))
                .doOnError(e -> logger.error("Error creating seat", e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Create multiple seats for a flight
    public Flux<Seat> createSeatsForFlight(Long flightId, int firstClassCount, int businessCount, int premiumEconomyCount, int economyCount) {
        logger.info("Creating seats for flight ID: {}", flightId);

        // Create first class seats
        Flux<Seat> firstClassSeats = createSeatsOfClass(flightId, firstClassCount, SeatClass.FIRST_CLASS);

        // Create business class seats
        Flux<Seat> businessSeats = createSeatsOfClass(flightId, businessCount, SeatClass.BUSINESS);

        // Create premium economy seats
        Flux<Seat> premiumEconomySeats = createSeatsOfClass(flightId, premiumEconomyCount, SeatClass.PREMIUM_ECONOMY);

        // Create economy seats
        Flux<Seat> economySeats = createSeatsOfClass(flightId, economyCount, SeatClass.ECONOMY);

        // Combine all seat creation operations
        return Flux.concat(firstClassSeats, businessSeats, premiumEconomySeats, economySeats);
    }

    // Helper method to create seats of a specific class
    private Flux<Seat> createSeatsOfClass(Long flightId, int count, SeatClass seatClass) {
        if (count <= 0) {
            return Flux.empty();
        }

        return Flux.range(1, count)
                .flatMap(i -> {
                    String seatNumber = generateSeatNumber(seatClass, i);
                    Seat seat = Seat.builder()
                            .flightId(flightId)
                            .seatNumber(seatNumber)
                            .seatClass(seatClass)
                            .status(SeatStatus.AVAILABLE)
                            .build();
                    return createSeat(seat);
                });
    }

    // Helper method to generate seat numbers based on class and index
    private String generateSeatNumber(SeatClass seatClass, int index) {
        // Simple implementation - can be enhanced based on specific requirements
        String prefix;
        switch (seatClass) {
            case FIRST_CLASS:
                prefix = "F";
                break;
            case BUSINESS:
                prefix = "B";
                break;
            case PREMIUM_ECONOMY:
                prefix = "P";
                break;
            case ECONOMY:
            default:
                prefix = "E";
                break;
        }
        return prefix + index;
    }

    // Delete seats by flight ID
    public Mono<Void> deleteByFlightId(Long flightId) {
        logger.info("Deleting seats for flight ID: {}", flightId);
        return seatRepository.deleteByFlightId(flightId)
                .doOnSuccess(v -> logger.info("Deleted seats for flight ID: {}", flightId))
                .doOnError(e -> logger.error("Error deleting seats for flight ID: {}", flightId, e))
                .onErrorResume(e -> Mono.error(e));
    }
}
