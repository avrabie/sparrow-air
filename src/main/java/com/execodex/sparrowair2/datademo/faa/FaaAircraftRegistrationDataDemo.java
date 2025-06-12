package com.execodex.sparrowair2.datademo.faa;

import com.execodex.sparrowair2.entities.caa.FaaAircraftRegistration;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * This class demonstrates reading of FAA aircraft registration data from a CSV file.
 * It returns a Flux of FaaAircraftRegistration objects.
 */
public class FaaAircraftRegistrationDataDemo {

    /**
     * Get FAA aircraft registrations from file
     * @param path Path to the CSV file
     * @return Flux of FaaAircraftRegistration objects
     */
    public static Flux<FaaAircraftRegistration> getFaaAircraftRegistrationsFromFile(String path) {
        return Flux.defer(() -> {
            try {
                System.out.println("Reading FAA aircraft registration data from: " + path);
                
                // Get the file from the classpath
                ClassPathResource resource = new ClassPathResource(path);
                
                // Create a BufferedReader to read the file line by line
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
                );
                
                // Skip the header row
                boolean[] headerSkipped = {false};
                
                // Return a Flux that reads each line and converts it to a FaaAircraftRegistration object
                return Flux.fromStream(reader.lines())
                    .doFinally(signalType -> {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            System.err.println("Error closing reader: " + e.getMessage());
                        }
                    })
                    .filter(line -> {
                        if (!headerSkipped[0]) {
                            headerSkipped[0] = true;
                            System.out.println("Skipping header row: " + line);
                            return false;
                        }
                        return true;
                    })
                    .map(line -> {
                        try {
                            return parseAircraftRegistrationFromCsvLine(line);
                        } catch (Exception e) {
                            System.err.println("Error parsing CSV line: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(registration -> registration != null);
            } catch (Exception e) {
                System.err.println("Error reading aircraft registration data: " + e.getMessage());
                return Flux.empty();
            }
        });
    }
    
    /**
     * Get FAA aircraft registrations from file with a specified buffer size (for compatibility)
     * @param path Path to the CSV file
     * @param bufferSize Buffer size (ignored in this implementation)
     * @return Flux of FaaAircraftRegistration objects
     */
    public static Flux<FaaAircraftRegistration> getFaaAircraftRegistrationsFromFile(String path, int bufferSize) {
        // Buffer size is ignored in this implementation
        return getFaaAircraftRegistrationsFromFile(path);
    }

    /**
     * Parse a CSV line into a FaaAircraftRegistration object
     * @param line CSV line to parse
     * @return FaaAircraftRegistration object
     */
    private static FaaAircraftRegistration parseAircraftRegistrationFromCsvLine(String line) {
        // Split the CSV line by comma
        // This is a simple implementation and doesn't handle quoted fields with commas
        String[] parts = line.split(",");

        if (parts.length < 30) {
            throw new IllegalArgumentException("CSV line does not have enough fields: " + line);
        }

        // Create a new instance
        FaaAircraftRegistration registration = new FaaAircraftRegistration();
        registration.setNNumber(normalizeField(parts[0]));
        registration.setSerialNumber(normalizeField(parts[1]));
        registration.setAircraftMfrModelCode(normalizeField(parts[2]));
        registration.setEngineMfrModelCode(normalizeField(parts[3]));
        registration.setYearManufactured(normalizeField(parts[4]));
        registration.setTypeRegistrant(normalizeField(parts[5]));
        registration.setRegistrantName(normalizeField(parts[6]));
        registration.setStreet1(normalizeField(parts[7]));
        registration.setStreet2(normalizeField(parts[8]));
        registration.setRegistrantCity(normalizeField(parts[9]));
        registration.setRegistrantState(normalizeField(parts[10]));
        registration.setRegistrantZipCode(normalizeField(parts[11]));
        registration.setRegistrantRegion(normalizeField(parts[12]));
        registration.setCountyMail(normalizeField(parts[13]));
        registration.setCountryMail(normalizeField(parts[14]));
        registration.setLastActivityDate(normalizeField(parts[15]));
        registration.setCertificateIssueDate(normalizeField(parts[16]));
        registration.setAirworthinessClassificationCode(normalizeField(parts[17]));
        registration.setApprovedOperationCodes(normalizeField(parts[18]));
        registration.setTypeAircraft(normalizeField(parts[19]));
        registration.setTypeEngine(normalizeField(parts[20]));
        registration.setStatusCode(normalizeField(parts[21]));
        registration.setModeSCode(normalizeField(parts[22]));
        registration.setFractionalOwnership(normalizeField(parts[23]));
        registration.setAirworthinessDate(normalizeField(parts[24]));
        registration.setOtherName1(normalizeField(parts[25]));
        registration.setOtherName2(normalizeField(parts[26]));
        registration.setOtherName3(normalizeField(parts[27]));
        registration.setOtherName4(normalizeField(parts[28]));
        registration.setOtherName5(normalizeField(parts[29]));
        
        if (parts.length > 30) registration.setExpirationDate(normalizeField(parts[30]));
        if (parts.length > 31) registration.setUniqueId(normalizeField(parts[31]));
        if (parts.length > 32) registration.setKitMfr(normalizeField(parts[32]));
        if (parts.length > 33) registration.setKitModel(normalizeField(parts[33]));
        if (parts.length > 34) registration.setModeScodeHex(normalizeField(parts[34]));
        
        return registration;
    }

    /**
     * Normalize a field value by converting empty strings to null and trimming
     * @param value Field value to normalize
     * @return Normalized field value
     */
    private static String normalizeField(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}