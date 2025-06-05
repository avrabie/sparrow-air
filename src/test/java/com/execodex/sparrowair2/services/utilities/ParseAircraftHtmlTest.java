package com.execodex.sparrowair2.services.utilities;

import com.execodex.sparrowair2.entities.Aircraft;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ParseAircraftHtmlTest {

    @Test
    void checkHtmlStructure() throws IOException {
        // Get the test HTML file
        String filePath = new ClassPathResource("stuff/AIRBUS A-320 _ SKYbrary Aviation Safety.html").getFile().getAbsolutePath();
        File input = new File(filePath);
        Document doc = Jsoup.parse(input, "UTF-8");

        System.out.println("Checking HTML structure of test file: " + filePath);

        // Check cruise section
        Element cruiseSection = doc.selectFirst("div.group-cruise.perf-data");
        System.out.println("Cruise section found: " + (cruiseSection != null));

        // Check service ceiling section
        Element serviceCeilingSection = doc.selectFirst("div.group-service-ceiling.perf-data");
        System.out.println("Service ceiling section found: " + (serviceCeilingSection != null));

        // Check range section
        Element rangeSection = doc.selectFirst("div.group-range.perf-data");
        System.out.println("Range section found: " + (rangeSection != null));

        // Check approach section
        Element approachSection = doc.selectFirst("div.group-approach.perf-data");
        System.out.println("Approach section found: " + (approachSection != null));

        // Check landing section
        Element landingSection = doc.selectFirst("div.group-landing.perf-data");
        System.out.println("Landing section found: " + (landingSection != null));

        // Let's check for alternative selectors that might be used in the online version
        System.out.println("\nChecking alternative selectors:");

        // Check for cruise data with different selectors
        Element altCruiseSection = doc.selectFirst("div.field-name-field-performance-cruise");
        System.out.println("Alternative cruise section found: " + (altCruiseSection != null));
        if (altCruiseSection != null) {
            Element iasElement = altCruiseSection.selectFirst("div.field-name-field-performance-cruise-ias");
            Element machElement = altCruiseSection.selectFirst("div.field-name-field-performance-cruise-mach");
            Element altitudeElement = altCruiseSection.selectFirst("div.field-name-field-performance-cruise-alt");

            System.out.println("  Cruise IAS element found: " + (iasElement != null));
            System.out.println("  Cruise Mach element found: " + (machElement != null));
            System.out.println("  Cruise Altitude element found: " + (altitudeElement != null));

            if (iasElement != null) {
                Element iasItemElement = iasElement.selectFirst("div.field-item");
                if (iasItemElement != null) {
                    System.out.println("  Cruise IAS value: " + iasItemElement.text());
                }
            }

            if (machElement != null) {
                Element machItemElement = machElement.selectFirst("div.field-item");
                if (machItemElement != null) {
                    System.out.println("  Cruise Mach value: " + machItemElement.text());
                }
            }

            if (altitudeElement != null) {
                Element altitudeItemElement = altitudeElement.selectFirst("div.field-item");
                if (altitudeItemElement != null) {
                    System.out.println("  Cruise Altitude value: " + altitudeItemElement.text());
                }
            }
        }

        // Check for service ceiling data with different selectors
        Element altServiceCeilingSection = doc.selectFirst("div.field-name-field-performance-service-ceiling");
        System.out.println("Alternative service ceiling section found: " + (altServiceCeilingSection != null));
        if (altServiceCeilingSection != null) {
            Element ceilingItemElement = altServiceCeilingSection.selectFirst("div.field-item");
            if (ceilingItemElement != null) {
                System.out.println("  Service ceiling value: " + ceilingItemElement.text());
            }
        }

        // Check for range data with different selectors
        Element altRangeSection = doc.selectFirst("div.field-name-field-performance-range");
        System.out.println("Alternative range section found: " + (altRangeSection != null));
        if (altRangeSection != null) {
            Element rangeItemElement = altRangeSection.selectFirst("div.field-item");
            if (rangeItemElement != null) {
                System.out.println("  Range value: " + rangeItemElement.text());
            }
        }

        // Check for approach data with different selectors
        Element altApproachSection = doc.selectFirst("div.field-name-field-performance-app-vref");
        System.out.println("Alternative approach section found: " + (altApproachSection != null));
        if (altApproachSection != null) {
            Element vrefItemElement = altApproachSection.selectFirst("div.field-item");
            if (vrefItemElement != null) {
                System.out.println("  Approach VREF value: " + vrefItemElement.text());
            }
        }

        // Check for landing data with different selectors
        Element altLandingDistanceSection = doc.selectFirst("div.field-name-field-performance-ldg-dist");
        System.out.println("Alternative landing distance section found: " + (altLandingDistanceSection != null));
        if (altLandingDistanceSection != null) {
            Element distanceItemElement = altLandingDistanceSection.selectFirst("div.field-item");
            if (distanceItemElement != null) {
                System.out.println("  Landing distance value: " + distanceItemElement.text());
            }
        }

        Element altMaxLandingWeightSection = doc.selectFirst("div.field-name-field-performance-ldg-mlw");
        System.out.println("Alternative max landing weight section found: " + (altMaxLandingWeightSection != null));
        if (altMaxLandingWeightSection != null) {
            Element mlwItemElement = altMaxLandingWeightSection.selectFirst("div.field-item");
            if (mlwItemElement != null) {
                System.out.println("  Max landing weight value: " + mlwItemElement.text());
            }
        }
    }

    @Test
    void parseAircraftFromHtml() throws IOException {
        // Arrange
        ParseAircraftHtml parser = new ParseAircraftHtml(null);
        String filePath = new ClassPathResource("stuff/AIRBUS A-320 _ SKYbrary Aviation Safety.html").getFile().getAbsolutePath();

        // Act
        Aircraft aircraft = parser.parseAircraftFromHtml(filePath);

        // Assert
        System.out.println("[DEBUG_LOG] Parsed Aircraft: " + aircraft);
        System.out.println("[DEBUG_LOG] cruiseIasKts: " + aircraft.getCruiseIasKts());
        System.out.println("[DEBUG_LOG] cruiseMach: " + aircraft.getCruiseMach());
        System.out.println("[DEBUG_LOG] cruiseAltitudeFt: " + aircraft.getCruiseAltitudeFt());
        System.out.println("[DEBUG_LOG] serviceCeilingFt: " + aircraft.getServiceCeilingFt());
        System.out.println("[DEBUG_LOG] rangeNm: " + aircraft.getRangeNm());
        System.out.println("[DEBUG_LOG] approachVrefKts: " + aircraft.getApproachVrefKts());
        System.out.println("[DEBUG_LOG] landingDistanceMeters: " + aircraft.getLandingDistanceMeters());
        System.out.println("[DEBUG_LOG] maxLandingWeightKg: " + aircraft.getMaxLandingWeightKg());

        // Basic aircraft information
        assertEquals("A-320", aircraft.getName());
        assertEquals("AIRBUS", aircraft.getManufacturer());
        assertEquals("Narrow", aircraft.getBodyType());
        assertEquals("Fixed Wing", aircraft.getWingType());
        assertEquals("Low wing", aircraft.getWingPosition());
        assertEquals("Regular tail, mid set", aircraft.getTailType());
        assertEquals("M", aircraft.getWeightCategory());
        assertEquals("C", aircraft.getAircraftPerformanceCategory());
        assertEquals("L2J", aircraft.getTypeCode());
        assertEquals("4C", aircraft.getAerodromeReferenceCode());
        assertEquals("6", aircraft.getRffCategory());
        assertEquals("Jet", aircraft.getEngineType());
        assertEquals("Multi", aircraft.getEngineCount());
        assertEquals("Underwing mounted", aircraft.getEnginePosition());
        assertEquals("Tricycle retractable", aircraft.getLandingGearType());

        // Technical data
        assertEquals(35.80, aircraft.getWingspanMeters(), 0.01);
        assertEquals(37.57, aircraft.getLengthMeters(), 0.01);
        assertEquals(11.76, aircraft.getHeightMeters(), 0.01);
        assertNotNull(aircraft.getPowerplant());
        assertTrue(aircraft.getPowerplant().contains("CFM56"));

        // Engine models
        assertNotNull(aircraft.getEngineModels());
        assertTrue(aircraft.getEngineModels().length > 0);

        // Performance data
        // Take-Off
        assertEquals(145, aircraft.getTakeOffV2Kts());
        assertEquals(2190, aircraft.getTakeOffDistanceMeters());
        assertEquals(73500, aircraft.getMaxTakeOffWeightKg());

        // Initial Climb (to 5000 ft)
        assertEquals(175, aircraft.getInitialClimbIasKts());
        assertEquals(2500, aircraft.getInitialClimbRocFtMin());
    }
}
