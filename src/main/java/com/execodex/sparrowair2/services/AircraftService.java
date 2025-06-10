package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.skybrary.Aircraft;
import com.execodex.sparrowair2.repositories.AircraftRepository;
import com.execodex.sparrowair2.services.utilities.ParseAircraftSkyBrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AircraftService {

    private static final Logger logger = LoggerFactory.getLogger(AircraftService.class);
    private final AircraftRepository aircraftRepository;
    private final ParseAircraftSkyBrary parseAircraftSkyBrary;

    public AircraftService(AircraftRepository aircraftRepository, ParseAircraftSkyBrary parseAircraftSkyBrary) {
        this.aircraftRepository = aircraftRepository;
        this.parseAircraftSkyBrary = parseAircraftSkyBrary;
    }

    // Get all aircraft
    public Flux<Aircraft> getAllAircraft() {
        return aircraftRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all aircraft", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all aircraft", e);
                    return Flux.error(e);
                });
    }

    // Get aircraft by ICAO code
    public Mono<Aircraft> getAircraftByIcaoCode(String icaoCode) {
        return aircraftRepository.findById(icaoCode)
                .doOnError(e -> logger.error("Error retrieving aircraft with ICAO code: {}", icaoCode, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving aircraft with ICAO code: {}", icaoCode, e);
                    return Mono.error(e);
                });
    }

    // Create a new aircraft
    public Mono<Aircraft> createAircraft(Aircraft aircraft) {
        return aircraftRepository.insert(aircraft)
                .doOnSuccess(a -> logger.info("Created aircraft with ICAO code: {}", a.getIcaoCode()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating aircraft with ICAO code: {}", aircraft.getIcaoCode(), e);
                    } else {
                        logger.error("Error creating aircraft with ICAO code: {}", aircraft.getIcaoCode(), e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing aircraft
    public Mono<Aircraft> updateAircraft(String icaoCode, Aircraft aircraft) {
        aircraft.setIcaoCode(icaoCode); // Ensure the ID is set correctly
        return aircraftRepository.save(aircraft)
                .doOnSuccess(a -> logger.info("Updated aircraft with ICAO code: {}", a.getIcaoCode()))
                .doOnError(e -> logger.error("Error updating aircraft with ICAO code: {}", icaoCode, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete an aircraft
    public Mono<Void> deleteAircraft(String icaoCode) {
        return aircraftRepository.findById(icaoCode)
                .flatMap(aircraft -> aircraftRepository.delete(aircraft)
                        .doOnSuccess(v -> logger.info("Deleted aircraft with ICAO code: {}", icaoCode))
                        .doOnError(e -> logger.error("Error deleting aircraft with ICAO code: {}", icaoCode, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }

    // Parse online aircraft from Skybrary
    public Mono<Aircraft> parseOnlineAircraft(String aircraftIcaoCode) {
        return parseAircraftSkyBrary.parseOnlineAircraft(aircraftIcaoCode)
                .doOnSuccess(a -> logger.info("Parsed online aircraft with ICAO code: {}", aircraftIcaoCode))
                .doOnError(e -> logger.error("Error parsing online aircraft with ICAO code: {}", aircraftIcaoCode, e))
                .onErrorResume(e -> Mono.error(e));
    }
}