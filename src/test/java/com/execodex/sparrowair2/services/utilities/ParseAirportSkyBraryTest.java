package com.execodex.sparrowair2.services.utilities;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParseAirportSkyBraryTest {

    // Create a test instance with null WebClient.Builder since we don't need it for this test
    private ParseAirportSkyBrary parseAirportSkyBrary = new ParseAirportSkyBrary(null);

    @Test
    public void testParseDMSToDecimal() throws Exception {
        // Get the private method using reflection
        Method parseDMSToDecimalMethod = ParseAirportSkyBrary.class.getDeclaredMethod(
                "parseDMSToDecimal", String.class, boolean.class);
        parseDMSToDecimalMethod.setAccessible(true);

        // Test latitude parsing
        // North latitude (positive)
        double northLat = (double) parseDMSToDecimalMethod.invoke(
                parseAirportSkyBrary, "51° 28' 39\" N", true);
        assertEquals(51.4775, northLat, 0.0001);

        // South latitude (negative)
        double southLat = (double) parseDMSToDecimalMethod.invoke(
                parseAirportSkyBrary, "33° 56' 24\" S", true);
        assertEquals(-33.94, southLat, 0.0001);

        // Test longitude parsing
        // East longitude (positive)
        double eastLon = (double) parseDMSToDecimalMethod.invoke(
                parseAirportSkyBrary, "2° 45' 10\" E", false);
        assertEquals(2.7528, eastLon, 0.0001);

        // West longitude (negative)
        double westLon = (double) parseDMSToDecimalMethod.invoke(
                parseAirportSkyBrary, "118° 24' 48\" W", false);
        assertEquals(-118.4133, westLon, 0.0001);

        System.out.println("[DEBUG_LOG] North latitude: " + northLat);
        System.out.println("[DEBUG_LOG] South latitude: " + southLat);
        System.out.println("[DEBUG_LOG] East longitude: " + eastLon);
        System.out.println("[DEBUG_LOG] West longitude: " + westLon);
    }
}
