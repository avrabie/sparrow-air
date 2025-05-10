package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Airline;
import com.execodex.sparrowair2.repositories.AirlineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AirlineService {

    private static final Logger logger = LoggerFactory.getLogger(AirlineService.class);
    private final AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    // Get all airlines
    public Flux<Airline> getAllAirlines() {
        return airlineRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all airlines", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all airlines", e);
                    return Flux.error(e);
                });
    }

    // Get airline by ICAO code
    public Mono<Airline> getAirlineByIcaoCode(String icaoCode) {
        return airlineRepository.findById(icaoCode)
                .doOnError(e -> logger.error("Error retrieving airline with ICAO code: {}", icaoCode, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airline with ICAO code: {}", icaoCode, e);
                    return Mono.error(e);
                });
    }

    // Create a new airline
    public Mono<Airline> createAirline(Airline airline) {
        return airlineRepository.insert(airline)
                .doOnSuccess(a -> logger.info("Created airline with ICAO code: {}", a.getIcaoCode()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating airline with ICAO code: {}", airline.getIcaoCode(), e);
                    } else {
                        logger.error("Error creating airline with ICAO code: {}", airline.getIcaoCode(), e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing airline
    public Mono<Airline> updateAirline(String icaoCode, Airline airline) {
        airline.setIcaoCode(icaoCode); // Ensure the ID is set correctly
        return airlineRepository.save(airline)
                .doOnSuccess(a -> logger.info("Updated airline with ICAO code: {}", a.getIcaoCode()))
                .doOnError(e -> logger.error("Error updating airline with ICAO code: {}", icaoCode, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete an airline
    public Mono<Void> deleteAirline(String icaoCode) {
        return airlineRepository.findById(icaoCode)
                .flatMap(airline -> airlineRepository.delete(airline)
                        .doOnSuccess(v -> logger.info("Deleted airline with ICAO code: {}", icaoCode))
                        .doOnError(e -> logger.error("Error deleting airline with ICAO code: {}", icaoCode, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }
}