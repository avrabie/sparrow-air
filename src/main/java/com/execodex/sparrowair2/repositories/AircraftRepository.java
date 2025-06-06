package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.Aircraft;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AircraftRepository extends ReactiveCrudRepository<Aircraft, String> {
    // The primary key of Aircraft is icaoCode, which is a String
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types

    @Query("INSERT INTO aircraft (icao_code, name, manufacturer, body_type, wing_type, wing_position, tail_type, " +
           "weight_category, aircraft_performance_category, type_code, aerodrome_reference_code, rff_category, " +
           "engine_type, engine_count, engine_position, landing_gear_type, wingspan_meters, length_meters, " +
           "height_meters, powerplant, engine_models, take_off_v2_kts, take_off_distance_meters, " +
           "max_take_off_weight_kg, initial_climb_ias_kts, initial_climb_roc_ft_min, climb_to_fl150_ias_kts, " +
           "climb_to_fl150_roc_ft_min, cruise_ias_kts, cruise_mach, cruise_altitude_ft, service_ceiling_ft, " +
           "range_nm, approach_vref_kts, landing_distance_meters, max_landing_weight_kg) " +
           "VALUES (:#{#aircraft.icaoCode}, :#{#aircraft.name}, :#{#aircraft.manufacturer}, :#{#aircraft.bodyType}, " +
           ":#{#aircraft.wingType}, :#{#aircraft.wingPosition}, :#{#aircraft.tailType}, :#{#aircraft.weightCategory}, " +
           ":#{#aircraft.aircraftPerformanceCategory}, :#{#aircraft.typeCode}, :#{#aircraft.aerodromeReferenceCode}, " +
           ":#{#aircraft.rffCategory}, :#{#aircraft.engineType}, :#{#aircraft.engineCount}, :#{#aircraft.enginePosition}, " +
           ":#{#aircraft.landingGearType}, :#{#aircraft.wingspanMeters}, :#{#aircraft.lengthMeters}, " +
           ":#{#aircraft.heightMeters}, :#{#aircraft.powerplant}, :#{#aircraft.engineModels}, :#{#aircraft.takeOffV2Kts}, " +
           ":#{#aircraft.takeOffDistanceMeters}, :#{#aircraft.maxTakeOffWeightKg}, :#{#aircraft.initialClimbIasKts}, " +
           ":#{#aircraft.initialClimbRocFtMin}, :#{#aircraft.climbToFL150IasKts}, :#{#aircraft.climbToFL150RocFtMin}, " +
           ":#{#aircraft.cruiseIasKts}, :#{#aircraft.cruiseMach}, :#{#aircraft.cruiseAltitudeFt}, " +
           ":#{#aircraft.serviceCeilingFt}, :#{#aircraft.rangeNm}, :#{#aircraft.approachVrefKts}, " +
           ":#{#aircraft.landingDistanceMeters}, :#{#aircraft.maxLandingWeightKg}) RETURNING *")
    Mono<Aircraft> insert(Aircraft aircraft);
}