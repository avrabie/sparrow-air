package com.execodex.sparrowair2.configs;

import com.execodex.sparrowair2.entities.AircraftType;
import com.execodex.sparrowair2.entities.Airline;
import com.execodex.sparrowair2.entities.Airport;
import com.execodex.sparrowair2.entities.Flight;
import com.execodex.sparrowair2.services.AircraftTypeService;
import com.execodex.sparrowair2.services.AirlineService;
import com.execodex.sparrowair2.services.AirportService;
import com.execodex.sparrowair2.services.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("datademo")
public class DataDemoProfileConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataDemoProfileConfig.class);

    @Bean
    public CommandLineRunner initializeAirportData(AirportService airportService, AircraftTypeService aircraftTypeService, AirlineService airlineService, FlightService flightService) {
        return args -> {
            logger.info("Initializing sample data for 'datademo' profile");

            // Insert sample airports, aircraft types, airlines, and flights into the database
            generateAirport(airportService)
                    .thenMany(generateAircraftType(aircraftTypeService))
                    .thenMany(generateAirline(airlineService))
                    .thenMany(generateFlight(flightService))
                    .doOnComplete(() -> logger.info("Sample data initialization completed"))
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
    public Flux<Airport> generateAirport(AirportService airportService) {
        List<Airport> sampleAirports = Arrays.asList(
                Airport.builder()
                        .icaoCode("KJFK")
                        .name("John F. Kennedy International Airport")
                        .city("New York")
                        .country("United States")
                        .timezone("America/New_York")
                        .latitude(40.6413)
                        .longitude(-73.7781)
                        .build(),
                Airport.builder()
                        .icaoCode("EGLL")
                        .name("London Heathrow Airport")
                        .city("London")
                        .country("United Kingdom")
                        .timezone("Europe/London")
                        .latitude(51.4700)
                        .longitude(-0.4543)
                        .build(),
                Airport.builder()
                        .icaoCode("RJTT")
                        .name("Tokyo Haneda Airport")
                        .city("Tokyo")
                        .country("Japan")
                        .timezone("Asia/Tokyo")
                        .latitude(35.5494)
                        .longitude(139.7798)
                        .build(),
                Airport.builder()
                        .icaoCode("YSSY")
                        .name("Sydney Kingsford Smith Airport")
                        .city("Sydney")
                        .country("Australia")
                        .timezone("Australia/Sydney")
                        .latitude(-33.9399)
                        .longitude(151.1753)
                        .build(),
                Airport.builder()
                        .icaoCode("EDDF")
                        .name("Frankfurt Airport")
                        .city("Frankfurt")
                        .country("Germany")
                        .timezone("Europe/Berlin")
                        .latitude(50.0379)
                        .longitude(8.5622)
                        .build(),
                Airport.builder()
                        .icaoCode("LUKK")
                        .name("Chisinau International Airport")
                        .city("Chisinau")
                        .country("Moldova")
                        .timezone("Europe/Chisinau")
                        .latitude(46.9271)
                        .longitude(28.9302)
                        .build(),
                Airport.builder()
                        .icaoCode("LROP")
                        .name("Bucharest Henri Coanda International Airport")
                        .city("Bucharest")
                        .country("Romania")
                        .timezone("Europe/Bucharest")
                        .latitude(44.5711)
                        .longitude(26.0858)
                        .build(),
                Airport.builder()
                        .icaoCode("LFSB")
                        .name("EuroAirport Basel-Mulhouse-Freiburg")
                        .city("Basel")
                        .country("Switzerland")
                        .timezone("Europe/Zurich")
                        .latitude(47.5915)
                        .longitude(7.5294)
                        .build(),
                Airport.builder()
                        .icaoCode("LSZH")
                        .name("Zurich Airport")
                        .city("Zurich")
                        .country("Switzerland")
                        .timezone("Europe/Zurich")
                        .latitude(47.4502)
                        .longitude(8.5619)
                        .build(),
                Airport.builder()
                        .icaoCode("OMDB")
                        .name("Dubai International Airport")
                        .city("Dubai")
                        .country("United Arab Emirates")
                        .timezone("Asia/Dubai")
                        .latitude(25.2532)
                        .longitude(55.3657)
                        .build(),
                Airport.builder()
                        .icaoCode("KIAD")
                        .name("Washington Dulles International Airport")
                        .city("Washington, D.C.")
                        .country("United States")
                        .timezone("America/New_York")
                        .latitude(38.9531)
                        .longitude(-77.4470)
                        .build()

        );

        // Insert sample airports into the database
        Flux<Airport> airportFlux = Flux.fromIterable(sampleAirports)
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

    /**
     * Generates sample aircraft types and inserts them into the database if they do not already exist.
     *
     * @param aircraftTypeService The service to interact with aircraft type data.
     * @return A Flux of generated AircraftType objects.
     */

    @Bean(name = "aircraftTypeDataGenerator")
    public Flux<AircraftType> generateAircraftType(AircraftTypeService aircraftTypeService) {
        List<AircraftType> sampleAircraftTypes = Arrays.asList(
                AircraftType.builder()
                        .icaoCode("B738")
                        .modelName("737-800")
                        .manufacturer("Boeing")
                        .seatingCapacity(189)
                        .maxRangeKm(5765)
                        .mtow(79010)
                        .build(),
                AircraftType.builder()
                        .icaoCode("A320")
                        .modelName("A320-200")
                        .manufacturer("Airbus")
                        .seatingCapacity(180)
                        .maxRangeKm(6100)
                        .mtow(77000)
                        .build(),
                AircraftType.builder()
                        .icaoCode("B77W")
                        .modelName("777-300ER")
                        .manufacturer("Boeing")
                        .seatingCapacity(396)
                        .maxRangeKm(13650)
                        .mtow(351500)
                        .build(),
                AircraftType.builder()
                        .icaoCode("A388")
                        .modelName("A380-800")
                        .manufacturer("Airbus")
                        .seatingCapacity(853)
                        .maxRangeKm(15700)
                        .mtow(575000)
                        .build(),
                AircraftType.builder()
                        .icaoCode("E190")
                        .modelName("E190")
                        .manufacturer("Embraer")
                        .seatingCapacity(114)
                        .maxRangeKm(4537)
                        .mtow(51800)
                        .build()
        );

        // Insert sample aircraft types into the database
        Flux<AircraftType> aircraftTypeFlux = Flux.fromIterable(sampleAircraftTypes)
                .flatMap(aircraftType -> aircraftTypeService
                        .getAircraftTypeByIcaoCode(aircraftType.getIcaoCode())
                        .hasElement()
                        .flatMap(existingAircraftType -> {
                            if (existingAircraftType) {
                                logger.info("Aircraft type {} already exists, skipping creation", aircraftType.getIcaoCode());
                                return Mono.empty();
                            }
                            return aircraftTypeService.createAircraftType(aircraftType);
                        })
                        .onErrorResume(e -> {
                            logger.warn("Could not create aircraft type {}: {}", aircraftType.getIcaoCode(), e.getMessage());
                            return Mono.empty();
                        })
                );
        return aircraftTypeFlux;
    }

    /**
     * Generates sample airlines and inserts them into the database if they do not already exist.
     *
     * @param airlineService The service to interact with airline data.
     * @return A Flux of generated Airline objects.
     */
    @Bean(name = "airlineDataGenerator")
    public Flux<Airline> generateAirline(AirlineService airlineService) {
        List<Airline> sampleAirlines = Arrays.asList(
                Airline.builder()
                        .icaoCode("AAL")
                        .name("American Airlines")
                        .headquarters("Fort Worth, Texas, United States")
                        .contactNumber("+1-800-433-7300")
                        .website("https://www.aa.com")
                        .build(),
                Airline.builder()
                        .icaoCode("BAW")
                        .name("British Airways")
                        .headquarters("London, United Kingdom")
                        .contactNumber("+44-20-8738-5050")
                        .website("https://www.britishairways.com")
                        .build(),
                Airline.builder()
                        .icaoCode("DLH")
                        .name("Lufthansa")
                        .headquarters("Cologne, Germany")
                        .contactNumber("+49-69-86-799-799")
                        .website("https://www.lufthansa.com")
                        .build(),
                Airline.builder()
                        .icaoCode("UAE")
                        .name("Emirates")
                        .headquarters("Dubai, United Arab Emirates")
                        .contactNumber("+971-600-555-555")
                        .website("https://www.emirates.com")
                        .build(),
                Airline.builder()
                        .icaoCode("SIA")
                        .name("Singapore Airlines")
                        .headquarters("Singapore")
                        .contactNumber("+65-6223-8888")
                        .website("https://www.singaporeair.com")
                        .build(),
                Airline.builder()
                        .icaoCode("THY")
                        .name("Turkish Airlines")
                        .headquarters("Istanbul, Turkey")
                        .contactNumber("+90-212-463-6363")
                        .website("https://www.turkishairlines.com")
                        .build(),
                Airline.builder()
                        .icaoCode("WZZ")
                        .name("Wizz Air")
                        .headquarters("Budapest, Hungary")
                        .contactNumber("+36-1-777-9300")
                        .website("https://wizzair.com")
                        .build()
        );

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
     * @param flightService The service to interact with flight data.
     * @return A Flux of generated Flight objects.
     */
    @Bean(name = "flightDataGenerator")
    public Flux<Flight> generateFlight(FlightService flightService) {
        List<Flight> sampleFlights = Arrays.asList(
                Flight.builder()
                        .airlineIcaoCode("AAL")
                        .flightNumber("AA123")
                        .departureAirportIcao("KJFK")
                        .arrivalAirportIcao("EGLL")
                        .scheduledDeparture(LocalDateTime.now().plusDays(1))
                        .scheduledArrival(LocalDateTime.now().plusDays(1).plusHours(7))
                        .aircraftTypeIcao("B77W")
                        .status("Scheduled")
                        .build(),
                Flight.builder()
                        .airlineIcaoCode("BAW")
                        .flightNumber("BA456")
                        .departureAirportIcao("EGLL")
                        .arrivalAirportIcao("RJTT")
                        .scheduledDeparture(LocalDateTime.now().plusDays(2))
                        .scheduledArrival(LocalDateTime.now().plusDays(2).plusHours(12))
                        .aircraftTypeIcao("A388")
                        .status("Scheduled")
                        .build(),
                Flight.builder()
                        .airlineIcaoCode("DLH")
                        .flightNumber("LH789")
                        .departureAirportIcao("EDDF")
                        .arrivalAirportIcao("KJFK")
                        .scheduledDeparture(LocalDateTime.now().plusDays(3))
                        .scheduledArrival(LocalDateTime.now().plusDays(3).plusHours(9))
                        .aircraftTypeIcao("A320")
                        .status("Scheduled")
                        .build(),
                Flight.builder()
                        .airlineIcaoCode("UAE")
                        .flightNumber("EK101")
                        .departureAirportIcao("OMDB")
                        .arrivalAirportIcao("YSSY")
                        .scheduledDeparture(LocalDateTime.now().plusDays(4))
                        .scheduledArrival(LocalDateTime.now().plusDays(4).plusHours(14))
                        .aircraftTypeIcao("A388")
                        .status("Scheduled")
                        .build(),
                Flight.builder()
                        .airlineIcaoCode("SIA")
                        .flightNumber("SQ222")
                        .departureAirportIcao("YSSY")
                        .arrivalAirportIcao("RJTT")
                        .scheduledDeparture(LocalDateTime.now().plusDays(5))
                        .scheduledArrival(LocalDateTime.now().plusDays(5).plusHours(10))
                        .aircraftTypeIcao("B77W")
                        .status("Scheduled")
                        .build()
        );

        // Insert sample flights into the database
        Flux<Flight> flightFlux = Flux.fromIterable(sampleFlights)
                .flatMap(flight -> flightService
                        .createFlight(flight)
                        .onErrorResume(e -> {
                            logger.warn("Could not create flight {}: {}", flight.getFlightNumber(), e.getMessage());
                            return Mono.empty();
                        })
                );
        return flightFlux;
    }
}
