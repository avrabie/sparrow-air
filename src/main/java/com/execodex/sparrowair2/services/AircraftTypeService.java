package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.AircraftType;
import com.execodex.sparrowair2.repositories.AircraftTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AircraftTypeService {

    private static final Logger logger = LoggerFactory.getLogger(AircraftTypeService.class);
    private final AircraftTypeRepository aircraftTypeRepository;

    public AircraftTypeService(AircraftTypeRepository aircraftTypeRepository) {
        this.aircraftTypeRepository = aircraftTypeRepository;
    }

    // Get all aircraft types
    public Flux<AircraftType> getAllAircraftTypes() {
        return aircraftTypeRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all aircraft types", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all aircraft types", e);
                    return Flux.error(e);
                });
    }

    // Get aircraft type by ICAO code
    public Mono<AircraftType> getAircraftTypeByIcaoCode(String icaoCode) {
        return aircraftTypeRepository.findById(icaoCode)
                .doOnError(e -> logger.error("Error retrieving aircraft type with ICAO code: {}", icaoCode, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving aircraft type with ICAO code: {}", icaoCode, e);
                    return Mono.error(e);
                });
    }

    // Create a new aircraft type
    public Mono<AircraftType> createAircraftType(AircraftType aircraftType) {
        return aircraftTypeRepository.insert(aircraftType)
                .doOnSuccess(a -> logger.info("Created aircraft type with ICAO code: {}", a.getIcaoCode()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating aircraft type with ICAO code: {}", aircraftType.getIcaoCode(), e);
                    } else {
                        logger.error("Error creating aircraft type with ICAO code: {}", aircraftType.getIcaoCode(), e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing aircraft type
    public Mono<AircraftType> updateAircraftType(String icaoCode, AircraftType aircraftType) {
        aircraftType.setIcaoCode(icaoCode); // Ensure the ID is set correctly
        return aircraftTypeRepository.save(aircraftType)
                .doOnSuccess(a -> logger.info("Updated aircraft type with ICAO code: {}", a.getIcaoCode()))
                .doOnError(e -> logger.error("Error updating aircraft type with ICAO code: {}", icaoCode, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete an aircraft type
    public Mono<Void> deleteAircraftType(String icaoCode) {
        return aircraftTypeRepository.findById(icaoCode)
                .flatMap(aircraftType -> aircraftTypeRepository.delete(aircraftType)
                        .doOnSuccess(v -> logger.info("Deleted aircraft type with ICAO code: {}", icaoCode))
                        .doOnError(e -> logger.error("Error deleting aircraft type with ICAO code: {}", icaoCode, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }
}