package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.configs.AbstractTestcontainersTest;
import com.execodex.sparrowair2.entities.AircraftType;
import com.execodex.sparrowair2.entities.Airline;
import com.execodex.sparrowair2.entities.Airport;
import com.execodex.sparrowair2.entities.Flight;
import com.execodex.sparrowair2.repositories.AircraftTypeRepository;
import com.execodex.sparrowair2.repositories.AirlineRepository;
import com.execodex.sparrowair2.repositories.AirportRepository;
import com.execodex.sparrowair2.repositories.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

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
    private AircraftTypeRepository aircraftTypeRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repositories before each test
        flightRepository.deleteAll().block();

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
        Airport airport1 = Airport.builder()
                .icaoCode("KJFK")
                .name("John F. Kennedy International Airport")
                .city("New York")
                .country("United States")
                .timezone("America/New_York")
                .latitude(40.6413)
                .longitude(-73.7781)
                .build();

        Airport airport2 = Airport.builder()
                .icaoCode("EGLL")
                .name("London Heathrow Airport")
                .city("London")
                .country("United Kingdom")
                .timezone("Europe/London")
                .latitude(51.4700)
                .longitude(-0.4543)
                .build();

        Airport airport3 = Airport.builder()
                .icaoCode("RJTT")
                .name("Tokyo Haneda Airport")
                .city("Tokyo")
                .country("Japan")
                .timezone("Asia/Tokyo")
                .latitude(35.5494)
                .longitude(139.7798)
                .build();

        Airport airport4 = Airport.builder()
                .icaoCode("YSSY")
                .name("Sydney Kingsford Smith Airport")
                .city("Sydney")
                .country("Australia")
                .timezone("Australia/Sydney")
                .latitude(-33.9399)
                .longitude(151.1753)
                .build();

        Airport airport5 = Airport.builder()
                .icaoCode("EDDF")
                .name("Frankfurt Airport")
                .city("Frankfurt")
                .country("Germany")
                .timezone("Europe/Berlin")
                .latitude(50.0379)
                .longitude(8.5622)
                .build();

        Airport airport6 = Airport.builder()
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
        airportRepository.insert(airport1).block();
        airportRepository.insert(airport2).block();
        airportRepository.insert(airport3).block();
        airportRepository.insert(airport4).block();
        airportRepository.insert(airport5).block();
        airportRepository.insert(airport6).block();

        // Create test aircraft types
        AircraftType aircraftType1 = AircraftType.builder()
                .icaoCode("B77W")
                .modelName("777-300ER")
                .manufacturer("Boeing")
                .seatingCapacity(396)
                .maxRangeKm(13650)
                .mtow(351500)
                .build();

        AircraftType aircraftType2 = AircraftType.builder()
                .icaoCode("A388")
                .modelName("A380-800")
                .manufacturer("Airbus")
                .seatingCapacity(853)
                .maxRangeKm(15700)
                .mtow(575000)
                .build();

        AircraftType aircraftType3 = AircraftType.builder()
                .icaoCode("A320")
                .modelName("A320-200")
                .manufacturer("Airbus")
                .seatingCapacity(180)
                .maxRangeKm(6100)
                .mtow(77000)
                .build();

        // Save aircraft types
        aircraftTypeRepository.deleteAll().block();
        aircraftTypeRepository.insert(aircraftType1).block();
        aircraftTypeRepository.insert(aircraftType2).block();
        aircraftTypeRepository.insert(aircraftType3).block();
    }

    @Test
    public void testCreateFlight() {
        LocalDateTime scheduledDeparture = LocalDateTime.now().plusDays(1);
        LocalDateTime scheduledArrival = scheduledDeparture.plusHours(7);

        Flight flight = Flight.builder()
                .airlineIcaoCode("AAL")
                .flightNumber("AA123")
                .departureAirportIcao("KJFK")
                .arrivalAirportIcao("EGLL")
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .aircraftTypeIcao("B77W")
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
                    assert f.getAircraftTypeIcao().equals(flight.getAircraftTypeIcao());
                    assert f.getStatus().equals(flight.getStatus());
                });
    }

    @Test
    public void testGetAllFlights() {
        // Create a test flight
        LocalDateTime scheduledDeparture = LocalDateTime.now().plusDays(2);
        LocalDateTime scheduledArrival = scheduledDeparture.plusHours(12);

        Flight flight = Flight.builder()
                .airlineIcaoCode("BAW")
                .flightNumber("BA456")
                .departureAirportIcao("EGLL")
                .arrivalAirportIcao("RJTT")
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .aircraftTypeIcao("A388")
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

        Flight flight = Flight.builder()
                .airlineIcaoCode("DLH")
                .flightNumber("LH789")
                .departureAirportIcao("EDDF")
                .arrivalAirportIcao("KJFK")
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .aircraftTypeIcao("A320")
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

        Flight flight = Flight.builder()
                .airlineIcaoCode("UAE")
                .flightNumber("EK101")
                .departureAirportIcao("OMDB")
                .arrivalAirportIcao("YSSY")
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .aircraftTypeIcao("A388")
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
                .aircraftTypeIcao("A388")
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

        Flight flight = Flight.builder()
                .airlineIcaoCode("SIA")
                .flightNumber("SQ222")
                .departureAirportIcao("YSSY")
                .arrivalAirportIcao("RJTT")
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .aircraftTypeIcao("B77W")
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
