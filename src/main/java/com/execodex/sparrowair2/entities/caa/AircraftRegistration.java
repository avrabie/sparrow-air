package com.execodex.sparrowair2.entities.caa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "aircraft_registration")
public class AircraftRegistration {
    @Id
    private String registrationNumber; // e.g., "G-ABCD", also called registration mark
    private String aircraftType; // e.g., "Boeing 737"
    private String operatorIcaoCode; // e.g., "Boeing"
    private String msn; // Manufacturer Serial Number, e.g., "12345", also called serial number
    private String ownerName; // e.g., "British Airways"
    private String ownerAddress; // e.g., "London, UK"
    private String countryOfRegistration; // e.g., "United Kingdom"
    private String status; // e.g., "Active", "De-registered"
    private String dateOfRegistration; // e.g., "2020-01-01"
}
