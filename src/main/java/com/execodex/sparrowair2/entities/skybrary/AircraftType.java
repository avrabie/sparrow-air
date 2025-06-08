package com.execodex.sparrowair2.entities.skybrary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("aircraft_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AircraftType {

    @Id
    @Column(value = "icao_code")
    private String icaoCode;

    private String name;
    private String manufacturer;
    private String bodyType;
    private String wingType;
    private String wingPosition;
    private String tailType;
    private String tailPosition;
    private String weightCategory;
    private String apc;
    private String typeCode;
    private String aerodromeReferenceCode;
    private String rffCategory;
    private String engineType;
    private int engineCount;
    private String enginePosition;
    private String landingGearType;
    private String[] variants;
    private double wingspan;
    private double length;
    private double height;
    private String[] engineModels;
    private double cruiseSpeedKts;
    private double cruiseSpeedMach;
    private int serviceCeiling;
    private int rangeNM;
}



