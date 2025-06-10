package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.kaggle.AirlineNew;
import com.execodex.sparrowair2.repositories.AirlineNewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AirlineNewService {

    private static final Logger logger = LoggerFactory.getLogger(AirlineNewService.class);
    private final AirlineNewRepository airlineNewRepository;

    public AirlineNewService(AirlineNewRepository airlineNewRepository) {
        this.airlineNewRepository = airlineNewRepository;
    }

    // Get all airlines
    public Flux<AirlineNew> getAllAirlines() {
        return airlineNewRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all airlines", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all airlines", e);
                    return Flux.error(e);
                });
    }

    // Get airlines with pagination
    public Flux<AirlineNew> getAllAirlines(Integer page, Integer size) {
        if (page == null || size == null) {
            return getAllAirlines();
        }

        Pageable pageable = PageRequest.of(page, size);
        return airlineNewRepository.findAllBy(pageable)
                .doOnError(e -> logger.error("Error retrieving airlines with pagination", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airlines with pagination", e);
                    return Flux.error(e);
                });
    }

    // Get airlines by country with pagination
    public Flux<AirlineNew> getAirlinesByCountry(String country, Integer page, Integer size) {
        Pageable pageable = (page == null || size == null)
                ? Pageable.unpaged()
                : PageRequest.of(page, size);

        return airlineNewRepository.findByCountry(country, pageable)
                .doOnError(e -> logger.error("Error retrieving airlines by country: {} with pagination", country, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airlines by country: {} with pagination", country, e);
                    return Flux.error(e);
                });
    }

    // Get airlines by active status with pagination
    public Flux<AirlineNew> getAirlinesByActive(String active, Integer page, Integer size) {
        Pageable pageable = (page == null || size == null)
                ? Pageable.unpaged()
                : PageRequest.of(page, size);

        return airlineNewRepository.findByActive(active, pageable)
                .doOnError(e -> logger.error("Error retrieving airlines by active status: {} with pagination", active, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airlines by active status: {} with pagination", active, e);
                    return Flux.error(e);
                });
    }

    // Get airline by ID
    public Mono<AirlineNew> getAirlineById(Integer airlineId) {
        return airlineNewRepository.findById(airlineId)
                .doOnError(e -> logger.error("Error retrieving airline with ID: {}", airlineId, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airline with ID: {}", airlineId, e);
                    return Mono.error(e);
                });
    }

    // Create a new airline
    public Mono<AirlineNew> createAirline(AirlineNew airline) {
        return airlineNewRepository.insert(airline)
                .doOnSuccess(a -> logger.info("Created airline with ID: {}", a.getAirlineId()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating airline with ID: {}", airline.getAirlineId(), e);
                    } else {
                        logger.error("Error creating airline with ID: {}", airline.getAirlineId(), e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing airline
    public Mono<AirlineNew> updateAirline(Integer airlineId, AirlineNew airline) {
        airline.setAirlineId(airlineId); // Ensure the ID is set correctly
        return airlineNewRepository.save(airline)
                .doOnSuccess(a -> logger.info("Updated airline with ID: {}", a.getAirlineId()))
                .doOnError(e -> logger.error("Error updating airline with ID: {}", airlineId, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete an airline
    public Mono<Void> deleteAirline(Integer airlineId) {
        return airlineNewRepository.findById(airlineId)
                .flatMap(airline -> airlineNewRepository.delete(airline)
                        .doOnSuccess(v -> logger.info("Deleted airline with ID: {}", airlineId))
                        .doOnError(e -> logger.error("Error deleting airline with ID: {}", airlineId, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }

    public Mono<AirlineNew> getAirlineByIcaoCode(String icaoCode) {
        return airlineNewRepository.findByIcaoCode(icaoCode)
                .doOnError(e -> logger.error("Error retrieving airline with ICAO code: {}", icaoCode, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airline with ICAO code: {}", icaoCode, e);
                    return Mono.error(e);
                });
    }

    public Mono<AirlineNew> getAirlineByIataCode(String iataCode) {
        return airlineNewRepository.findByIataCode(iataCode)
                .doOnError(e -> logger.error("Error retrieving airline with IATA code: {}", iataCode, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving airline with IATA code: {}", iataCode, e);
                    return Mono.error(e);
                });
    }
}
