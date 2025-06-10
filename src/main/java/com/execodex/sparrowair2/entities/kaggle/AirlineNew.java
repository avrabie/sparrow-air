package com.execodex.sparrowair2.entities.kaggle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * POJO representing airline data from the airlines.csv file.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "airlines_new")
public class AirlineNew {
    @Id
    private Integer airlineId;
    private String name;
    private String alias;
    private String iata;
    @Column("icao_code")
    private String icaoCode;
    private String callsign;
    private String country;
    private String active;
}