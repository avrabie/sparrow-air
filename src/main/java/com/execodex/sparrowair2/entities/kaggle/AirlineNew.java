package com.execodex.sparrowair2.entities.kaggle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO representing airline data from the airlines.csv file.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirlineNew {
    private Integer airlineId;
    private String name;
    private String alias;
    private String iata;
    private String icao;
    private String callsign;
    private String country;
    private String active;
}