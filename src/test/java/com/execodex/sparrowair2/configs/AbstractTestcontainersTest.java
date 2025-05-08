package com.execodex.sparrowair2.configs;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for tests that need a PostgreSQL database.
 * Extend this class to use a PostgreSQL container for your tests.
 */
@Testcontainers
public abstract class AbstractTestcontainersTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    /**
     * Dynamically sets the Spring properties for the database connection.
     */
    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        // R2DBC properties
        registry.add("spring.r2dbc.url", () -> 
                String.format("r2dbc:postgresql://%s:%d/%s", 
                        postgres.getHost(),
                        postgres.getMappedPort(5432),
                        postgres.getDatabaseName()));
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);
        
        // Liquibase properties
        registry.add("spring.liquibase.url", () -> 
                String.format("jdbc:postgresql://%s:%d/%s", 
                        postgres.getHost(),
                        postgres.getMappedPort(5432),
                        postgres.getDatabaseName()));
        registry.add("spring.liquibase.user", postgres::getUsername);
        registry.add("spring.liquibase.password", postgres::getPassword);
    }
}