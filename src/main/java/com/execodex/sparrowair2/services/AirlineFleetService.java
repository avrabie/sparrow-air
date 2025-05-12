package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.AirlineFleet;
import com.execodex.sparrowair2.repositories.AirlineFleetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AirlineFleetService {

    private static final Logger logger = LoggerFactory.getLogger(AirlineFleetService.class);
    private final AirlineFleetRepository airlineFleetRepository;

    public AirlineFleetService(AirlineFleetRepository airlineFleetRepository) {
        this.airlineFleetRepository = airlineFleetRepository;
    }

    // Get all aircraft in the fleet
    public Flux<AirlineFleet> getAllAirlineFleet() {
        return airlineFleetRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all airline fleet", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all airline fleet", e);
                    return Flux.error(e);
                });
    }

    // Get aircraft by ID
    public Mono<AirlineFleet> getAirlineFleetById(Long id) {
        return airlineFleetRepository.findById(id)
                .doOnError(e -> logger.error("Error retrieving airline fleet with ID: {}", id, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airline fleet with ID: {}", id, e);
                    return Mono.error(e);
                });
    }

    // Get all aircraft for a specific airline
    public Flux<AirlineFleet> getAirlineFleetByAirlineIcao(String airlineIcao) {
        return airlineFleetRepository.findByAirlineIcao(airlineIcao)
                .doOnError(e -> logger.error("Error retrieving airline fleet for airline: {}", airlineIcao, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airline fleet for airline: {}", airlineIcao, e);
                    return Flux.error(e);
                });
    }

    // Get all aircraft of a specific type
    public Flux<AirlineFleet> getAirlineFleetByAircraftTypeIcao(String aircraftTypeIcao) {
        return airlineFleetRepository.findByAircraftTypeIcao(aircraftTypeIcao)
                .doOnError(e -> logger.error("Error retrieving airline fleet for aircraft type: {}", aircraftTypeIcao, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airline fleet for aircraft type: {}", aircraftTypeIcao, e);
                    return Flux.error(e);
                });
    }

    // Create a new aircraft in the fleet
    public Mono<AirlineFleet> createAirlineFleet(AirlineFleet airlineFleet) {
        return airlineFleetRepository.insert(airlineFleet)
                .doOnSuccess(a -> logger.info("Created airline fleet entry with ID: {}", a.getId()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating airline fleet entry", e);
                    } else {
                        logger.error("Error creating airline fleet entry", e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing aircraft in the fleet
    public Mono<AirlineFleet> updateAirlineFleet(Long id, AirlineFleet airlineFleet) {
        airlineFleet.setId(id); // Ensure the ID is set correctly
        return airlineFleetRepository.save(airlineFleet)
                .doOnSuccess(a -> logger.info("Updated airline fleet entry with ID: {}", a.getId()))
                .doOnError(e -> logger.error("Error updating airline fleet entry with ID: {}", id, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete an aircraft from the fleet
    public Mono<Void> deleteAirlineFleet(Long id) {
        return airlineFleetRepository.findById(id)
                .flatMap(airlineFleet -> airlineFleetRepository.delete(airlineFleet)
                        .doOnSuccess(v -> logger.info("Deleted airline fleet entry with ID: {}", id))
                        .doOnError(e -> logger.error("Error deleting airline fleet entry with ID: {}", id, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }

    public Mono<Long> getTotalAircraftCount() {
        return airlineFleetRepository.count()
                .doOnError(e -> logger.error("Error retrieving total aircraft count", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving total aircraft count", e);
                    return Mono.error(e);
                });
    }

    // Get total aircraft count by airline ICAO
    public Mono<Long> getTotalAircraftCountByAirlineIcao(String airlineIcao) {
        return airlineFleetRepository.countByAirlineIcao(airlineIcao)
                .doOnError(e -> logger.error("Error retrieving total aircraft count for airline: {}", airlineIcao, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving total aircraft count for airline: {}", airlineIcao, e);
                    return Mono.error(e);
                });
    }
}