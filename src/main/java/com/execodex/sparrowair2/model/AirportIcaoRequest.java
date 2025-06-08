package com.execodex.sparrowair2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportIcaoRequest {
    private List<String> icaoCodes; // List of ICAO codes (e.g., ["KJFK", "EGLL", "OMDB"])
}
