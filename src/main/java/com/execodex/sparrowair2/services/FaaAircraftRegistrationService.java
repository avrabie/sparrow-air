package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.caa.FaaAircraftRegistration;
import com.execodex.sparrowair2.repositories.FaaAircraftRegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FaaAircraftRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(FaaAircraftRegistrationService.class);
    private final FaaAircraftRegistrationRepository faaAircraftRegistrationRepository;

    public FaaAircraftRegistrationService(FaaAircraftRegistrationRepository faaAircraftRegistrationRepository) {
        this.faaAircraftRegistrationRepository = faaAircraftRegistrationRepository;
    }

    // Get all FAA aircraft registrations
    public Flux<FaaAircraftRegistration> getAllFaaAircraftRegistrations() {
        return faaAircraftRegistrationRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all FAA aircraft registrations", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all FAA aircraft registrations", e);
                    return Flux.error(e);
                });
    }

    // Get FAA aircraft registration by N-Number
    public Mono<FaaAircraftRegistration> getFaaAircraftRegistrationByNNumber(String nNumber) {
        return faaAircraftRegistrationRepository.findById(nNumber)
                .doOnError(e -> logger.error("Error retrieving FAA aircraft registration with N-Number: {}", nNumber, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving FAA aircraft registration with N-Number: {}", nNumber, e);
                    return Mono.error(e);
                });
    }

    // Get FAA aircraft registrations by engine manufacturer model code
    public Flux<FaaAircraftRegistration> getFaaAircraftRegistrationsByEngineMfrModelCode(String engineMfrModelCode) {
        return faaAircraftRegistrationRepository.findByEngineMfrModelCode(engineMfrModelCode)
                .doOnError(e -> logger.error("Error retrieving FAA aircraft registrations with engine manufacturer model code: {}", engineMfrModelCode, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving FAA aircraft registrations with engine manufacturer model code: {}", engineMfrModelCode, e);
                    return Flux.error(e);
                });
    }

    // Get FAA aircraft registrations by type aircraft
    public Flux<FaaAircraftRegistration> getFaaAircraftRegistrationsByTypeAircraft(String typeAircraft) {
        return faaAircraftRegistrationRepository.findByTypeAircraft(typeAircraft)
                .doOnError(e -> logger.error("Error retrieving FAA aircraft registrations with type aircraft: {}", typeAircraft, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving FAA aircraft registrations with type aircraft: {}", typeAircraft, e);
                    return Flux.error(e);
                });
    }

    // Get FAA aircraft registrations by aircraft manufacturer model code
    public Flux<FaaAircraftRegistration> getFaaAircraftRegistrationsByAircraftMfrModelCode(String aircraftMfrModelCode) {
        return faaAircraftRegistrationRepository.findByAircraftMfrModelCode(aircraftMfrModelCode)
                .doOnError(e -> logger.error("Error retrieving FAA aircraft registrations with aircraft manufacturer model code: {}", aircraftMfrModelCode, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving FAA aircraft registrations with aircraft manufacturer model code: {}", aircraftMfrModelCode, e);
                    return Flux.error(e);
                });
    }

    // Get FAA aircraft registrations by Mode S Code
    public Flux<FaaAircraftRegistration> getFaaAircraftRegistrationsByModeSCode(String modeSCode) {
        return faaAircraftRegistrationRepository.findByModeSCode(modeSCode)
                .doOnError(e -> logger.error("Error retrieving FAA aircraft registrations with Mode S Code: {}", modeSCode, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving FAA aircraft registrations with Mode S Code: {}", modeSCode, e);
                    return Flux.error(e);
                });
    }

    // Get FAA aircraft registrations by Mode S Code Hex
    public Flux<FaaAircraftRegistration> getFaaAircraftRegistrationsByModeScodeHex(String modeScodeHex) {
        return faaAircraftRegistrationRepository.findByModeScodeHex(modeScodeHex)
                .doOnError(e -> logger.error("Error retrieving FAA aircraft registrations with Mode S Code Hex: {}", modeScodeHex, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving FAA aircraft registrations with Mode S Code Hex: {}", modeScodeHex, e);
                    return Flux.error(e);
                });
    }

    // Create a new FAA aircraft registration
    public Mono<FaaAircraftRegistration> createFaaAircraftRegistration(FaaAircraftRegistration registration) {
        return faaAircraftRegistrationRepository.insert(registration)
                .doOnSuccess(r -> logger.info("Created FAA aircraft registration with N-Number: {}", r.getNNumber()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating FAA aircraft registration with N-Number: {}", registration.getNNumber(), e);
                    } else {
                        logger.error("Error creating FAA aircraft registration with N-Number: {} ; registration: {}", registration.getNNumber(), registration, e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing FAA aircraft registration
    public Mono<FaaAircraftRegistration> updateFaaAircraftRegistration(String nNumber, FaaAircraftRegistration registration) {
        registration.setNNumber(nNumber); // Ensure the ID is set correctly
        return faaAircraftRegistrationRepository.save(registration)
                .doOnSuccess(r -> logger.info("Updated FAA aircraft registration with N-Number: {}", r.getNNumber()))
                .doOnError(e -> logger.error("Error updating FAA aircraft registration with N-Number: {}", nNumber, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete a FAA aircraft registration
    public Mono<Void> deleteFaaAircraftRegistration(String nNumber) {
        return faaAircraftRegistrationRepository.findById(nNumber)
                .flatMap(registration -> faaAircraftRegistrationRepository.delete(registration)
                        .doOnSuccess(v -> logger.info("Deleted FAA aircraft registration with N-Number: {}", nNumber))
                        .doOnError(e -> logger.error("Error deleting FAA aircraft registration with N-Number: {}", nNumber, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }
}
