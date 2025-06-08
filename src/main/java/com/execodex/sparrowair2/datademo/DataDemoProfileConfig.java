package com.execodex.sparrowair2.datademo;

import com.execodex.sparrowair2.entities.*;
import com.execodex.sparrowair2.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("datademo")
public class DataDemoProfileConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataDemoProfileConfig.class);

    @Bean
    public CommandLineRunner initializeAirportData(AirportService airportService, AircraftService aircraftService,
                                                   AirlineService airlineService, FlightService flightService,
                                                   PassengerService passengerService, AirlineFleetService airlineFleetService,
                                                   BookingService bookingService, SeatService seatService,
                                                   BookingSegmentService bookingSegmentService) {
        return args -> {
            logger.info("Initializing sample data for 'datademo' profile");

            // Insert sample airports, aircraft types, airlines, airline fleet, flights, and passengers into the database
            generateAirport(airportService)
                    .thenMany(generateAircraft(aircraftService))
                    .thenMany(generateAirline(airlineService))
                    .thenMany(generateAirlineFleet(airlineFleetService))
                    .thenMany(generateFlight(flightService, airlineFleetService))
                    .thenMany(generateSeat(seatService, flightService))
                    .thenMany(generatePassenger(passengerService))
                    .thenMany(generateBooking(bookingService, passengerService))
                    .thenMany(generateBookingSegment(bookingSegmentService, bookingService, flightService, seatService))
                    .doOnComplete(() -> logger.info("Sample data initialization completed"))
                    .doOnError(e -> logger.error("Error during sample data initialization", e))
                    .blockLast();
        };
    }

    /**
     * Generates sample airports and inserts them into the database if they do not already exist.
     *
     * @param airportService The service to interact with airport data.
     * @return A Flux of generated Airport objects.
     */
    @Bean(name = "airportDataGenerator")
    public Flux<Airport2> generateAirport(AirportService airportService) {


        // Insert sample airports into the database
        List<Airport2> airport2s = SampleDataDemo.getDemoAirports();

        Flux<Airport2> airportFlux = Flux.fromIterable(airport2s)
                .flatMap(airport -> airportService
                        .getAirportByIcaoCode(airport.getIcaoCode())
                        .hasElement()
                        .flatMap(existingAirport -> {
                            if (existingAirport) {
                                logger.info("Airport {} already exists, skipping creation", airport.getIcaoCode());
                                return Mono.empty();
                            }
                            return airportService.createAirport(airport);
                        })
                        .onErrorResume(e -> {
                            logger.warn("Could not create airport {}: {}", airport.getIcaoCode(), e.getMessage());
                            return Mono.empty();
                        })
                );
        return airportFlux;
    }

    @Bean(name = "aircraftDataGenerato")
    public Flux<Aircraft> generateAircraft(AircraftService aircraftService) {
        Flux<Aircraft> demoAircraftsFromFile = AircraftDataDemo.getDemoAircraftsFromFile("stuff/data/iaka2.jsonl");
        return demoAircraftsFromFile.flatMap(aircraft -> aircraftService
                .getAircraftByIcaoCode(aircraft.getIcaoCode())
                .hasElement()
                .flatMap(existingAircraft -> {
                    if (existingAircraft) {
                        logger.info("Aircraft {} already exists, skipping creation", aircraft.getIcaoCode());
                        return Mono.empty();
                    }
                    return aircraftService.createAircraft(aircraft);
                })
        );
    }

    /**
     * Generates sample aircraft types and inserts them into the database if they do not already exist.
     *
     * @param aircraftTypeService The service to interact with aircraft type data.
     * @return A Flux of generated AircraftType objects.
     */

