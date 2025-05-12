-- liquibase formatted sql

-- changeset execodex:05
CREATE TABLE passengers (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    passport_number VARCHAR(50) UNIQUE,
    nationality VARCHAR(100),
    email VARCHAR(255),
    phone VARCHAR(50)
);