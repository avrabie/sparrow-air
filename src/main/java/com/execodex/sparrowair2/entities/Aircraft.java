package com.execodex.sparrowair2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents an aircraft with detailed information extracted from HTML.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "aircraft")
public class Aircraft {
    @Id
    private String icaoCode;
    
    private String name;
    private String manufacturer;
    private String bodyType;
    private String wingType;
    private String wingPosition;
    private String tailType;
    private String weightCategory; // WTC
    private String aircraftPerformanceCategory; // APC
    private String typeCode;
    private String aerodromeReferenceCode;
    private String rffCategory;
    private String engineType;
    private String engineCount;
    private String enginePosition;
    private String landingGearType;
    
    // Technical Data
    private double wingspanMeters;
    private double lengthMeters;
    private double heightMeters;
    private String powerplant;
    private String[] engineModels;
    
    // Performance Data
    // Take-Off
    private int takeOffV2Kts;
    private int takeOffDistanceMeters;
    private int maxTakeOffWeightKg;
    
    // Initial Climb (to 5000 ft)
    private int initialClimbIasKts;
    private int initialClimbRocFtMin;
    
    // Initial Climb (to FL150)
    private int climbToFL150IasKts;
    private int climbToFL150RocFtMin;
    
    // Cruise
    private int cruiseIasKts;
    private double cruiseMach;
    private int cruiseAltitudeFt;
    
    // Service Ceiling
    private int serviceCeilingFt;
    
    // Range
    private int rangeNm;
    
    // Approach
    private int approachVrefKts;
    
    // Landing
    private int landingDistanceMeters;
    private int maxLandingWeightKg;
}