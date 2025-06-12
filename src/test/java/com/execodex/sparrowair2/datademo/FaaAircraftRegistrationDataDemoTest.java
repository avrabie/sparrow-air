package com.execodex.sparrowair2.datademo;

import com.execodex.sparrowair2.datademo.faa.FaaAircraftRegistrationDataDemo;
import com.execodex.sparrowair2.entities.caa.FaaAircraftRegistration;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for FaaAircraftRegistrationDataDemo.
 */
public class FaaAircraftRegistrationDataDemoTest {

    @Test
    public void testGetFaaAircraftRegistrationsFromFile() {
        // Path to the test data file
        String dataPath = "stuff/data/mcaa/master-100.txt";

        // Get the Flux of FaaAircraftRegistration objects
        Flux<FaaAircraftRegistration> registrationsFlux = FaaAircraftRegistrationDataDemo.getFaaAircraftRegistrationsFromFile(dataPath);

        // Collect all registrations
        List<FaaAircraftRegistration> registrations = registrationsFlux.collectList().block();

        // Verify that we got some registrations
        assertNotNull(registrations);
        System.out.println("Total registrations: " + registrations.size());

        // Verify that we have registrations (should be 100 data rows in the file)
        assertTrue(registrations.size() > 0, "Should have at least one registration");

        // Print some sample data for manual verification
        System.out.println("\nSample registrations:");
        for (int i = 0; i < Math.min(5, registrations.size()); i++) {
            FaaAircraftRegistration registration = registrations.get(i);
            System.out.println("N-Number: " + registration.getNNumber());
            System.out.println("Serial Number: " + registration.getSerialNumber());
            System.out.println("Registrant Name: " + registration.getRegistrantName());
            System.out.println("Aircraft Mfr Model Code: " + registration.getAircraftMfrModelCode());
            System.out.println("Year Manufactured: " + registration.getYearManufactured());
            System.out.println("------------------------------");
        }
    }

}
