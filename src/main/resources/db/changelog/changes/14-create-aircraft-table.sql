-- liquibase formatted sql

-- changeset execodex:14
CREATE TABLE aircraft (
    icao_code VARCHAR(10) PRIMARY KEY NOT NULL,
    name VARCHAR(255),
    manufacturer VARCHAR(255),
    body_type VARCHAR(50),
    wing_type VARCHAR(50),
    wing_position VARCHAR(50),
    tail_type VARCHAR(50),
    weight_category VARCHAR(50),
    aircraft_performance_category VARCHAR(50),
    type_code VARCHAR(50),
    aerodrome_reference_code VARCHAR(50),
    rff_category VARCHAR(50),
    engine_type VARCHAR(50),
    engine_count VARCHAR(50),
    engine_position VARCHAR(50),
    landing_gear_type VARCHAR(50),

    -- Technical Data
    wingspan_meters DOUBLE PRECISION,
    length_meters DOUBLE PRECISION,
    height_meters DOUBLE PRECISION,
    powerplant VARCHAR(255),
    engine_models VARCHAR(1000), -- Comma-separated list of engine models

    -- Performance Data
    -- Take-Off
    take_off_v2_kts INT,
    take_off_distance_meters INT,
    max_take_off_weight_kg INT,

    -- Initial Climb (to 5000 ft)
    initial_climb_ias_kts INT,
    initial_climb_roc_ft_min INT,

    -- Initial Climb (to FL150)
    climb_to_fl150_ias_kts INT,
    climb_to_fl150_roc_ft_min INT,

    -- Cruise
    cruise_ias_kts INT,
    cruise_mach DOUBLE PRECISION,
    cruise_altitude_ft INT,

    -- Service Ceiling
    service_ceiling_ft INT,

    -- Range
    range_nm INT,

    -- Approach
    approach_vref_kts INT,

    -- Landing
    landing_distance_meters INT,
    max_landing_weight_kg INT
);
