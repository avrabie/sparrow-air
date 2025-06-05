package com.execodex.sparrowair2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AircraftIcaoRequest {
    private List<String> icaoCodes; // List of ICAO type codes (e.g., ["B738", "A320", "B77W"])
}
