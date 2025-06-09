package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Country;
import com.execodex.sparrowair2.repositories.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    // Get all countries
    public Flux<Country> getAllCountries() {
        return countryRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all countries", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all countries", e);
                    return Flux.error(e);
                });
    }

    // Get countries by name similarity
    public Flux<Country> getCountriesByNameSimilarity(String name) {
        return countryRepository.findByNameContainingIgnoreCase(name)
                .doOnError(e -> logger.error("Error retrieving countries with name similar to: {}", name, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving countries with name similar to: {}", name, e);
                    return Flux.error(e);
                });
    }

    // Get country by code
    public Mono<Country> getCountryByCode(String code) {
        return countryRepository.findById(code)
                .doOnError(e -> logger.error("Error retrieving country with code: {}", code, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving country with code: {}", code, e);
                    return Mono.error(e);
                });
    }

    // Create a new country
    public Mono<Country> createCountry(Country country) {
        return countryRepository.insert(country)
                .doOnSuccess(c -> logger.info("Created country with code: {}", c.getCode()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating country with code: {}", country.getCode(), e);
                    } else {
                        logger.error("Error creating country with code: {}", country.getCode(), e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing country
    public Mono<Country> updateCountry(String code, Country country) {
        country.setCode(code); // Ensure the ID is set correctly
        return countryRepository.save(country)
                .doOnSuccess(c -> logger.info("Updated country with code: {}", c.getCode()))
                .doOnError(e -> logger.error("Error updating country with code: {}", code, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete a country
    public Mono<Void> deleteCountry(String code) {
        return countryRepository.findById(code)
                .flatMap(country -> countryRepository.delete(country)
                        .doOnSuccess(v -> logger.info("Deleted country with code: {}", code))
                        .doOnError(e -> logger.error("Error deleting country with code: {}", code, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }
}
