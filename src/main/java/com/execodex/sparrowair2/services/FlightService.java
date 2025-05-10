package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Flight;
import com.execodex.sparrowair2.repositories.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    // Get all flights
    public Flux<Flight> getAllFlights() {
        return flightRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all flights", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all flights", e);
                    return Flux.error(e);
                });
    }

    // Get flight by ID
    public Mono<Flight> getFlightById(Long id) {
        return flightRepository.findById(id)
                .doOnError(e -> logger.error("Error retrieving flight with ID: {}", id, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving flight with ID: {}", id, e);
                    return Mono.error(e);
                });
    }

    // Create a new flight
    public Mono<Flight> createFlight(Flight flight) {
        return flightRepository.insert(flight)
                .doOnSuccess(f -> logger.info("Created flight with ID: {}", f.getId()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating flight", e);
                    } else {
                        logger.error("Error creating flight", e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing flight
    public Mono<Flight> updateFlight(Long id, Flight flight) {
        flight.setId(id); // Ensure the ID is set correctly
        return flightRepository.save(flight)
                .doOnSuccess(f -> logger.info("Updated flight with ID: {}", f.getId()))
                .doOnError(e -> logger.error("Error updating flight with ID: {}", id, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete a flight
    public Mono<Void> deleteFlight(Long id) {
        return flightRepository.findById(id)
                .flatMap(flight -> flightRepository.delete(flight)
                        .doOnSuccess(v -> logger.info("Deleted flight with ID: {}", id))
                        .doOnError(e -> logger.error("Error deleting flight with ID: {}", id, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }
}