package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Passenger;
import com.execodex.sparrowair2.repositories.PassengerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PassengerService {

    private static final Logger logger = LoggerFactory.getLogger(PassengerService.class);
    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    // Get all passengers
    public Flux<Passenger> getAllPassengers() {
        return passengerRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all passengers", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all passengers", e);
                    return Flux.error(e);
                });
    }

    // Get passenger by ID
    public Mono<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id)
                .doOnError(e -> logger.error("Error retrieving passenger with ID: {}", id, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving passenger with ID: {}", id, e);
                    return Mono.error(e);
                });
    }

    // Get passengers by first name and last name
    public Flux<Passenger> getPassengersByFirstNameAndLastName(String firstName, String lastName) {
        return passengerRepository.findByFirstNameAndLastName(firstName, lastName)
                .doOnError(e -> logger.error("Error retrieving passengers with firstName: {} and lastName: {}", firstName, lastName, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving passengers with firstName: {} and lastName: {}", firstName, lastName, e);
                    return Flux.error(e);
                });
    }

    // Create a new passenger
    public Mono<Passenger> createPassenger(Passenger passenger) {
        return passengerRepository.insert(passenger)
                .doOnSuccess(p -> logger.info("Created passenger with ID: {}", p.getId()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating passenger with passport number: {}", passenger.getPassportNumber(), e);
                    } else {
                        logger.error("Error creating passenger", e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing passenger
    public Mono<Passenger> updatePassenger(Long id, Passenger passenger) {
        passenger.setId(id); // Ensure the ID is set correctly
        return passengerRepository.save(passenger)
                .doOnSuccess(p -> logger.info("Updated passenger with ID: {}", p.getId()))
                .doOnError(e -> logger.error("Error updating passenger with ID: {}", id, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete a passenger
    public Mono<Void> deletePassenger(Long id) {
        return passengerRepository.findById(id)
                .flatMap(passenger -> passengerRepository.delete(passenger)
                        .doOnSuccess(v -> logger.info("Deleted passenger with ID: {}", id))
                        .doOnError(e -> logger.error("Error deleting passenger with ID: {}", id, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }


    public Mono<Passenger> getPassengerByPassportNumber(String passportNumber) {
        return passengerRepository.findByPassportNumber(passportNumber)
                .doOnError(e -> logger.error("Error retrieving passenger with passport number: {}", passportNumber, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving passenger with passport number: {}", passportNumber, e);
                    return Mono.error(e);
                });
    }

    public Flux<Passenger> getPassengersByDateOfBirth(String dateOfBirth) {
        return passengerRepository.findByDateOfBirth(dateOfBirth)
                .doOnError(e -> logger.error("Error retrieving passengers with date of birth: {}", dateOfBirth, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving passengers with date of birth: {}", dateOfBirth, e);
                    return Flux.error(e);
                });
    }
}
