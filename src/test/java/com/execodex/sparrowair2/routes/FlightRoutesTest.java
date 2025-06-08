package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.configs.AbstractTestcontainersTest;
import com.execodex.sparrowair2.entities.*;
import com.execodex.sparrowair2.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightRoutesTest extends AbstractTestcontainersTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private AirlineFleetRepository airlineFleetRepository;

    @Autowired
    private SeatRepository seatRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repositories before each test
        // Delete seats first to avoid foreign key constraint violations
        seatRepository.deleteAll().block();
        flightRepository.deleteAll().block();
        airlineFleetRepository.deleteAll().block();
//        aircraftRepository.deleteAll().block();

        // Create test airlines
        Airline airline1 = Airline.builder()
                .icaoCode("AAL")
                .name("American Airlines")
                .headquarters("Fort Worth, Texas, United States")
                .contactNumber("+1-800-433-7300")
                .website("https://www.aa.com")
                .build();

        Airline airline2 = Airline.builder()
                .icaoCode("BAW")
                .name("British Airways")
                .headquarters("London, United Kingdom")
                .contactNumber("+44-20-8738-5050")
                .website("https://www.britishairways.com")
                .build();

        Airline airline3 = Airline.builder()
                .icaoCode("DLH")
                .name("Lufthansa")
                .headquarters("Cologne, Germany")
                .contactNumber("+49-69-86-799-799")
                .website("https://www.lufthansa.com")
                .build();

        Airline airline4 = Airline.builder()
                .icaoCode("UAE")
                .name("Emirates")
                .headquarters("Dubai, United Arab Emirates")
                .contactNumber("+971-600-555-555")
                .website("https://www.emirates.com")
                .build();

        Airline airline5 = Airline.builder()
                .icaoCode("SIA")
                .name("Singapore Airlines")
                .headquarters("Singapore")
                .contactNumber("+65-6223-8888")
                .website("https://www.singaporeair.com")
                .build();

        // Save airlines
        airlineRepository.deleteAll().block();
        airlineRepository.insert(airline1).block();
        airlineRepository.insert(airline2).block();
        airlineRepository.insert(airline3).block();
        airlineRepository.insert(airline4).block();
        airlineRepository.insert(airline5).block();

        // Create test airports
        Airport airport21 = Airport.builder()
                .icaoCode("KJFK")
                .name("John F. Kennedy International Airport")
                .city("New York")
                .country("United States")
                .timezone("America/New_York")
                .latitude(40.6413)
                .longitude(-73.7781)
                .build();

        Airport airport = Airport.builder()
                .icaoCode("EGLL")
                .name("London Heathrow Airport")
                .city("London")
                .country("United Kingdom")
                .timezone("Europe/London")
                .latitude(51.4700)
                .longitude(-0.4543)
                .build();

        Airport airport23 = Airport.builder()
                .icaoCode("RJTT")
                .name("Tokyo Haneda Airport")
                .city("Tokyo")
                .country("Japan")
                .timezone("Asia/Tokyo")
                .latitude(35.5494)
                .longitude(139.7798)
                .build();

        Airport airport24 = Airport.builder()
                .icaoCode("YSSY")
                .name("Sydney Kingsford Smith Airport")
                .city("Sydney")
                .country("Australia")
                .timezone("Australia/Sydney")
                .latitude(-33.9399)
                .longitude(151.1753)
                .build();

        Airport airport25 = Airport.builder()
                .icaoCode("EDDF")
                .name("Frankfurt Airport")
                .city("Frankfurt")
                .country("Germany")
                .timezone("Europe/Berlin")
                .latitude(50.0379)
                .longitude(8.5622)
                .build();

        Airport airport26 = Airport.builder()
                .icaoCode("OMDB")
                .name("Dubai International Airport")
                .city("Dubai")
                .country("United Arab Emirates")
                .timezone("Asia/Dubai")
                .latitude(25.2532)
                .longitude(55.3657)
                .build();

        // Save airports
        airportRepository.deleteAll().block();
        airportRepository.insert(airport21).block();
        airportRepository.insert(airport).block();
        airportRepository.insert(airport23).block();
        airportRepository.insert(airport24).block();
        airportRepository.insert(airport25).block();
        airportRepository.insert(airport26).block();

        // Create test aircraft types
        Aircraft aircraftType1 = Aircraft.builder()
                .icaoCode("B738")
                .name("Boeing 737-800")
                .manufacturer("Boeing")
                .rangeNm(5600)
                .maxTakeOffWeightKg(79400)
                .build();
        Aircraft aircraftType2 = Aircraft.builder()
                .icaoCode("A320")
                .name("Airbus A320-200")
                .manufacturer("Airbus")
                .rangeNm(3100)
                .maxTakeOffWeightKg(78000)
                .build();
        Aircraft aircraftType3 = Aircraft.builder()
                .icaoCode("B77W")
                .name("Boeing 777-300ER")
                .manufacturer("Boeing")
                .rangeNm(7400)
                .maxTakeOffWeightKg(351500)
                .build();


        // Save aircraft types
        aircraftRepository.deleteAll().block();
        aircraftRepository.insert(aircraftType1).block();
        aircraftRepository.insert(aircraftType2).block();
        aircraftRepository.insert(aircraftType3).block();

        // Create test airline fleet entries
        AirlineFleet airlineFleet1 = AirlineFleet.builder()
                .aircraftTypeIcao("B77W")
                .airlineIcao("AAL")
                .registrationNumber("UA 777")
                .aircraftAge(LocalDate.of(2015, 5, 12))
                .seatConfiguration("3-4-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(8)
                .businessSeats(42)
                .economySeats(304)
                .build();

        AirlineFleet airlineFleet2 = AirlineFleet.builder()
                .aircraftTypeIcao("B77W")
                .airlineIcao("BAW")
                .registrationNumber("UA 388")
                .aircraftAge(LocalDate.of(2010, 8, 15))
                .seatConfiguration("3-4-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(14)
                .businessSeats(76)
                .economySeats(427)
                .build();

        AirlineFleet airlineFleet3 = AirlineFleet.builder()
                .aircraftTypeIcao("A320")
                .airlineIcao("DLH")
                .registrationNumber("UA 320")
                .aircraftAge(LocalDate.of(2018, 3, 24))
                .seatConfiguration("3-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(false)
                .firstClassSeats(0)
                .businessSeats(32)
                .economySeats(120)
                .build();

        AirlineFleet airlineFleet4 = AirlineFleet.builder()
                .aircraftTypeIcao("B77W")
                .airlineIcao("UAE")
                .registrationNumber("UA 388")
                .aircraftAge(LocalDate.of(2012, 11, 7))
                .seatConfiguration("3-4-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(14)
                .businessSeats(76)
                .economySeats(427)
                .build();

        AirlineFleet airlineFleet5 = AirlineFleet.builder()
                .aircraftTypeIcao("B77W")
                .airlineIcao("SIA")
                .registrationNumber("UA 777")
                .aircraftAge(LocalDate.of(2014, 2, 3))
                .seatConfiguration("3-4-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(8)
                .businessSeats(42)
                .economySeats(304)
                .build();

        // Save airline fleet entries
        airlineFleetRepository.insert(airlineFleet1).block();
        airlineFleetRepository.insert(airlineFleet2).block();
        airlineFleetRepository.insert(airlineFleet3).block();
        airlineFleetRepository.insert(airlineFleet4).block();
        airlineFleetRepository.insert(airlineFleet5).block();
    }

    @Test
    public void testCreateFlight() {
        LocalDateTime scheduledDeparture = LocalDateTime.now().plusDays(1);
        LocalDateTime scheduledArrival = scheduledDeparture.plusHours(7);

        // Get the ID of the AAL B77W airline fleet entry
        AirlineFleet airlineFleet = airlineFleetRepository.findAll()
                .filter(af -> af.getAirlineIcao().equals("AAL") && af.getAircraftTypeIcao().equals("B77W"))
                .blockFirst();

        Flight flight = Flight.builder()
                .airlineIcaoCode("AAL")
                .flightNumber("AA123")
                .departureAirportIcao("KJFK")
                .arrivalAirportIcao("EGLL")
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .airlineFleetId(airlineFleet.getId())
                .status("Scheduled")
                .build();

        webTestClient.post()
                .uri("/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(flight), Flight.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Flight.class)
                .value(f -> {
                    assert f.getAirlineIcaoCode().equals(flight.getAirlineIcaoCode());
                    assert f.getFlightNumber().equals(flight.getFlightNumber());
                    assert f.getDepartureAirportIcao().equals(flight.getDepartureAirportIcao());
                    assert f.getArrivalAirportIcao().equals(flight.getArrivalAirportIcao());
                    assert f.getScheduledDeparture().equals(flight.getScheduledDeparture());
                    assert f.getScheduledArrival().equals(flight.getScheduledArrival());
                    assert f.getAirlineFleetId().equals(flight.getAirlineFleetId());
                    assert f.getStatus().equals(flight.getStatus());
                });
    }

    @Test
    public void testGetAllFlights() {
        // Create a test flight
        LocalDateTime scheduledDeparture = LocalDateTime.now().plusDays(2);
        LocalDateTime scheduledArrival = scheduledDeparture.plusHours(12);

        // Get the ID of the BAW A388 airline fleet entry
        AirlineFleet airlineFleet = airlineFleetRepository.findAll()
                .filter(af -> af.getAirlineIcao().equals("BAW") && af.getAircraftTypeIcao().equals("B77W"))
                .blockFirst();

        Flight flight = Flight.builder()
                .airlineIcaoCode("BAW")
                .flightNumber("BA456")
                .departureAirportIcao("EGLL")
                .arrivalAirportIcao("RJTT")
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .airlineFleetId(airlineFleet.getId())
                .status("Scheduled")
                .build();

        // Save the flight and wait for it to complete
        Flight savedFlight = flightRepository.insert(flight).block();

        webTestClient.get()
                .uri("/flights")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Flight.class)
                .contains(savedFlight);
    }

    @Test
    public void testGetFlightById() {
        // Create a test flight
        LocalDateTime scheduledDeparture = LocalDateTime.now().plusDays(3);
        LocalDateTime scheduledArrival = scheduledDeparture.plusHours(9);

        // Get the ID of the DLH A320 airline fleet entry
        AirlineFleet airlineFleet = airlineFleetRepository.findAll()
                .filter(af -> af.getAirlineIcao().equals("DLH") && af.getAircraftTypeIcao().equals("A320"))
                .blockFirst();

        Flight flight = Flight.builder()
                .airlineIcaoCode("DLH")
                .flightNumber("LH789")
                .departureAirportIcao("EDDF")
                .arrivalAirportIcao("KJFK")
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .airlineFleetId(airlineFleet.getId())
                .status("Scheduled")
                .build();

        // Save the flight and wait for it to complete
        Flight savedFlight = flightRepository.insert(flight).block();

        webTestClient.get()
                .uri("/flights/{id}", savedFlight.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Flight.class)
                .isEqualTo(savedFlight);
    }

    @Test
    public void testUpdateFlight() {
        // Create a test flight
        LocalDateTime scheduledDeparture = LocalDateTime.now().plusDays(4);
        LocalDateTime scheduledArrival = scheduledDeparture.plusHours(14);

        // Get the ID of the UAE A388 airline fleet entry
        AirlineFleet airlineFleet = airlineFleetRepository.findAll()
                .filter(af -> af.getAirlineIcao().equals("UAE") && af.getAircraftTypeIcao().equals("B77W"))
                .blockFirst();

        Flight flight = Flight.builder()
                .airlineIcaoCode("UAE")
                .flightNumber("EK101")
                .departureAirportIcao("OMDB")
                .arrivalAirportIcao("YSSY")
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .airlineFleetId(airlineFleet.getId())
                .status("Scheduled")
                .build();

        // Save the flight and wait for it to complete
        Flight savedFlight = flightRepository.insert(flight).block();

        // Update the flight
        Flight updatedFlight = Flight.builder()
                .id(savedFlight.getId())
                .airlineIcaoCode("UAE")
                .flightNumber("EK101")
                .departureAirportIcao("OMDB")
                .arrivalAirportIcao("YSSY")
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .airlineFleetId(airlineFleet.getId())
                .status("Delayed") // Updated status
                .build();

        webTestClient.put()
                .uri("/flights/{id}", savedFlight.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedFlight), Flight.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Flight.class)
                .isEqualTo(updatedFlight);
    }

    @Test
    public void testDeleteFlight() {
        // Create a test flight
        LocalDateTime scheduledDeparture = LocalDateTime.now().plusDays(5);
        LocalDateTime scheduledArrival = scheduledDeparture.plusHours(10);

        // Get the ID of the SIA B77W airline fleet entry
        AirlineFleet airlineFleet = airlineFleetRepository.findAll()
                .filter(af -> af.getAirlineIcao().equals("SIA") && af.getAircraftTypeIcao().equals("B77W"))
                .blockFirst();

        Flight flight = Flight.builder()
                .airlineIcaoCode("SIA")
                .flightNumber("SQ222")
                .departureAirportIcao("YSSY")
                .arrivalAirportIcao("RJTT")
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .airlineFleetId(airlineFleet.getId())
                .status("Scheduled")
                .build();

        // Save the flight and wait for it to complete
        Flight savedFlight = flightRepository.insert(flight).block();

        webTestClient.delete()
                .uri("/flights/{id}", savedFlight.getId())
                .exchange()
                .expectStatus().isNoContent();

        // Verify the flight was deleted
        webTestClient.get()
                .uri("/flights/{id}", savedFlight.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