//    @Bean(name = "aircraftTypeDataGenerator")
//    public Flux<AircraftType> generateAircraftType(AircraftTypeService aircraftTypeService) {
//        List<AircraftType> demoAircraftTypes = SampleDataDemo.getDemoAircraftTypes();
//
//        // Insert sample aircraft types into the database
//        Flux<AircraftType> aircraftTypeFlux = Flux.fromIterable(demoAircraftTypes)
//                .flatMap(aircraftType -> aircraftTypeService
//                        .getAircraftTypeByIcaoCode(aircraftType.getIcaoCode())
//                        .hasElement()
//                        .flatMap(existingAircraftType -> {
//                            if (existingAircraftType) {
//                                logger.info("Aircraft type {} already exists, skipping creation", aircraftType.getIcaoCode());
//                                return Mono.empty();
//                            }
//                            return aircraftTypeService.createAircraftType(aircraftType);
//                        })
//                        .onErrorResume(e -> {
//                            logger.warn("Could not create aircraft type {}: {}", aircraftType.getIcaoCode(), e.getMessage());
//                            return Mono.empty();
//                        })
//                );
//        return aircraftTypeFlux;
//    }

    /**
     * Generates sample airlines and inserts them into the database if they do not already exist.
     *
     * @param airlineService The service to interact with airline data.
     * @return A Flux of generated Airline objects.
     */
    @Bean(name = "airlineDataGenerator")
    public Flux<Airline> generateAirline(AirlineService airlineService) {
        List<Airline> sampleAirlines = SampleDataDemo.getSampleAirlines();

        // Insert sample airlines into the database
        Flux<Airline> airlineFlux = Flux.fromIterable(sampleAirlines)
                .flatMap(airline -> airlineService
                        .getAirlineByIcaoCode(airline.getIcaoCode())
                        .hasElement()
                        .flatMap(existingAirline -> {
                            if (existingAirline) {
                                logger.info("Airline {} already exists, skipping creation", airline.getIcaoCode());
                                return Mono.empty();
                            }
                            return airlineService.createAirline(airline);
                        })
                        .onErrorResume(e -> {
                            logger.warn("Could not create airline {}: {}", airline.getIcaoCode(), e.getMessage());
                            return Mono.empty();
                        })
                );
        return airlineFlux;
    }

    /**
     * Generates sample flights and inserts them into the database if they do not already exist.
     *
     * @param flightService       The service to interact with flight data.
     * @param airlineFleetService The service to interact with airline fleet data.
     * @return A Flux of generated Flight objects.
     */

    @Bean(name = "flightGenerator")
    public Flux<Flight> generateFlight(FlightService flightService, AirlineFleetService airlineFleetService) {
        // First, get all airline fleet entries
        List<Flight> sampleFlights = SampleDataDemo.getSampleFlights();
        return Flux.fromIterable(sampleFlights)
                .flatMap(flight -> flightService
                        .getFlightByAirlineIcaoCodeAndFlightNumber(flight.getAirlineIcaoCode(), flight.getFlightNumber())
                        .hasElement()
                        .flatMap(existingFlight -> {
                            if (existingFlight) {
                                logger.info("Flight {} already exists, skipping creation", flight.getFlightNumber());
                                return Mono.empty();
                            }
                            return flightService.createFlight(flight);
                        })
                        .onErrorResume(e -> {
                            logger.warn("Could not create flight {}: {}", flight.getFlightNumber(), e.getMessage());
                            return Mono.empty();
                        })

                );

    }

    /**
     * Generates sample passengers and inserts them into the database if they do not already exist.
     *
     * @param passengerService The service to interact with passenger data.
     * @return A Flux of generated Passenger objects.
     */
    @Bean(name = "passengerDataGenerator")
    public Flux<Passenger> generatePassenger(PassengerService passengerService) {
        List<Passenger> samplePassengers = Arrays.asList(
                Passenger.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.of(1990, 1, 1))
                        .passportNumber("US123456789")
                        .nationality("United States")
                        .email("john.doe@example.com")
                        .phone("+1-555-123-4567")
                        .build(),
                Passenger.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .dateOfBirth(LocalDate.of(1985, 5, 15))
                        .passportNumber("UK987654321")
                        .nationality("United Kingdom")
                        .email("jane.smith@example.com")
                        .phone("+44-20-1234-5678")
                        .build(),
                Passenger.builder()
                        .firstName("Hans")
                        .lastName("Mueller")
                        .dateOfBirth(LocalDate.of(1988, 3, 20))
                        .passportNumber("DE456789123")
                        .nationality("Germany")
                        .email("hans.mueller@example.com")
                        .phone("+49-30-1234-5678")
                        .build(),
                Passenger.builder()
                        .firstName("Yuki")
                        .lastName("Tanaka")
                        .dateOfBirth(LocalDate.of(1992, 7, 30))
                        .passportNumber("JP789123456")
                        .nationality("Japan")
                        .email("yuki.tanaka@example.com")
                        .phone("+81-3-1234-5678")
                        .build(),
                Passenger.builder()
                        .firstName("Maria")
                        .lastName("Garcia")
                        .dateOfBirth(LocalDate.of(1980, 12, 25))
                        .passportNumber("ES321654987")
                        .nationality("Spain")
                        .email("maria.garcia@example.com")
                        .phone("+34-91-1234-5678")
                        .build()
        );

        // Insert sample passengers into the database
        Flux<Passenger> passengerFlux = Flux.fromIterable(samplePassengers)
                .flatMap(passenger -> passengerService
                        .getPassengerByPassportNumber(passenger.getPassportNumber())
                        .hasElement()
                        .flatMap(existingPassenger -> {
                            if (existingPassenger) {
                                logger.info("Passenger {} already exists, skipping creation", passenger.getPassportNumber());
                                return Mono.empty();
                            }
                            return passengerService.createPassenger(passenger);
                        })
                        .onErrorResume(e -> {
                            logger.warn("Could not create passenger {}: {}", passenger.getPassportNumber(), e.getMessage());
                            return Mono.empty();
                        })
                );
        return passengerFlux;
    }

    /**
     * Generates sample booking entries and inserts them into the database.
     *
     * @param bookingService   The service to interact with booking data.
     * @param passengerService The service to interact with passenger data.
     * @return A Flux of generated Booking objects.
     */
    @Bean(name = "bookingDataGenerator")
    public Flux<Booking> generateBooking(BookingService bookingService, PassengerService passengerService) {
        // Get all passengers to reference in bookings
        return passengerService.getAllPassengers()
                .collectList()
                .flatMapMany(passengers -> {
                    if (passengers.isEmpty()) {
                        logger.warn("No passengers found to create bookings for");
                        return Flux.empty();
                    }

                    // Create 5 sample bookings
                    List<Booking> sampleBookings = Arrays.asList(
                            Booking.builder()
                                    .bookingReference("BK12345")
                                    .passengerId(passengers.get(0).getId())
                                    .status("CONFIRMED")
                                    .createdAt(LocalDateTime.now())
                                    .build(),
                            Booking.builder()
                                    .bookingReference("BK23456")
                                    .passengerId(passengers.get(1).getId())
                                    .status("CONFIRMED")
                                    .createdAt(LocalDateTime.now())
                                    .build(),
                            Booking.builder()
                                    .bookingReference("BK34567")
                                    .passengerId(passengers.get(2).getId())
                                    .status("PENDING")
                                    .createdAt(LocalDateTime.now())
                                    .build(),
                            Booking.builder()
                                    .bookingReference("BK45678")
                                    .passengerId(passengers.get(3).getId())
                                    .status("CONFIRMED")
                                    .createdAt(LocalDateTime.now())
                                    .build(),
                            Booking.builder()
                                    .bookingReference("BK56789")
                                    .passengerId(passengers.get(4).getId())
                                    .status("CANCELLED")
                                    .createdAt(LocalDateTime.now())
                                    .build()
                    );

                    // Insert sample bookings into the database
                    return Flux.fromIterable(sampleBookings)
                            .flatMap(booking -> bookingService
                                    .getBookingByReference(booking.getBookingReference())
                                    .hasElement()
                                    .flatMap(existingBooking -> {
                                        if (existingBooking) {
                                            logger.info("Booking {} already exists, skipping creation", booking.getBookingReference());
                                            return Mono.empty();
                                        }
                                        return bookingService
                                                .createBooking(booking)
                                                .doOnSuccess(b -> logger.info("Created booking with reference: {}", b.getBookingReference()));
                                    })
                                    .onErrorResume(e -> {
                                        logger.warn("Could not create booking {}: {}", booking.getBookingReference(), e.getMessage());
                                        return Mono.empty();
                                    })
                            );
                });
    }

    /**
     * Generates sample airline fleet entries and inserts them into the database.
     *
     * @param airlineFleetService The service to interact with airline fleet data.
     * @return A Flux of generated AirlineFleet objects.
     */
    @Bean(name = "airlineFleetDataGenerator")
    public Flux<AirlineFleet> generateAirlineFleet(AirlineFleetService airlineFleetService) {

        List<AirlineFleet> sampleAirlineFleet = SampleDataDemo.getSampleAirlineFleet();
        // Insert sample airline fleet entries into the database
        Flux<AirlineFleet> airlineFleetFlux = Flux.fromIterable(sampleAirlineFleet)
                .flatMap(airlineFleet -> airlineFleetService
                        .getAirlineFleetByRegistration(airlineFleet.getRegistrationNumber())
                        .hasElement()
                        .flatMap(existingAirlineFleet -> {
                            if (existingAirlineFleet) {
                                logger.info("Airline fleet entry {} already exists, skipping creation", airlineFleet.getRegistrationNumber());
                                return Mono.empty();
                            }
                            return airlineFleetService.createAirlineFleet(airlineFleet);
                        }));

        return airlineFleetFlux;
    }

    /**
     * Generates sample seats for flights and inserts them into the database.
     *
     * @param seatService   The service to interact with seat data.
     * @param flightService The service to interact with flight data.
     * @return A Flux of generated Seat objects.
     */
    @Bean(name = "seatDataGenerator")
    public Flux<Seat> generateSeat(SeatService seatService, FlightService flightService) {
        return flightService.getAllFlights()
                .collectList()
                .flatMapMany(flights -> {
                    if (flights.isEmpty()) {
                        logger.warn("No flights found to create seats for");
                        return Flux.empty();
                    }

                    // Create sample seats for each flight
                    return Flux.fromIterable(flights)
                            .flatMap(flight -> {
                                // Create a mix of seat classes for each flight
                                List<Seat> flightSeats = Arrays.asList(
                                        // First class seats
                                        Seat.builder()
                                                .flightId(flight.getId())
                                                .seatNumber("1A")
                                                .seatClass(SeatClass.FIRST_CLASS)
                                                .status(SeatStatus.AVAILABLE)
                                                .build(),
                                        Seat.builder()
                                                .flightId(flight.getId())
                                                .seatNumber("1B")
                                                .seatClass(SeatClass.FIRST_CLASS)
                                                .status(SeatStatus.AVAILABLE)
                                                .build(),
                                        // Business class seats
                                        Seat.builder()
                                                .flightId(flight.getId())
                                                .seatNumber("5A")
                                                .seatClass(SeatClass.BUSINESS)
                                                .status(SeatStatus.AVAILABLE)
                                                .build(),
                                        Seat.builder()
                                                .flightId(flight.getId())
                                                .seatNumber("5B")
                                                .seatClass(SeatClass.BUSINESS)
                                                .status(SeatStatus.AVAILABLE)
                                                .build(),
                                        // Premium economy seats
                                        Seat.builder()
                                                .flightId(flight.getId())
                                                .seatNumber("10A")
                                                .seatClass(SeatClass.PREMIUM_ECONOMY)
                                                .status(SeatStatus.AVAILABLE)
                                                .build(),
                                        Seat.builder()
                                                .flightId(flight.getId())
                                                .seatNumber("10B")
                                                .seatClass(SeatClass.PREMIUM_ECONOMY)
                                                .status(SeatStatus.AVAILABLE)
                                                .build(),
                                        // Economy seats
                                        Seat.builder()
                                                .flightId(flight.getId())
                                                .seatNumber("20A")
                                                .seatClass(SeatClass.ECONOMY)
                                                .status(SeatStatus.AVAILABLE)
                                                .build(),
                                        Seat.builder()
                                                .flightId(flight.getId())
                                                .seatNumber("20B")
                                                .seatClass(SeatClass.ECONOMY)
                                                .status(SeatStatus.AVAILABLE)
                                                .build()
                                );

                                // Insert sample seats into the database
                                return Flux.fromIterable(flightSeats)
                                        .flatMap(seat -> seatService.createSeat(seat)
                                                .doOnSuccess(s -> logger.info("Created seat {} for flight {}",
                                                        s.getSeatNumber(), flight.getFlightNumber()))
                                                .onErrorResume(e -> {
                                                    logger.warn("Could not create seat {} for flight {}: {}",
                                                            seat.getSeatNumber(), flight.getFlightNumber(), e.toString());
                                                    return Mono.empty();
                                                })
                                        );
                            });
                });
    }

    /**
     * Generates sample booking segments and inserts them into the database.
     *
     * @param bookingSegmentService The service to interact with booking segment data.
     * @param bookingService        The service to interact with booking data.
     * @param flightService         The service to interact with flight data.
     * @param seatService           The service to interact with seat data.
     * @return A Flux of generated BookingSegment objects.
     */
    @Bean(name = "bookingSegmentDataGenerator")
    public Flux<BookingSegment> generateBookingSegment(BookingSegmentService bookingSegmentService,
                                                       BookingService bookingService,
                                                       FlightService flightService,
                                                       SeatService seatService) {
        // Get all bookings, flights, and seats to reference in booking segments
        return Mono.zip(
                bookingService.getAllBookings().collectList(),
                flightService.getAllFlights().collectList(),
                seatService.getAllSeats().collectList()
        ).flatMapMany(tuple -> {
            List<Booking> bookings = tuple.getT1();
            List<Flight> flights = tuple.getT2();
            List<Seat> seats = tuple.getT3();

            if (bookings.isEmpty() || flights.isEmpty() || seats.isEmpty()) {
                logger.warn("Missing data to create booking segments: bookings={}, flights={}, seats={}",
                        bookings.size(), flights.size(), seats.size());
                return Flux.empty();
            }

            // Create sample booking segments
            List<BookingSegment> sampleBookingSegments = new java.util.ArrayList<>();

            // Create booking segments for each booking, connecting to available flights and seats
            for (int i = 0; i < Math.min(bookings.size(), flights.size()); i++) {
                Booking booking = bookings.get(i);
                Flight flight = flights.get(i);

                // Find available seats for this flight
                List<Seat> flightSeats = seats.stream()
                        .filter(seat -> seat.getFlightId().equals(flight.getId()) && seat.getStatus() == SeatStatus.AVAILABLE)
                        .collect(java.util.stream.Collectors.toList());

                if (!flightSeats.isEmpty()) {
                    // Use a random available seat for the booking segment
                    int size = flightSeats.size();
                    int randomIndex = (int) (Math.random() * size) - 1;
//                    Seat seat = Math.random() < 0.5 ? flightSeats.get(0) : flightSeats.get(flightSeats.size() - 1);
                    Seat seat = flightSeats.get(randomIndex);

                    // Create a booking segment
                    BookingSegment bookingSegment = BookingSegment.builder()
                            .bookingId(booking.getId())
                            .flightId(flight.getId())
                            .seatId(seat.getId())
                            .fareClass(seat.getSeatClass().name())
                            .ticketNumber("TKT" + booking.getBookingReference() + "-" + flight.getFlightNumber() + "-" + seat.getSeatNumber())
                            .build();

                    sampleBookingSegments.add(bookingSegment);
                }
            }

            // Insert sample booking segments into the database
            return Flux.fromIterable(sampleBookingSegments)
                    .flatMap(bookingSegment -> bookingSegmentService
                            .getBookingSegmentByFlightIdAndSeatId(bookingSegment.getFlightId(), bookingSegment.getSeatId())
                            .hasElement()
                            .flatMap(existingBookingSegment -> {
                                if (existingBookingSegment) {
                                    logger.info("Booking segment for flight {} and seat {} already exists, skipping creation",
                                            bookingSegment.getFlightId(), bookingSegment.getSeatId());
                                    return Mono.empty();
                                }
                                return bookingSegmentService.createBookingSegment(bookingSegment);
                            })
                            .doOnSuccess(bs -> logger.info("Created booking segment with ticket number: {}", bs.getTicketNumber()))
                            .onErrorResume(e -> {
                                logger.warn("Could not create booking segment: {}", e.toString());
                                return Mono.empty();
                            })
                    );
//
        });
    }
}
