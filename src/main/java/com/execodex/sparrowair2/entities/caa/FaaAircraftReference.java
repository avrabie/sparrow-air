package com.execodex.sparrowair2.entities.caa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents an FAA aircraft reference record from a comma-delimited text file.
 * Based on the FAA aircraft reference database format.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "faa_aircraft_reference")
public class FaaAircraftReference {
    @Id
    private String code; // Positions 1-7: Aircraft Manufacturer, Model and Series Code
                         // Positions (1-3) - Manufacturer Code
                         // Positions (4-5) - Model Code
                         // Positions (6-7) - Series Code

    private String manufacturerName; // Positions 9-38: Name of the aircraft manufacturer
    private String modelName; // Positions 40-59: Name of the aircraft model and series

    private String typeAircraft; // Position 61: Type of aircraft (1-Glider, 2-Balloon, etc.)
    private String typeEngine; // Positions 63-64: Type of engine (0-None, 1-Reciprocating, etc.)
    private String aircraftCategoryCode; // Position 66: Aircraft Category Code (1-Land, 2-Sea, 3-Amphibian)
    private String builderCertificationCode; // Position 68: Builder Certification Code (0-Type Certificated, etc.)

    private String numberOfEngines; // Positions 70-71: Number of engines on the aircraft
    private String numberOfSeats; // Positions 73-75: Maximum number of seats in the aircraft

    private String aircraftWeight; // Positions 77-83: Aircraft maximum gross take off weight in pounds
    private String aircraftCruisingSpeed; // Positions 85-88: Aircraft average cruising speed in miles per hour

    private String tcDataSheet; // Positions 90-105: Type Certificate Data Sheet
    private String tcDataHolder; // Positions 107-157: Type Certificate Data Holder

    /**
     * Parses a line from the FAA aircraft reference comma-delimited text file.
     *
     * @param line The line to parse
     * @return A new FaaAircraftReference object
     */
    public static FaaAircraftReference parseFromCsvLine(String line) {
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Line is empty or null");
        }

        String[] parts = line.split(",", -1);
        if (parts.length < 13) {
            throw new IllegalArgumentException("Line does not contain enough fields");
        }

        // Map the columns based on the header in the sample file:
        // CODE,MFR,MODEL,TYPE-ACFT,TYPE-ENG,AC-CAT,BUILD-CERT-IND,NO-ENG,NO-SEATS,AC-WEIGHT,SPEED,TC-DATA-SHEET,TC-DATA-HOLDER
        return FaaAircraftReference.builder()
                .code(parts[0].trim())                      // CODE
                .manufacturerName(parts[1].trim())          // MFR
                .modelName(parts[2].trim())                 // MODEL
                .typeAircraft(parts[3].trim())              // TYPE-ACFT
                .typeEngine(parts[4].trim())                // TYPE-ENG
                .aircraftCategoryCode(parts[5].trim())      // AC-CAT
                .builderCertificationCode(parts[6].trim())  // BUILD-CERT-IND
                .numberOfEngines(parts[7].trim())           // NO-ENG
                .numberOfSeats(parts[8].trim())             // NO-SEATS
                .aircraftWeight(parts[9].trim())            // AC-WEIGHT
                .aircraftCruisingSpeed(parts[10].trim())    // SPEED
                .tcDataSheet(parts[11].trim())              // TC-DATA-SHEET
                .tcDataHolder(parts[12].trim())             // TC-DATA-HOLDER
                .build();
    }

    /**
     * Returns the manufacturer code part of the code field.
     * 
     * @return The manufacturer code (first 3 characters of the code field)
     */
    public String getManufacturerCode() {
        if (code == null || code.length() < 3) {
            return "";
        }
        return code.substring(0, 3);
    }

    /**
     * Returns the model code part of the code field.
     * 
     * @return The model code (characters 4-5 of the code field)
     */
    public String getModelCode() {
        if (code == null || code.length() < 5) {
            return "";
        }
        return code.substring(3, 5);
    }

    /**
     * Returns the series code part of the code field.
     * 
     * @return The series code (characters 6-7 of the code field)
     */
    public String getSeriesCode() {
        if (code == null || code.length() < 7) {
            return "";
        }
        return code.substring(5, 7);
    }
}
