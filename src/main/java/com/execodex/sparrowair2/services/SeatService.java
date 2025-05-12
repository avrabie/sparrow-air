package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Seat;
import com.execodex.sparrowair2.entities.SeatClass;
import com.execodex.sparrowair2.entities.SeatStatus;
import com.execodex.sparrowair2.repositories.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SeatService {

    private static final Logger logger = LoggerFactory.getLogger(SeatService.class);
    private final SeatRepository seatRepository;
    private final FlightService flightService;

    public SeatService(SeatRepository seatRepository, @Lazy FlightService flightService) {
        this.seatRepository = seatRepository;
        this.flightService = flightService;
    }

    // Get all seats
    public Flux<Seat> getAllSeats() {
        return seatRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all seats", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all seats", e);
                    return Flux.error(e);
                });
    }

    // Get seat by ID
    public Mono<Seat> getSeatById(Long id) {
        return seatRepository.findById(id)
                .doOnError(e -> logger.error("Error retrieving seat with ID: {}", id, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving seat with ID: {}", id, e);
                    return Mono.error(e);
                });
    }

    // Get seats by flight ID
    public Flux<Seat> getSeatsByFlightId(Long flightId) {
        return seatRepository.findByFlightId(flightId)
                .doOnError(e -> logger.error("Error retrieving seats for flight ID: {}", flightId, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving seats for flight ID: {}", flightId, e);
                    return Flux.error(e);
                });
    }

    // Create a new seat
    public Mono<Seat> createSeat(Seat seat) {
        return seatRepository.findByFlightIdAndSeatNumber(seat.getFlightId(), seat.getSeatNumber())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        logger.error("Seat with flight ID: {} and seat number: {} already exists", seat.getFlightId(), seat.getSeatNumber());
                        return Mono.empty();
                    }
                    return seatRepository.insert(seat);
                })
                .doOnSuccess(s -> logger.info("Created seat with ID: {}", s.getId()))
                .doOnError(e -> logger.error("Error creating seat", e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Create multiple seats for a flight
    public Flux<Seat> createSeatsForFlight(Long flightId, int firstClassCount, int businessCount, int premiumEconomyCount, int economyCount, String seatConfiguration) {
        logger.info("Creating seats for flight ID: {}, with seat configuration: {}", flightId, seatConfiguration);

        // Create first class seats
        Flux<Seat> firstClassSeats = createSeatsOfClass(flightId, firstClassCount, SeatClass.FIRST_CLASS, seatConfiguration);

        // Create business class seats
        Flux<Seat> businessSeats = createSeatsOfClass(flightId, businessCount, SeatClass.BUSINESS, seatConfiguration);

        // Create premium economy seats
        Flux<Seat> premiumEconomySeats = createSeatsOfClass(flightId, premiumEconomyCount, SeatClass.PREMIUM_ECONOMY, seatConfiguration);

        // Create economy seats
        Flux<Seat> economySeats = createSeatsOfClass(flightId, economyCount, SeatClass.ECONOMY, seatConfiguration);

        // Combine all seat creation operations
        return Flux.concat(firstClassSeats, businessSeats, premiumEconomySeats, economySeats);
    }

    // Overloaded method for backward compatibility
    public Flux<Seat> createSeatsForFlight(Long flightId, int firstClassCount, int businessCount, int premiumEconomyCount, int economyCount) {
        // Default to a common configuration if none provided
        return createSeatsForFlight(flightId, firstClassCount, businessCount, premiumEconomyCount, economyCount, "3-3");
    }

    // Helper method to create seats of a specific class
    private Flux<Seat> createSeatsOfClass(Long flightId, int count, SeatClass seatClass, String seatConfiguration) {
        if (count <= 0) {
            return Flux.empty();
        }

        return Flux.range(1, count)
                .flatMap(i -> {
                    String seatNumber = generateSeatNumber(seatClass, i, seatConfiguration);
                    Seat seat = Seat.builder()
                            .flightId(flightId)
                            .seatNumber(seatNumber)
                            .seatClass(seatClass)
                            .status(SeatStatus.AVAILABLE)
                            .build();
                    return createSeat(seat);
                });
    }

    // Overloaded method for backward compatibility
    private Flux<Seat> createSeatsOfClass(Long flightId, int count, SeatClass seatClass) {
        return createSeatsOfClass(flightId, count, seatClass, "3-3");
    }

    // Helper method to generate seat numbers based on class, index, and seat configuration
    private String generateSeatNumber(SeatClass seatClass, int index, String seatConfiguration) {
        // Calculate row and seat letter
        int seatsPerRow = calculateSeatsPerRow(seatConfiguration);
        int row = (index - 1) / seatsPerRow + 1;
        int seatIndex = (index - 1) % seatsPerRow;

        // Convert seat index to letter (A, B, C, etc.)
        char seatLetter = (char) ('A' + seatIndex);

        // Determine if it's a window seat
        String position = determinePosition(seatLetter, seatConfiguration);

        // Format: "15A, Economy, Window"
        return row + String.valueOf(seatLetter) + ", " + seatClass.toString() + ", " + position;
    }

    // Helper method to calculate total seats per row based on configuration
    private int calculateSeatsPerRow(String seatConfiguration) {
        if (seatConfiguration == null || seatConfiguration.isEmpty()) {
            return 6; // Default to 3-3 configuration (6 seats per row)
        }

        // Sum up all numbers in the configuration (e.g., "3-3" = 6, "2-3-2" = 7)
        int total = 0;
        for (String part : seatConfiguration.split("-")) {
            try {
                total += Integer.parseInt(part);
            } catch (NumberFormatException e) {
                logger.warn("Invalid seat configuration part: {}", part);
            }
        }

        return total > 0 ? total : 6; // Default to 6 if parsing fails
    }

    // Helper method to determine if a seat is window, middle, or aisle
    private String determinePosition(char seatLetter, String seatConfiguration) {
        if (seatConfiguration == null || seatConfiguration.isEmpty()) {
            return "Unknown";
        }

        String[] sections = seatConfiguration.split("-");
        int seatIndex = seatLetter - 'A';

        // Check if it's a window seat (first or last seat in the row)
        if (seatIndex == 0 || seatIndex == calculateSeatsPerRow(seatConfiguration) - 1) {
            return "Window";
        }

        // Calculate section boundaries to determine aisle seats
        int currentPosition = 0;
        for (String section : sections) {
            try {
                int sectionSize = Integer.parseInt(section);
                currentPosition += sectionSize;

                // If we're at a section boundary (except the last one), it's an aisle seat
                if (seatIndex == currentPosition - 1 || seatIndex == currentPosition) {
                    return "Aisle";
                }
            } catch (NumberFormatException e) {
                logger.warn("Invalid seat configuration section: {}", section);
            }
        }

        // If not window or aisle, it's a middle seat
        return "Middle";
    }

    // Delete seats by flight ID
    public Mono<Void> deleteByFlightId(Long flightId) {
        logger.info("Deleting seats for flight ID: {}", flightId);
        return seatRepository.deleteByFlightId(flightId)
                .doOnSuccess(v -> logger.info("Deleted seats for flight ID: {}", flightId))
                .doOnError(e -> logger.error("Error deleting seats for flight ID: {}", flightId, e))
                .onErrorResume(e -> Mono.error(e));
    }

    public Flux<Seat> getSeatsByFlightNumber(String airlineNameIcao, String flightNumber) {
        return flightService.getFlightByAirlineIcaoCodeAndFlightNumber(airlineNameIcao, flightNumber)
                .flatMapMany(flight -> getSeatsByFlightId(flight.getId()))
                .doOnError(e -> logger.error("Error retrieving seats for flight number: {}", flightNumber, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving seats for flight number: {}", flightNumber, e);
                    return Flux.error(e);
                });
    }
}
