package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.configs.AbstractTestcontainersTest;
import com.execodex.sparrowair2.entities.Airline;
import com.execodex.sparrowair2.repositories.AirlineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AirlineRoutesTest extends AbstractTestcontainersTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AirlineRepository airlineRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repository before each test
        airlineRepository.deleteAll().block();
    }

    @Test
    public void testCreateAirline() {
        Airline airline = Airline.builder()
                .icaoCode("AAL")
                .name("American Airlines")
                .headquarters("Fort Worth, Texas, United States")
                .contactNumber("+1-800-433-7300")
                .website("https://www.aa.com")
                .build();

        webTestClient.post()
                .uri("/airlines")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(airline), Airline.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Airline.class)
                .isEqualTo(airline);
    }

    @Test
    public void testGetAllAirlines() {
        // Create a test airline
        Airline airline = Airline.builder()
                .icaoCode("BAW")
                .name("British Airways")
                .headquarters("London, United Kingdom")
                .contactNumber("+44-20-8738-5050")
                .website("https://www.britishairways.com")
                .build();

        // Save the airline and wait for it to complete
        Airline savedAirline = airlineRepository.insert(airline).block();

        webTestClient.get()
                .uri("/airlines")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Airline.class)
                .contains(airline);
    }

    @Test
    public void testGetAllAirlinesXml() {
        // Create a test airline
        Airline airline = Airline.builder()
                .icaoCode("DLH")
                .name("Lufthansa")
                .headquarters("Cologne, Germany")
                .contactNumber("+49-69-86-799-799")
                .website("https://www.lufthansa.com")
                .build();

        // Save the airline and wait for it to complete
        Airline savedAirline = airlineRepository.insert(airline).block();

        webTestClient.get()
                .uri("/airlines")
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Airline.class)
                .contains(airline);
    }

    @Test
    public void testGetAirlineByIcaoCode() {
        // Create a test airline
        Airline airline = Airline.builder()
                .icaoCode("UAE")
                .name("Emirates")
                .headquarters("Dubai, United Arab Emirates")
                .contactNumber("+971-600-555-555")
                .website("https://www.emirates.com")
                .build();

        // Save the airline and wait for it to complete
        Airline savedAirline = airlineRepository.insert(airline).block();

        webTestClient.get()
                .uri("/airlines/{icaoCode}", "UAE")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Airline.class)
                .isEqualTo(airline);
    }

    @Test
    public void testUpdateAirline() {
        // Create a test airline
        Airline airline = Airline.builder()
                .icaoCode("SIA")
                .name("Singapore Airlines")
                .headquarters("Singapore")
                .contactNumber("+65-6223-8888")
                .website("https://www.singaporeair.com")
                .build();

        // Save the airline and wait for it to complete
        Airline savedAirline = airlineRepository.insert(airline).block();

        // Update the airline
        Airline updatedAirline = Airline.builder()
                .icaoCode("SIA") // Same ICAO code
                .name("Singapore Airlines")
                .headquarters("Singapore")
                .contactNumber("+65-6223-9999") // Updated contact number
                .website("https://www.singaporeair.com")
                .build();

        webTestClient.put()
                .uri("/airlines/{icaoCode}", "SIA")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedAirline), Airline.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Airline.class)
                .isEqualTo(updatedAirline);
    }

    @Test
    public void testDeleteAirline() {
        // Create a test airline
        Airline airline = Airline.builder()
                .icaoCode("QFA")
                .name("Qantas")
                .headquarters("Sydney, Australia")
                .contactNumber("+61-2-9691-3636")
                .website("https://www.qantas.com")
                .build();

        // Save the airline and wait for it to complete
        Airline savedAirline = airlineRepository.insert(airline).block();

        webTestClient.delete()
                .uri("/airlines/{icaoCode}", "QFA")
                .exchange()
                .expectStatus().isNoContent();

        // Verify the airline was deleted
        webTestClient.get()
                .uri("/airlines/{icaoCode}", "QFA")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}