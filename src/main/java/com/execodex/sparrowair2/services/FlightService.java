package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Flight;
import com.execodex.sparrowair2.repositories.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);
    private final FlightRepository flightRepository;
    private final AirlineFleetService airlineFleetService;
    private final SeatService seatService;

    public FlightService(FlightRepository flightRepository, AirlineFleetService airlineFleetService, SeatService seatService) {
        this.flightRepository = flightRepository;
        this.airlineFleetService = airlineFleetService;
        this.seatService = seatService;
    }

    // Get all flights
    public Flux<Flight> getAllFlights() {
        return flightRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all flights", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all flights", e);
                    return Flux.error(e);
                });
    }

    // Get flight by ID
    public Mono<Flight> getFlightById(Long id) {
        return flightRepository.findById(id)
                .doOnError(e -> logger.error("Error retrieving flight with ID: {}", id, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving flight with ID: {}", id, e);
                    return Mono.error(e);
                });
    }

    // Create a new flight
    public Mono<Flight> createFlight(Flight flight) {
        return flightRepository.insert(flight)
                .doOnSuccess(f -> logger.info("Created flight with ID: {}", f.getId()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating flight", e);
                    } else {
                        logger.error("Error creating flight", e);
                    }
                })
                .flatMap(createdFlight -> {
                    // Get the airline fleet information to determine seat configuration
                    Long airlineFleetId = createdFlight.getAirlineFleetId();
                    if (airlineFleetId == null) {
                        logger.warn("Flight created without airlineFleetId, no seats will be created");
                        return Mono.just(createdFlight);
                    }

                    return airlineFleetService.getAirlineFleetById(airlineFleetId)
                            .flatMap(airlineFleet -> {
                                // Create seats based on the airline fleet configuration
                                Integer firstClassSeats = airlineFleet.getFirstClassSeats() != null ? airlineFleet.getFirstClassSeats() : 0;
                                Integer businessSeats = airlineFleet.getBusinessSeats() != null ? airlineFleet.getBusinessSeats() : 0;
                                Integer premiumEconomySeats = airlineFleet.getPremiumEconomySeats() != null ? airlineFleet.getPremiumEconomySeats() : 0;
                                Integer economySeats = airlineFleet.getEconomySeats() != null ? airlineFleet.getEconomySeats() : 0;

                                logger.info("Creating seats for flight ID: {}, for aircraftTypeIcao {}, First Class: {}, Business: {}, Premium Economy: {}, Economy: {}",
                                        createdFlight.getId(), airlineFleet.getAircraftTypeIcao(), firstClassSeats, businessSeats, premiumEconomySeats, economySeats);

                                return seatService.createSeatsForFlight(
                                        createdFlight.getId(), 
                                        firstClassSeats, 
                                        businessSeats, 
                                        premiumEconomySeats, 
                                        economySeats,
                                        airlineFleet.getSeatConfiguration()
                                ).then(Mono.just(createdFlight));
                            })
                            .onErrorResume(e -> {
                                logger.error("Error retrieving airline fleet or creating seats for flight ID: {}", createdFlight.getId(), e);
                                return Mono.just(createdFlight); // Return the flight even if seat creation fails
                            });
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing flight
    public Mono<Flight> updateFlight(Long id, Flight flight) {
        flight.setId(id); // Ensure the ID is set correctly

        // First, get the existing flight to check if airlineFleetId has changed
        return flightRepository.findById(id)
                .flatMap(existingFlight -> {
                    Long existingAirlineFleetId = existingFlight.getAirlineFleetId();
                    Long newAirlineFleetId = flight.getAirlineFleetId();

                    // Check if airlineFleetId has changed
                    boolean airlineFleetChanged = (existingAirlineFleetId != null && newAirlineFleetId != null && 
                                                !existingAirlineFleetId.equals(newAirlineFleetId)) ||
                                                (existingAirlineFleetId == null && newAirlineFleetId != null) ||
                                                (existingAirlineFleetId != null && newAirlineFleetId == null);

                    // If airlineFleetId has changed, delete existing seats and create new ones
                    if (airlineFleetChanged) {
                        logger.info("Airline fleet changed for flight ID: {} from {} to {}", id, existingAirlineFleetId, newAirlineFleetId);

                        // Save the updated flight first
                        return flightRepository.save(flight)
                                .flatMap(updatedFlight -> {
                                    // Delete existing seats
                                    return seatService.deleteByFlightId(id)
                                            .then(Mono.just(updatedFlight));
                                })
                                .flatMap(updatedFlight -> {
                                    // If new airlineFleetId is null, just return the updated flight
                                    if (newAirlineFleetId == null) {
                                        logger.warn("Flight updated with null airlineFleetId, no seats will be created");
                                        return Mono.just(updatedFlight);
                                    }

                                    // Get the new airline fleet information and create seats
                                    return airlineFleetService.getAirlineFleetById(newAirlineFleetId)
                                            .flatMap(airlineFleet -> {
                                                // Create seats based on the airline fleet configuration
                                                Integer firstClassSeats = airlineFleet.getFirstClassSeats() != null ? airlineFleet.getFirstClassSeats() : 0;
                                                Integer businessSeats = airlineFleet.getBusinessSeats() != null ? airlineFleet.getBusinessSeats() : 0;
                                                Integer premiumEconomySeats = airlineFleet.getPremiumEconomySeats() != null ? airlineFleet.getPremiumEconomySeats() : 0;
                                                Integer economySeats = airlineFleet.getEconomySeats() != null ? airlineFleet.getEconomySeats() : 0;

                                                logger.info("Creating seats for updated flight ID: {}, First Class: {}, Business: {}, Premium Economy: {}, Economy: {}", 
                                                        updatedFlight.getId(), firstClassSeats, businessSeats, premiumEconomySeats, economySeats);

                                                return seatService.createSeatsForFlight(
                                                        updatedFlight.getId(), 
                                                        firstClassSeats, 
                                                        businessSeats, 
                                                        premiumEconomySeats, 
                                                        economySeats,
                                                        airlineFleet.getSeatConfiguration()
                                                ).then(Mono.just(updatedFlight));
                                            })
                                            .onErrorResume(e -> {
                                                logger.error("Error retrieving airline fleet or creating seats for updated flight ID: {}", updatedFlight.getId(), e);
                                                return Mono.just(updatedFlight); // Return the flight even if seat creation fails
                                            });
                                });
                    } else {
                        // If airlineFleetId hasn't changed, just save the flight
                        return flightRepository.save(flight)
                                .doOnSuccess(f -> logger.info("Updated flight with ID: {}", f.getId()))
                                .doOnError(e -> logger.error("Error updating flight with ID: {}", id, e));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // If the flight doesn't exist, just save it (this should not happen in normal operation)
                    logger.warn("Attempted to update non-existent flight with ID: {}", id);
                    return flightRepository.save(flight);
                }))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete a flight
    public Mono<Void> deleteFlight(Long id) {
        return flightRepository.findById(id)
                .flatMap(flight -> {
                    // First delete all seats associated with the flight
                    return seatService.deleteByFlightId(id)
                            .then(flightRepository.delete(flight))
                            .doOnSuccess(v -> logger.info("Deleted flight with ID: {}", id))
                            .doOnError(e -> logger.error("Error deleting flight with ID: {}", id, e))
                            .onErrorResume(e -> Mono.error(e));
                })
                .switchIfEmpty(Mono.empty());
    }

    public Mono<Flight> getFlightByAirlineIcaoCodeAndFlightNumber(String airlineIcaoCode , String flightNumber) {
        return flightRepository.findByAirlineIcaoCodeAndFlightNumber(airlineIcaoCode, flightNumber)
                .doOnError(e -> logger.error("Error retrieving flight with flight number: {}", flightNumber, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving flight with flight number: {}", flightNumber, e);
                    return Mono.error(e);
                });
    }

    public Flux<Flight> getFlightsByAirlineIcaoCode(String airlineIcaoCode) {
        return flightRepository.findByAirlineIcaoCode(airlineIcaoCode)
                .doOnError(e -> logger.error("Error retrieving flights for airline: {}", airlineIcaoCode, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving flights for airline: {}", airlineIcaoCode, e);
                    return Flux.error(e);
                });
    }
}
