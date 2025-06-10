package com.execodex.sparrowair2.entities.skybrary;

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
@Table(name = "airports_new")
public class AirportNew {
    @Id
    private String icaoCode;
    private String iataCode;
    private String name;
    private String icaoRegion;
    private String icaoTerritory;
    private String location;
    private String city;
    private String country;
    private String elevation;
    private double latitude;
    private double longitude;
    private String KCCode; // KC Code (e.g., "KLA3")
    private String airportBS;
    private String airportLOS;
    private String airportRE;



}
