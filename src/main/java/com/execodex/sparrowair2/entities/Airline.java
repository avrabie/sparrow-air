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
@Table(name = "airlines")
public class Airline {
    @Id
    @Column(value = "icao_code")
    private String icaoCode; // ICAO 3-letter code (e.g., "AAL")

    private String name;
    private String headquarters;
    private String contactNumber;
    private String website;



    // Getters, setters, constructors
}
