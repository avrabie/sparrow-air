package com.execodex.sparrowair2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table(name = "airports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airport {
    @Id
    @Column(value = "icao_code")
    private String icaoCode; // ICAO 4-letter code (e.g., "KLAX")

    private String name;
    private String city;
    private String country;
    private String timezone;
    private double latitude;
    private double longitude;

    // Getters, setters, constructors
}
