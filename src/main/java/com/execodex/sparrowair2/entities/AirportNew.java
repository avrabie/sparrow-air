package com.execodex.sparrowair2.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportNew {
    private String icaoCode;
    private String iataCode;
    private String name;
    private String icaoRegion;
    private String icaoTerritory;
    private String location;
    private String city;
    private String country;
    private String elevation;
    private String latitude;
    private String longitude;
    private String KCCode; // KC Code (e.g., "KLA3")
    private String airportBS;
    private String airportLOS;
    private String airportRE;



}
