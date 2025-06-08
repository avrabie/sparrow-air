package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.AirportNew;
import com.execodex.sparrowair2.repositories.AirportNewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AirportNewService {

    private static final Logger logger = LoggerFactory.getLogger(AirportNewService.class);
    private final AirportNewRepository airportNewRepository;

    public AirportNewService(AirportNewRepository airportNewRepository) {
        this.airportNewRepository = airportNewRepository;
    }

    // Get all airports
    public Flux<AirportNew> getAllAirports() {
        return airportNewRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all airports", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all airports", e);
                    return Flux.error(e);
                });
    }

    // Get airport by ICAO code
    public Mono<AirportNew> getAirportByIcaoCode(String icaoCode) {
        return airportNewRepository.findById(icaoCode)
                .doOnError(e -> logger.error("Error retrieving airport with ICAO code: {}", icaoCode, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airport with ICAO code: {}", icaoCode, e);
                    return Mono.error(e);
                });
    }

    // Create a new airport
    public Mono<AirportNew> createAirport(AirportNew airport) {
        return airportNewRepository.insert(airport)
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
    public Mono<AirportNew> updateAirport(String icaoCode, AirportNew airport) {
        airport.setIcaoCode(icaoCode); // Ensure the ID is set correctly
        return airportNewRepository.save(airport)
                .doOnSuccess(a -> logger.info("Updated airport with ICAO code: {}", a.getIcaoCode()))
                .doOnError(e -> logger.error("Error updating airport with ICAO code: {}", icaoCode, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete an airport
    public Mono<Void> deleteAirport(String icaoCode) {
        return airportNewRepository.findById(icaoCode)
                .flatMap(airport -> airportNewRepository.delete(airport)
                        .doOnSuccess(v -> logger.info("Deleted airport with ICAO code: {}", icaoCode))
                        .doOnError(e -> logger.error("Error deleting airport with ICAO code: {}", icaoCode, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }

    // Calculates the distance between two airports using Haversine formula, in kilometers
    public double distance(AirportNew airport1, AirportNew airport2) {
        double lat1 = airport1.getLatitude();
        double lon1 = airport1.getLongitude();
        double lat2 = airport2.getLatitude();
        double lon2 = airport2.getLongitude();

        // Haversine formula to calculate the distance between two points on the Earth
        final int R = 6371; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in kilometers
    }
}