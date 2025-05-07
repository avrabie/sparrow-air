package com.execodex.sparrowair2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "aircraft_types")
public class AircraftType {
    @Id
    @Column(value = "icao_code")
    private String icaoCode; // ICAO type code (e.g., "B738")

    private String modelName;
    private String manufacturer;
    private int seatingCapacity;
    private int maxRangeKm;

    // Getters, setters, constructors
}
