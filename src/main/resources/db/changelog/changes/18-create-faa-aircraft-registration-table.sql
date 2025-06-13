-- liquibase formatted sql

-- changeset execodex:18
CREATE TABLE faa_aircraft_registration (
    n_number VARCHAR(5) PRIMARY KEY NOT NULL,
    serial_number VARCHAR(30),
    aircraft_mfr_model_code VARCHAR(7),
    engine_mfr_model_code VARCHAR(5),
    year_manufactured VARCHAR(4),
    
    type_registrant VARCHAR(2),
    registrant_name VARCHAR(50),
    street1 VARCHAR(33),
    street2 VARCHAR(33),
    registrant_city VARCHAR(18),
    registrant_state VARCHAR(2),
    registrant_zip_code VARCHAR(10),
    registrant_region VARCHAR(1),
    
    county_mail VARCHAR(3),
    country_mail VARCHAR(2),
    
    last_activity_date VARCHAR(8),
    certificate_issue_date VARCHAR(8),
    
    airworthiness_classification_code VARCHAR(2),
    approved_operation_codes VARCHAR(9),
    
    type_aircraft VARCHAR(2),
    type_engine VARCHAR(2),
    
    status_code VARCHAR(2),
    
    mode_s_code VARCHAR(8),
    fractional_ownership VARCHAR(2),
    
    airworthiness_date VARCHAR(8),
    
    other_name1 VARCHAR(50),
    other_name2 VARCHAR(50),
    other_name3 VARCHAR(50),
    other_name4 VARCHAR(50),
    other_name5 VARCHAR(50),
    
    expiration_date VARCHAR(8),
    unique_id VARCHAR(8),
    
    kit_mfr VARCHAR(30),
    kit_model VARCHAR(20),
    
    mode_scode_hex VARCHAR(10)
);