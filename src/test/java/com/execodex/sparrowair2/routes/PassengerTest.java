package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.configs.AbstractTestcontainersTest;
import com.execodex.sparrowair2.entities.Passenger;
import com.execodex.sparrowair2.repositories.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PassengerTest extends AbstractTestcontainersTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PassengerRepository passengerRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repository before each test
        passengerRepository.deleteAll().block();
    }

    @Test
    public void testCreatePassenger() {
        Passenger passenger = Passenger.builder()
                .firstName("John")
                .lastName("Doe")
                .passportNumber("US123456789")
                .nationality("United States")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .build();

        webTestClient.post()
                .uri("/passengers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(passenger), Passenger.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Passenger.class)
                .value(p -> {
                    assert p.getId() != null;
                    assert p.getFirstName().equals(passenger.getFirstName());
                    assert p.getLastName().equals(passenger.getLastName());
                    assert p.getPassportNumber().equals(passenger.getPassportNumber());
                    assert p.getNationality().equals(passenger.getNationality());
                    assert p.getEmail().equals(passenger.getEmail());
                    assert p.getPhone().equals(passenger.getPhone());
                });
    }

    @Test
    public void testGetAllPassengers() {
        // Create a test passenger
        Passenger passenger = Passenger.builder()
                .firstName("Jane")
                .lastName("Smith")
                .passportNumber("UK987654321")
                .nationality("United Kingdom")
                .email("jane.smith@example.com")
                .phone("+44-20-1234-5678")
                .build();

        // Save the passenger and wait for it to complete
        Passenger savedPassenger = passengerRepository.insert(passenger).block();

        webTestClient.get()
                .uri("/passengers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Passenger.class)
                .value(passengers -> {
                    assert passengers.size() > 0;
                    Passenger p = passengers.get(0);
                    assert p.getId() != null;
                    assert p.getFirstName().equals(passenger.getFirstName());
                    assert p.getLastName().equals(passenger.getLastName());
                    assert p.getPassportNumber().equals(passenger.getPassportNumber());
                });
    }

    @Test
    public void testGetPassengerById() {
        // Create a test passenger
        Passenger passenger = Passenger.builder()
                .firstName("Hans")
                .lastName("Mueller")
                .passportNumber("DE456789123")
                .nationality("Germany")
                .email("hans.mueller@example.com")
                .phone("+49-30-1234-5678")
                .build();

        // Save the passenger and wait for it to complete
        Passenger savedPassenger = passengerRepository.insert(passenger).block();

        webTestClient.get()
                .uri("/passengers/{id}", savedPassenger.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Passenger.class)
                .value(p -> {
                    assert p.getId().equals(savedPassenger.getId());
                    assert p.getFirstName().equals(passenger.getFirstName());
                    assert p.getLastName().equals(passenger.getLastName());
                    assert p.getPassportNumber().equals(passenger.getPassportNumber());
                });
    }

    @Test
    public void testUpdatePassenger() {
        // Create a test passenger
        Passenger passenger = Passenger.builder()
                .firstName("Yuki")
                .lastName("Tanaka")
                .passportNumber("JP789123456")
                .nationality("Japan")
                .email("yuki.tanaka@example.com")
                .phone("+81-3-1234-5678")
                .build();

        // Save the passenger and wait for it to complete
        Passenger savedPassenger = passengerRepository.insert(passenger).block();

        // Update the passenger
        Passenger updatedPassenger = Passenger.builder()
                .id(savedPassenger.getId()) // Same ID
                .firstName("Yuki")
                .lastName("Tanaka")
                .passportNumber("JP789123456")
                .nationality("Japan")
                .email("yuki.tanaka.updated@example.com") // Updated email
                .phone("+81-3-1234-5678")
                .build();

        webTestClient.put()
                .uri("/passengers/{id}", savedPassenger.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedPassenger), Passenger.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Passenger.class)
                .value(p -> {
                    assert p.getId().equals(savedPassenger.getId());
                    assert p.getFirstName().equals(updatedPassenger.getFirstName());
                    assert p.getLastName().equals(updatedPassenger.getLastName());
                    assert p.getEmail().equals(updatedPassenger.getEmail());
                });
    }

    @Test
    public void testDeletePassenger() {
        // Create a test passenger
        Passenger passenger = Passenger.builder()
                .firstName("Maria")
                .lastName("Garcia")
                .passportNumber("ES321654987")
                .nationality("Spain")
                .email("maria.garcia@example.com")
                .phone("+34-91-1234-5678")
                .build();

        // Save the passenger and wait for it to complete
        Passenger savedPassenger = passengerRepository.insert(passenger).block();

        webTestClient.delete()
                .uri("/passengers/{id}", savedPassenger.getId())
                .exchange()
                .expectStatus().isNoContent();

        // Verify the passenger was deleted
        webTestClient.get()
                .uri("/passengers/{id}", savedPassenger.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}