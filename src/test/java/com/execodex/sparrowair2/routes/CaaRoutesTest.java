package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.entities.caa.AircraftRegistration;
import com.execodex.sparrowair2.handlers.CaaRouteHandlers;
import com.execodex.sparrowair2.services.caa.MoldavianCaaAircraftParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CaaRoutesTest {

    private WebTestClient webTestClient;
    private MoldavianCaaAircraftParser mockParser;

    @BeforeEach
    void setUp() {
        // Create mock parser
        mockParser = Mockito.mock(MoldavianCaaAircraftParser.class);

        // Create sample data
        List<AircraftRegistration> sampleData = Arrays.asList(
                AircraftRegistration.builder()
                        .registrationNumber("ER-AAA")
                        .aircraftType("Boeing 737")
                        .msn("12345")
                        .ownerName("Sample Airline")
                        .countryOfRegistration("Moldova")
                        .build(),
                AircraftRegistration.builder()
                        .registrationNumber("ER-BBB")
                        .aircraftType("Airbus A320")
                        .msn("67890")
                        .ownerName("Another Airline")
                        .countryOfRegistration("Moldova")
                        .build()
        );

        // Configure mock to return sample data
        when(mockParser.parseAircraftRegistrations(any(Path.class)))
                .thenReturn(Flux.fromIterable(sampleData));

        // Create handler with mock parser
        CaaRouteHandlers handlers = new CaaRouteHandlers(mockParser);

        // Create routes with handler
        CaaRoutes routes = new CaaRoutes(handlers);

        // Create WebTestClient
        webTestClient = WebTestClient
                .bindToRouterFunction(routes.caaRoutesFunction())
                .build();
    }

    @Test
    void testGetMoldavianAircraftRegistrations() {
        webTestClient.get()
                .uri("/caa/moldova/aircraft-registrations")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .expectBodyList(AircraftRegistration.class)
                .hasSize(2)
                .contains(
                        AircraftRegistration.builder()
                                .registrationNumber("ER-AAA")
                                .aircraftType("Boeing 737")
                                .msn("12345")
                                .ownerName("Sample Airline")
                                .countryOfRegistration("Moldova")
                                .build(),
                        AircraftRegistration.builder()
                                .registrationNumber("ER-BBB")
                                .aircraftType("Airbus A320")
                                .msn("67890")
                                .ownerName("Another Airline")
                                .countryOfRegistration("Moldova")
                                .build()
                );
    }
}
