package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.configs.AbstractTestcontainersTest;
import com.execodex.sparrowair2.entities.AircraftType;
import com.execodex.sparrowair2.entities.Airline;
import com.execodex.sparrowair2.entities.AirlineFleet;
import com.execodex.sparrowair2.repositories.AircraftTypeRepository;
import com.execodex.sparrowair2.repositories.AirlineFleetRepository;
import com.execodex.sparrowair2.repositories.AirlineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AirlineFleetTest extends AbstractTestcontainersTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AirlineFleetRepository airlineFleetRepository;

    @Autowired
    private AircraftTypeRepository aircraftTypeRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repositories before each test
        airlineFleetRepository.deleteAll().block();

        // Insert required aircraft types
        AircraftType b738 = AircraftType.builder()
                .icaoCode("B738")
                .modelName("737-800")
                .manufacturer("Boeing")
                .maxRangeKm(5765)
                .mtow(79010)
                .build();

        AircraftType a320 = AircraftType.builder()
                .icaoCode("A320")
                .modelName("A320-200")
                .manufacturer("Airbus")
                .maxRangeKm(6100)
                .mtow(77000)
                .build();

        AircraftType b77w = AircraftType.builder()
                .icaoCode("B77W")
                .modelName("777-300ER")
                .manufacturer("Boeing")
                .maxRangeKm(13650)
                .mtow(351500)
                .build();

        AircraftType a388 = AircraftType.builder()
                .icaoCode("A388")
                .modelName("A380-800")
                .manufacturer("Airbus")
                .maxRangeKm(15700)
                .mtow(575000)
                .build();

        AircraftType e190 = AircraftType.builder()
                .icaoCode("E190")
                .modelName("E190")
                .manufacturer("Embraer")
                .maxRangeKm(4537)
                .mtow(51800)
                .build();

        // Insert required airlines
        Airline aal = Airline.builder()
                .icaoCode("AAL")
                .name("American Airlines")
                .headquarters("Fort Worth, Texas, United States")
                .contactNumber("+1-800-433-7300")
                .website("https://www.aa.com")
                .build();

        Airline baw = Airline.builder()
                .icaoCode("BAW")
                .name("British Airways")
                .headquarters("London, United Kingdom")
                .contactNumber("+44-20-8738-5050")
                .website("https://www.britishairways.com")
                .build();

        Airline dlh = Airline.builder()
                .icaoCode("DLH")
                .name("Lufthansa")
                .headquarters("Cologne, Germany")
                .contactNumber("+49-69-86-799-799")
                .website("https://www.lufthansa.com")
                .build();

        Airline uae = Airline.builder()
                .icaoCode("UAE")
                .name("Emirates")
                .headquarters("Dubai, United Arab Emirates")
                .contactNumber("+971-600-555-555")
                .website("https://www.emirates.com")
                .build();

        Airline wzz = Airline.builder()
                .icaoCode("WZZ")
                .name("Wizz Air")
                .headquarters("Budapest, Hungary")
                .contactNumber("+36-1-777-9300")
                .website("https://wizzair.com")
                .build();

        // Save aircraft types and airlines
        aircraftTypeRepository.deleteAll().block();
        airlineRepository.deleteAll().block();

        aircraftTypeRepository.insert(b738).block();
        aircraftTypeRepository.insert(a320).block();
        aircraftTypeRepository.insert(b77w).block();
        aircraftTypeRepository.insert(a388).block();
        aircraftTypeRepository.insert(e190).block();

        airlineRepository.insert(aal).block();
        airlineRepository.insert(baw).block();
        airlineRepository.insert(dlh).block();
        airlineRepository.insert(uae).block();
        airlineRepository.insert(wzz).block();
    }

    @Test
    public void testCreateAirlineFleet() {
        AirlineFleet airlineFleet = AirlineFleet.builder()
                .aircraftTypeIcao("B738")
                .airlineIcao("AAL")
                .registrationNumber("UA 12345")
                .aircraftAge(LocalDate.of(2015, 5, 12))
                .seatConfiguration("3-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(8)
                .businessSeats(24)
                .economySeats(126)
                .build();

        webTestClient.post()
                .uri("/airline-fleet")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(airlineFleet), AirlineFleet.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AirlineFleet.class)
                .value(response -> {
                    assert response.getId() != null;
                    assert response.getAircraftTypeIcao().equals(airlineFleet.getAircraftTypeIcao());
                    assert response.getAirlineIcao().equals(airlineFleet.getAirlineIcao());
                    assert response.getAircraftAge().equals(airlineFleet.getAircraftAge());
                    assert response.getSeatConfiguration().equals(airlineFleet.getSeatConfiguration());
                    assert response.getHasWifi().equals(airlineFleet.getHasWifi());
                    assert response.getHasPowerOutlets().equals(airlineFleet.getHasPowerOutlets());
                    assert response.getHasEntertainmentSystem().equals(airlineFleet.getHasEntertainmentSystem());
                    assert response.getFirstClassSeats().equals(airlineFleet.getFirstClassSeats());
                    assert response.getBusinessSeats().equals(airlineFleet.getBusinessSeats());
                    assert response.getEconomySeats().equals(airlineFleet.getEconomySeats());
                });
    }

    @Test
    public void testGetAllAirlineFleet() {
        // Create a test airline fleet entry
        AirlineFleet airlineFleet = AirlineFleet.builder()
                .aircraftTypeIcao("A320")
                .airlineIcao("BAW")
                .registrationNumber("UA 12345")
                .aircraftAge(LocalDate.of(2018, 3, 24))
                .seatConfiguration("3-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(false)
                .firstClassSeats(0)
                .businessSeats(32)
                .economySeats(120)
                .build();

        // Save the airline fleet entry and wait for it to complete
        AirlineFleet savedAirlineFleet = airlineFleetRepository.insert(airlineFleet).block();

        webTestClient.get()
                .uri("/airline-fleet")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AirlineFleet.class)
                .contains(savedAirlineFleet);
    }

    @Test
    public void testGetAirlineFleetById() {
        // Create a test airline fleet entry
        AirlineFleet airlineFleet = AirlineFleet.builder()
                .aircraftTypeIcao("B77W")
                .airlineIcao("DLH")
                .registrationNumber("D-ABCD")
                .aircraftAge(LocalDate.of(2012, 11, 7))
                .seatConfiguration("3-4-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(8)
                .businessSeats(42)
                .economySeats(304)
                .build();

        // Save the airline fleet entry and wait for it to complete
        AirlineFleet savedAirlineFleet = airlineFleetRepository.insert(airlineFleet).block();

        webTestClient.get()
                .uri("/airline-fleet/{id}", savedAirlineFleet.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AirlineFleet.class)
                .isEqualTo(savedAirlineFleet);
    }

    @Test
    public void testGetAirlineFleetByAirlineIcao() {
        // Create test airline fleet entries
        AirlineFleet airlineFleet1 = AirlineFleet.builder()
                .aircraftTypeIcao("A388")
                .airlineIcao("UAE")
                .registrationNumber("A6-EDB")
                .aircraftAge(LocalDate.of(2010, 8, 15))
                .seatConfiguration("3-4-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(14)
                .businessSeats(76)
                .economySeats(427)
                .build();

        AirlineFleet airlineFleet2 = AirlineFleet.builder()
                .aircraftTypeIcao("B77W")
                .airlineIcao("UAE")
                .registrationNumber("A6-EDC")
                .aircraftAge(LocalDate.of(2013, 5, 20))
                .seatConfiguration("3-4-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(8)
                .businessSeats(42)
                .economySeats(304)
                .build();

        // Save the airline fleet entries and wait for them to complete
        AirlineFleet savedAirlineFleet1 = airlineFleetRepository.insert(airlineFleet1).block();
        AirlineFleet savedAirlineFleet2 = airlineFleetRepository.insert(airlineFleet2).block();

        webTestClient.get()
                .uri("/airline-fleet/airline/{airlineIcao}", "UAE")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AirlineFleet.class)
                .contains(savedAirlineFleet1, savedAirlineFleet2);
    }

    @Test
    public void testUpdateAirlineFleet() {
        // Create a test airline fleet entry
        AirlineFleet airlineFleet = AirlineFleet.builder()
                .aircraftTypeIcao("E190")
                .airlineIcao("WZZ")
                .registrationNumber("HA-LYB")
                .aircraftAge(LocalDate.of(2019, 2, 3))
                .seatConfiguration("2-2")
                .hasWifi(false)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(false)
                .firstClassSeats(0)
                .businessSeats(12)
                .economySeats(88)
                .build();

        // Save the airline fleet entry and wait for it to complete
        AirlineFleet savedAirlineFleet = airlineFleetRepository.insert(airlineFleet).block();

        // Update the airline fleet entry
        AirlineFleet updatedAirlineFleet = AirlineFleet.builder()
                .id(savedAirlineFleet.getId())
                .aircraftTypeIcao("E190")
                .airlineIcao("WZZ")
                .registrationNumber("HA-LYB")
                .aircraftAge(LocalDate.of(2019, 2, 3))
                .seatConfiguration("2-2")
                .hasWifi(true) // Updated WiFi availability
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(false)
                .firstClassSeats(0)
                .businessSeats(12)
                .economySeats(88)
                .build();

        webTestClient.put()
                .uri("/airline-fleet/{id}", savedAirlineFleet.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedAirlineFleet), AirlineFleet.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AirlineFleet.class)
                .isEqualTo(updatedAirlineFleet);
    }

    @Test
    public void testDeleteAirlineFleet() {
        // Create a test airline fleet entry
        AirlineFleet airlineFleet = AirlineFleet.builder()
                .aircraftTypeIcao("B738")
                .airlineIcao("AAL")
                .registrationNumber("N12345")
                .aircraftAge(LocalDate.of(2015, 5, 12))
                .seatConfiguration("3-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(8)
                .businessSeats(24)
                .economySeats(126)
                .build();

        // Save the airline fleet entry and wait for it to complete
        AirlineFleet savedAirlineFleet = airlineFleetRepository.insert(airlineFleet).block();

        webTestClient.delete()
                .uri("/airline-fleet/{id}", savedAirlineFleet.getId())
                .exchange()
                .expectStatus().isNoContent();

        // Verify the airline fleet entry was deleted
        webTestClient.get()
                .uri("/airline-fleet/{id}", savedAirlineFleet.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testGetAirlineFleetByRegistration() {
        // Create a test airline fleet entry with a unique registration number
        AirlineFleet airlineFleet = AirlineFleet.builder()
                .aircraftTypeIcao("A320")
                .airlineIcao("BAW")
                .registrationNumber("G-ABCD")
                .aircraftAge(LocalDate.of(2017, 6, 15))
                .seatConfiguration("3-3")
                .hasWifi(true)
                .hasPowerOutlets(true)
                .hasEntertainmentSystem(true)
                .firstClassSeats(0)
                .businessSeats(32)
                .economySeats(148)
                .build();

        // Save the airline fleet entry and wait for it to complete
        AirlineFleet savedAirlineFleet = airlineFleetRepository.insert(airlineFleet).block();

        // Test the GET /registration/{registration} endpoint
        webTestClient.get()
                .uri("/airline-fleet/registration/{registration}", "G-ABCD")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AirlineFleet.class)
                .isEqualTo(savedAirlineFleet);
    }
}
