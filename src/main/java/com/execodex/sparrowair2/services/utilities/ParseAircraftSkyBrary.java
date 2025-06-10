package com.execodex.sparrowair2.services.utilities;

import com.execodex.sparrowair2.entities.skybrary.Aircraft;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for parsing aircraft information from HTML files.
 */
@Service
public class ParseAircraftSkyBrary {
    private final WebClient webClient;

    public ParseAircraftSkyBrary(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder != null ? webClientBuilder.baseUrl("https://skybrary.aero").build() : null;
    }

    /**
     * Fetches and parses aircraft data from Skybrary website.
     * 
     * @param aircraftIcaoCode The ICAO code of the aircraft (e.g., "a19n")
     * @return A Mono containing the parsed Aircraft data
     */
    public Mono<Aircraft> parseOnlineAircraft(String aircraftIcaoCode) {
        String path = "/aircraft/" + aircraftIcaoCode.toLowerCase();

        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseHtmlContent)
                .map(aircraft -> {;
                    aircraft.setIcaoCode(aircraftIcaoCode.toUpperCase());
                    return aircraft;
                });
    }

    /**
     * Parses HTML content and extracts aircraft information.
     * 
     * @param htmlContent The HTML content to parse
     * @return The extracted Aircraft object
     */
    private Aircraft parseHtmlContent(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);

        Aircraft.AircraftBuilder builder = Aircraft.builder();

        // Extract basic aircraft information
        extractAircraftData(builder, doc);

        // Extract technical data
        extractTechnicalData(builder, doc);

        // Extract performance data
        extractPerformanceData(builder, doc);

        return builder.build();
    }



    /**
     * Extracts basic aircraft information from the HTML document.
     *
     * @param builder The Aircraft builder to populate
     * @param doc The HTML document to extract data from
     */
    private void extractAircraftData(Aircraft.AircraftBuilder builder, Document doc) {
        Element aircraftDataTable = doc.selectFirst("div.group-aircraft.data-table");
        if (aircraftDataTable != null) {
            Elements fields = aircraftDataTable.select("div.field");
            for (Element field : fields) {
                Element labelElement = field.selectFirst("div.field-label");
                Element itemElement = field.selectFirst("div.field-item");

                if (labelElement != null && itemElement != null) {
                    String label = labelElement.text().trim();
                    String value = itemElement.text().trim();

                    switch (label) {
                        case "Name":
                            builder.name(value);
                            break;
                        case "Manufacturer":
                            builder.manufacturer(value);
                            break;
                        case "Body":
                            builder.bodyType(value);
                            break;
                        case "Wing":
                            builder.wingType(value);
                            break;
                        case "Position":
                            // This could be either wing position or engine position
                            // Check the parent element to determine which one it is
                            Element parent = field.parent();
                            if (parent != null) {
                                Element prevField = field.previousElementSibling();
                                if (prevField != null && prevField.text().contains("Wing")) {
                                    builder.wingPosition(value);
                                } else if (prevField != null && prevField.text().contains("Engine")) {
                                    builder.enginePosition(value);
                                }
                            }
                            break;
                        case "Tail":
                            builder.tailType(value);
                            break;
                        case "WTC":
                            builder.weightCategory(value);
                            break;
                        case "APC":
                            builder.aircraftPerformanceCategory(value);
                            break;
                        case "Type Code":
                            builder.typeCode(value);
                            break;
                        case "Aerodrome Reference Code":
                            builder.aerodromeReferenceCode(value);
                            break;
                        case "RFF Category":
                            builder.rffCategory(value);
                            break;
                        case "Engine":
                            builder.engineType(value);
                            break;
                        case "Engine Count":
                            builder.engineCount(value);
                            break;
                        case "Landing gear":
                            builder.landingGearType(value);
                            break;
                    }
                }
            }
        }
    }

    /**
     * Extracts technical data from the HTML document.
     *
     * @param builder The Aircraft builder to populate
     * @param doc The HTML document to extract data from
     */
    private void extractTechnicalData(Aircraft.AircraftBuilder builder, Document doc) {
        Element technicalDataTable = doc.selectFirst("div.group-technical-data.data-table");
        if (technicalDataTable != null) {
            Elements fields = technicalDataTable.select("div.field");
            for (Element field : fields) {
                Element labelElement = field.selectFirst("div.field-label");
                Element itemElement = field.selectFirst("div.field-item");

                if (labelElement != null && itemElement != null) {
                    String label = labelElement.text().trim();
                    String value = itemElement.text().trim();

                    switch (label) {
                        case "Wing span (metric)":
                            try {
                                double wingspan = Double.parseDouble(value.replace(" m", ""));
                                builder.wingspanMeters(wingspan);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                        case "Length (metric)":
                            try {
                                double length = Double.parseDouble(value.replace(" m", ""));
                                builder.lengthMeters(length);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                        case "Height (metric)":
                            try {
                                double height = Double.parseDouble(value.replace(" m", ""));
                                builder.heightMeters(height);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                        case "Powerplant":
                            builder.powerplant(value);
                            break;
                    }
                }
            }

            // Extract engine models
            Elements engineModelElements = technicalDataTable.select("div.field-name-field-engine-models div.field-item");
            if (!engineModelElements.isEmpty()) {
                List<String> engineModels = new ArrayList<>();
                for (Element engineModel : engineModelElements) {
                    engineModels.add(engineModel.text().trim());
                }
                builder.engineModels(engineModels.toArray(new String[0]));
            }
        }
    }

    /**
     * Extracts performance data from the HTML document.
     *
     * @param builder The Aircraft builder to populate
     * @param doc The HTML document to extract data from
     */
    private void extractPerformanceData(Aircraft.AircraftBuilder builder, Document doc) {
        // Extract take-off data
        Element takeOffSection = doc.selectFirst("div.group-take-off.perf-data");
        if (takeOffSection != null) {
            Elements fields = takeOffSection.select("div.field");
            for (Element field : fields) {
                Element labelElement = field.selectFirst("div.field-label");
                Element itemElement = field.selectFirst("div.field-item");

                if (labelElement != null && itemElement != null) {
                    String label = labelElement.text().trim();
                    String value = itemElement.text().trim();

                    switch (label) {
                        case "V2":
                            try {
                                int v2 = Integer.parseInt(value.replace(" kts", ""));
                                builder.takeOffV2Kts(v2);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                        case "Distance":
                            try {
                                int distance = Integer.parseInt(value.replace(" m", ""));
                                builder.takeOffDistanceMeters(distance);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                        case "MTOW":
                            try {
                                int mtow = Integer.parseInt(value.replace(" kg", ""));
                                builder.maxTakeOffWeightKg(mtow);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                    }
                }
            }
        }

        // Extract initial climb data (to 5000 ft)
        Element initialClimbSection = doc.selectFirst("div.group-initial-climb-5000.perf-data");
        if (initialClimbSection != null) {
            Elements fields = initialClimbSection.select("div.field");
            for (Element field : fields) {
                Element labelElement = field.selectFirst("div.field-label");
                Element itemElement = field.selectFirst("div.field-item");

                if (labelElement != null && itemElement != null) {
                    String label = labelElement.text().trim();
                    String value = itemElement.text().trim();

                    switch (label) {
                        case "IAS":
                            try {
                                int ias = Integer.parseInt(value.replace(" kts", ""));
                                builder.initialClimbIasKts(ias);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                        case "ROC":
                            try {
                                int roc = Integer.parseInt(value.replace(" ft/min", ""));
                                builder.initialClimbRocFtMin(roc);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                    }
                }
            }
        }

        // Extract initial climb data (to FL150)
        Element climbToFL150Section = doc.selectFirst("div.group-initial-climb-fl150.perf-data");
        if (climbToFL150Section != null) {
            Elements fields = climbToFL150Section.select("div.field");
            for (Element field : fields) {
                Element labelElement = field.selectFirst("div.field-label");
                Element itemElement = field.selectFirst("div.field-item");

                if (labelElement != null && itemElement != null) {
                    String label = labelElement.text().trim();
                    String value = itemElement.text().trim();

                    switch (label) {
                        case "IAS":
                            try {
                                int ias = Integer.parseInt(value.replace(" kts", ""));
                                builder.climbToFL150IasKts(ias);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                        case "ROC":
                            try {
                                int roc = Integer.parseInt(value);
                                builder.climbToFL150RocFtMin(roc);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                    }
                }
            }
        }

        // Extract cruise data
        Element cruiseSection = doc.selectFirst("div.group-cruise.perf-data");
        if (cruiseSection != null) {
            Elements fields = cruiseSection.select("div.field");
            for (Element field : fields) {
                Element labelElement = field.selectFirst("div.field-label");
                Element itemElement = field.selectFirst("div.field-item");

                if (labelElement != null && itemElement != null) {
                    String label = labelElement.text().trim();
                    String value = itemElement.text().trim();

                    switch (label) {
                        case "IAS":
                            try {
                                int ias = Integer.parseInt(value.replace(" kts", ""));
                                builder.cruiseIasKts(ias);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                        case "Mach No":
                            try {
                                double mach = Double.parseDouble(value);
                                builder.cruiseMach(mach);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                        case "Altitude":
                            try {
                                int altitude = Integer.parseInt(value.replace(" ft", ""));
                                builder.cruiseAltitudeFt(altitude);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                    }
                }
            }
        }

        // Try alternative selectors for cruise data
        // Extract Mach from cruise section
        Element machElement = doc.selectFirst("div.field-node-field-performance-cruise-mach div.field-item");
        if (machElement != null) {
            String value = machElement.text().trim();
            try {
                double mach = Double.parseDouble(value);
                builder.cruiseMach(mach);
            } catch (NumberFormatException e) {
                // Handle parsing error
            }
        }

        // Extract Ceiling (Altitude) from cruise section
        Element ceilingElement = doc.selectFirst("div.field-node-field-performance-cruise-ceiling div.field-item");
        if (ceilingElement != null) {
            String value = ceilingElement.text().trim();
            try {
                int altitude = Integer.parseInt(value.replace(" ft", "")) * 100; // Convert FL to feet
                builder.cruiseAltitudeFt(altitude);
                // Also use this as service ceiling if not set elsewhere
                if (builder.build().getServiceCeilingFt() == 0) {
                    builder.serviceCeilingFt(altitude);
                }
            } catch (NumberFormatException e) {
                // Handle parsing error
            }
        }

        // Extract Range from cruise section
        Element cruiseRangeElement = doc.selectFirst("div.field-node-field-performance-cruise-range div.field-item");
        if (cruiseRangeElement != null) {
            String value = cruiseRangeElement.text().trim();
            try {
                int range = Integer.parseInt(value.replace(" NM", ""));
                builder.rangeNm(range);
            } catch (NumberFormatException e) {
                // Handle parsing error
            }
        }

        // Extract service ceiling data
        Element serviceCeilingSection = doc.selectFirst("div.group-service-ceiling.perf-data");
        if (serviceCeilingSection != null) {
            Element serviceCeilingElement = serviceCeilingSection.selectFirst("div.field-item");
            if (serviceCeilingElement != null) {
                String value = serviceCeilingElement.text().trim();
                try {
                    int serviceCeiling = Integer.parseInt(value.replace(" ft", ""));
                    builder.serviceCeilingFt(serviceCeiling);
                } catch (NumberFormatException e) {
                    // Handle parsing error
                }
            }
        }

        // Try alternative selector for service ceiling
        Element altServiceCeilingElement = doc.selectFirst("div.field-name-field-performance-service-ceiling div.field-item");
        if (altServiceCeilingElement != null) {
            String value = altServiceCeilingElement.text().trim();
            try {
                int serviceCeiling = Integer.parseInt(value.replace(" ft", ""));
                builder.serviceCeilingFt(serviceCeiling);
            } catch (NumberFormatException e) {
                // Handle parsing error
            }
        }

        // Extract range data
        Element rangeSection = doc.selectFirst("div.group-range.perf-data");
        if (rangeSection != null) {
            Element rangeElement = rangeSection.selectFirst("div.field-item");
            if (rangeElement != null) {
                String value = rangeElement.text().trim();
                try {
                    int range = Integer.parseInt(value.replace(" nm", ""));
                    builder.rangeNm(range);
                } catch (NumberFormatException e) {
                    // Handle parsing error
                }
            }
        }

        // Try alternative selector for range
        Element altRangeElement = doc.selectFirst("div.field-name-field-performance-range div.field-item");
        if (altRangeElement != null) {
            String value = altRangeElement.text().trim();
            try {
                int range = Integer.parseInt(value.replace(" nm", ""));
                builder.rangeNm(range);
            } catch (NumberFormatException e) {
                // Handle parsing error
            }
        }

        // Extract approach data
        Element approachSection = doc.selectFirst("div.group-approach.perf-data");
        if (approachSection != null) {
            // Extract Vapp (IAS) as VREF
            Element vappElement = approachSection.selectFirst("div.field-node-field-performance-app-ias div.field-item");
            if (vappElement != null) {
                String value = vappElement.text().trim();
                try {
                    int vref = Integer.parseInt(value.replace(" kts", ""));
                    builder.approachVrefKts(vref);
                } catch (NumberFormatException e) {
                    // Handle parsing error
                }
            }

            // Extract approach distance as landing distance
            Element approachDistanceElement = approachSection.selectFirst("div.field-node-field-performance-app-distance div.field-item");
            if (approachDistanceElement != null) {
                String value = approachDistanceElement.text().trim();
                try {
                    int distance = Integer.parseInt(value.replace(" m", ""));
                    builder.landingDistanceMeters(distance);
                } catch (NumberFormatException e) {
                    // Handle parsing error
                }
            }
        }

        // Try alternative selector for approach VREF
        Element altVappElement = doc.selectFirst("div.field-node-field-performance-app-ias div.field-item");
        if (altVappElement != null) {
            String value = altVappElement.text().trim();
            try {
                int vref = Integer.parseInt(value.replace(" kts", ""));
                builder.approachVrefKts(vref);
            } catch (NumberFormatException e) {
                // Handle parsing error
            }
        }

        // Extract landing data
        Element landingSection = doc.selectFirst("div.group-landing.perf-data");
        if (landingSection != null) {
            Elements fields = landingSection.select("div.field");
            for (Element field : fields) {
                Element labelElement = field.selectFirst("div.field-label");
                Element itemElement = field.selectFirst("div.field-item");

                if (labelElement != null && itemElement != null) {
                    String label = labelElement.text().trim();
                    String value = itemElement.text().trim();

                    switch (label) {
                        case "Distance":
                            try {
                                int distance = Integer.parseInt(value.replace(" m", ""));
                                builder.landingDistanceMeters(distance);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                        case "MLW":
                            try {
                                int mlw = Integer.parseInt(value.replace(" kg", ""));
                                builder.maxLandingWeightKg(mlw);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                            break;
                    }
                }
            }
        }

        // If maxLandingWeightKg is still 0, estimate it as 85% of maxTakeOffWeightKg
        if (builder.build().getMaxLandingWeightKg() == 0 && builder.build().getMaxTakeOffWeightKg() > 0) {
            int estimatedMLW = (int) (builder.build().getMaxTakeOffWeightKg() * 0.85);
            builder.maxLandingWeightKg(estimatedMLW);
        }

        // If cruiseIasKts is still 0, estimate it based on TAS and Mach
        if (builder.build().getCruiseIasKts() == 0) {
            // Look for TAS in cruise section
            Element tasElement = doc.selectFirst("div.field-node-field-performance-cruise-tas div.field-item");
            if (tasElement != null) {
                String value = tasElement.text().trim();
                try {
                    int tas = Integer.parseInt(value.replace(" kts", ""));
                    // Estimate IAS as 85% of TAS at cruise altitude
                    int estimatedIAS = (int) (tas * 0.85);
                    builder.cruiseIasKts(estimatedIAS);
                } catch (NumberFormatException e) {
                    // Handle parsing error
                }
            } else if (builder.build().getCruiseMach() > 0) {
                // Estimate IAS based on Mach number (rough approximation)
                double mach = builder.build().getCruiseMach();
                int estimatedIAS = (int) (mach * 600); // Rough approximation
                builder.cruiseIasKts(estimatedIAS);
            }
        }
    }
}
