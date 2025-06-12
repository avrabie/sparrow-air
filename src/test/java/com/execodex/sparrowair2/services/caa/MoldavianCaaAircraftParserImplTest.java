package com.execodex.sparrowair2.services.caa;

import com.execodex.sparrowair2.entities.caa.AircraftRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class MoldavianCaaAircraftParserImplTest {

    private MoldavianCaaAircraftParser parser;

    @BeforeEach
    void setUp() {
        parser = new MoldavianCaaAircraftParserImpl();
    }

    @Test
    void testParseAircraftRegistrations() throws IOException {
        // Get the path to the PDF file
        Path pdfPath = new ClassPathResource("stuff/data/mcaa/Registrul_Aerian_al_Republicii_Moldova.pdf").getFile().toPath();

        // Parse the PDF
        Flux<AircraftRegistration> registrations = parser.parseAircraftRegistrations(pdfPath);

        // Verify that we get at least one registration
        StepVerifier.create(registrations.collectList())
                .expectNextMatches(list -> {
                    System.out.println("[DEBUG_LOG] Found " + list.size() + " aircraft registrations");
                    list.forEach(reg -> {
                        System.out.println("[DEBUG_LOG] Registration: " + reg);
                        System.out.println("[DEBUG_LOG]   - Registration Number: " + reg.getRegistrationNumber());
                        System.out.println("[DEBUG_LOG]   - Aircraft Type: " + reg.getAircraftType());
                        System.out.println("[DEBUG_LOG]   - MSN: " + reg.getMsn());
                        System.out.println("[DEBUG_LOG]   - Operator ICAO Code: " + reg.getOperatorIcaoCode());
                        System.out.println("[DEBUG_LOG]   - Owner Name: " + reg.getOwnerName());
                        System.out.println("[DEBUG_LOG]   - Owner Address: " + reg.getOwnerAddress());
                        System.out.println("[DEBUG_LOG]   - Country: " + reg.getCountryOfRegistration());
                        System.out.println("[DEBUG_LOG]   - Status: " + reg.getStatus());
                        System.out.println("[DEBUG_LOG]   - Date of Registration: " + reg.getDateOfRegistration());
                    });
                    return !list.isEmpty();
                })
                .verifyComplete();
    }
}
