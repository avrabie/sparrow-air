package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Airport;
import com.execodex.sparrowair2.repositories.AirportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AirportService {

    private static final Logger logger = LoggerFactory.getLogger(AirportService.class);
    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    // Get all airports
    public Flux<Airport> getAllAirports() {
        return airportRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all airports", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all airports", e);
                    return Flux.error(e);
                });
    }

    // Get airport by ICAO code
    public Mono<Airport> getAirportByIcaoCode(String icaoCode) {
        return airportRepository.findById(icaoCode)
                .doOnError(e -> logger.error("Error retrieving airport with ICAO code: {}", icaoCode, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airport with ICAO code: {}", icaoCode, e);
                    return Mono.error(e);
                });
    }

    // Create a new airport
    public Mono<Airport> createAirport(Airport airport) {
        return airportRepository.insert(airport)
                .doOnSuccess(a -> logger.info("Created airport with ICAO code: {}", a.getIcaoCode()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating airport with ICAO code: {}", airport.getIcaoCode(), e);
                    } else {
                        logger.error("Error creating airport with ICAO code: {}", airport.getIcaoCode(), e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing airport
    public Mono<Airport> updateAirport(String icaoCode, Airport airport) {
        airport.setIcaoCode(icaoCode); // Ensure the ID is set correctly
        return airportRepository.save(airport)
                .doOnSuccess(a -> logger.info("Updated airport with ICAO code: {}", a.getIcaoCode()))
                .doOnError(e -> logger.error("Error updating airport with ICAO code: {}", icaoCode, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete an airport
    public Mono<Void> deleteAirport(String icaoCode) {
        return airportRepository.findById(icaoCode)
                .flatMap(airport -> airportRepository.delete(airport)
                        .doOnSuccess(v -> logger.info("Deleted airport with ICAO code: {}", icaoCode))
                        .doOnError(e -> logger.error("Error deleting airport with ICAO code: {}", icaoCode, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }
}
