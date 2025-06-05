package com.execodex.sparrowair2.services.utilities;

import com.execodex.sparrowair2.entities.skybrary.AircraftType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class ParseOnlineAircraftType {
    private final WebClient webClient;

    public ParseOnlineAircraftType(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://skybrary.aero").build();
    }

    /**
     * Fetches and parses aircraft type data from Skybrary website.
     * 
     * @param aircraftIcaoCode The ICAO code of the aircraft (e.g., "a19n")
     * @return A Mono containing the parsed AircraftType data
     */
    public Mono<AircraftType> parseOnlineAircraftType(String aircraftIcaoCode) {
        String path = "/aircraft/" + aircraftIcaoCode.toLowerCase();

        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseHtmlContent);
    }

    /**
     * Parses HTML content from Skybrary and extracts aircraft information.
     * 
     * @param htmlContent The HTML content from Skybrary
     * @return An AircraftType object with extracted information
     */
    private AircraftType parseHtmlContent(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);

        // Extract ICAO code from the page title
        String title = doc.title();
        String icaoCode = title.contains(" - ") ? 
                title.substring(0, title.indexOf(" - ")).trim() : 
                "";

        // Start building the AircraftType object
        AircraftType.AircraftTypeBuilder builder = AircraftType.builder()
                .icaoCode(icaoCode);

        // Extract basic aircraft information from the "group-aircraft data-table" section
        Element aircraftDataTable = doc.selectFirst("div.group-aircraft.data-table");
        if (aircraftDataTable != null) {
            extractAircraftDataTable(builder, aircraftDataTable);
        }

        // Extract technical data from the "group-technical-data data-table" section
        Element technicalDataTable = doc.selectFirst("div.group-technical-data.data-table");
        if (technicalDataTable != null) {
            extractTechnicalDataTable(builder, technicalDataTable);
        }

        // Extract performance data from the "group-performance-data clearfix" section
        Element performanceDataSection = doc.selectFirst("div.group-performance-data.clearfix");
        if (performanceDataSection != null) {
            extractPerformanceDataSection(builder, performanceDataSection);
        }

        // If the new structure is not found, fall back to the old structure
        if (aircraftDataTable == null && technicalDataTable == null && performanceDataSection == null) {
            // Extract manufacturer
            Element manufacturerElement = doc.selectFirst("div.field--name-field-manufacturer");
            if (manufacturerElement != null) {
                builder.manufacturer(manufacturerElement.text().trim());
            }

            // Extract information from tables
            Elements tables = doc.select("table.table");
            for (Element table : tables) {
                Map<String, String> tableData = extractTableData(table);
                populateAircraftTypeFromTableData(builder, tableData);
            }

            // Extract additional information from the page content
            Elements contentSections = doc.select("div.field--name-field-content");
            for (Element section : contentSections) {
                extractContentSectionData(builder, section);
            }
        }

        return builder.build();
    }

    /**
     * Extracts key-value pairs from a table element.
     * 
     * @param table The table element to extract data from
     * @return A map of key-value pairs from the table
     */
    private Map<String, String> extractTableData(Element table) {
        Map<String, String> data = new HashMap<>();
        Elements rows = table.select("tr");

        for (Element row : rows) {
            Elements cells = row.select("td");
            if (cells.size() >= 2) {
                String key = cells.get(0).text().trim();
                String value = cells.get(1).text().trim();
                data.put(key, value);
            }
        }

        return data;
    }

    /**
     * Populates the AircraftType builder with data from the extracted table.
     * 
     * @param builder The AircraftType builder to populate
     * @param tableData The table data as key-value pairs
     */
    private void populateAircraftTypeFromTableData(AircraftType.AircraftTypeBuilder builder, Map<String, String> tableData) {
        // Map table data to AircraftType fields
        for (Map.Entry<String, String> entry : tableData.entrySet()) {
            String key = entry.getKey().toLowerCase();
            String value = entry.getValue();

            switch (key) {
                case "body":
                    builder.bodyType(value);
                    break;
                case "wing":
                    builder.wingType(value);
                    break;
                case "wing position":
                    builder.wingPosition(value);
                    break;
                case "tail":
                    builder.tailPosition(value);
                    break;
                case "landing gear":
                    builder.landingGearType(value);
                    break;
                case "engine position":
                    builder.enginePosition(value);
                    break;
                case "wtc":
                    builder.weightCategory(value);
                    break;
                case "apc":
                    builder.apc(value);
                    break;
                case "type code":
                    builder.typeCode(value);
                    break;
                case "aerodrome reference code":
                    builder.aerodromeReferenceCode(value);
                    break;
                case "rff category":
                    builder.rffCategory(value);
                    break;
                case "engine":
                    builder.engineType(value);
                    break;
                case "engine count":
                case "number of engines":
                    try {
                        builder.engineCount(Integer.parseInt(value.trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                    break;
                case "engine choice":
                    builder.engineModels(new String[]{value});
                    break;
                case "fuel capacity":
                    // No direct mapping for fuelCapacity in new AircraftType
                    break;
                case "max range":
                case "range":
                    try {
                        builder.rangeNM(Integer.parseInt(value.replaceAll("[^0-9]", "").trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                    break;
                case "cruise speed":
                    try {
                        builder.cruiseSpeedKts(Double.parseDouble(value.replaceAll("[^0-9.]", "").trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                    break;
                case "cruise mach":
                    try {
                        builder.cruiseSpeedMach(Double.parseDouble(value.replaceAll("[^0-9.]", "").trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                    break;
                case "ceiling":
                    try {
                        builder.serviceCeiling(Integer.parseInt(value.replaceAll("[^0-9]", "").trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                    break;
                case "wingspan":
                    try {
                        builder.wingspan(Double.parseDouble(value.replaceAll("[^0-9.]", "").trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                    break;
                case "length":
                    try {
                        builder.length(Double.parseDouble(value.replaceAll("[^0-9.]", "").trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                    break;
                case "height":
                    try {
                        builder.height(Double.parseDouble(value.replaceAll("[^0-9.]", "").trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                    break;
                // Removed fields that don't exist in the new AircraftType
            }
        }
    }

    /**
     * Extracts data from content sections of the page.
     * 
     * @param builder The AircraftType builder to populate
     * @param section The content section to extract data from
     */
    private void extractContentSectionData(AircraftType.AircraftTypeBuilder builder, Element section) {
        // Extract additional information from content sections
        // This is a simplified implementation and may need to be expanded
        // based on the actual structure of the Skybrary pages

        // Look for paragraphs that might contain relevant information
        Elements paragraphs = section.select("p");
        for (Element paragraph : paragraphs) {
            String text = paragraph.text().toLowerCase();

            // The new AircraftType doesn't have cabin dimensions fields
            // We could extract other information here if needed in the future
        }
    }

    /**
     * Extracts basic aircraft information from the "group-aircraft data-table" section.
     * 
     * @param builder The AircraftType builder to populate
     * @param dataTable The data table element to extract data from
     */
    private void extractAircraftDataTable(AircraftType.AircraftTypeBuilder builder, Element dataTable) {
        // Extract fields from the aircraft data table
        Elements fields = dataTable.select("div.field");
        for (Element field : fields) {
            Element labelElement = field.selectFirst("div.field-label");
            Element itemElement = field.selectFirst("div.field-item");

            if (labelElement != null && itemElement != null) {
                String label = labelElement.text().trim();
                String value = itemElement.text().trim();

                // Remove the colon from the label if present
                if (label.endsWith(":")) {
                    label = label.substring(0, label.length() - 1).trim();
                }

                // Map the label to the appropriate field in the AircraftType
                switch (label.toLowerCase()) {
                    case "name":
                        builder.name(value);
                        break;
                    case "manufacturer":
                        builder.manufacturer(value);
                        break;
                    case "body":
                        builder.bodyType(value);
                        break;
                    case "wing":
                        builder.wingType(value);
                        break;
                    case "position":
                        // This could be either wing position or engine position
                        // Check the parent element to determine which one it is
                        Element parent = field.parent();
                        if (parent != null) {
                            // Check if this is under the wing section
                            Element wingLabel = parent.previousElementSibling();
                            if (wingLabel != null && wingLabel.text().toLowerCase().contains("wing")) {
                                builder.wingPosition(value);
                            } else {
                                // Assume it's engine position
                                builder.enginePosition(value);
                            }
                        }
                        break;
                    case "tail":
                        builder.tailType(value);
                        break;
                    case "wtc":
                        builder.weightCategory(value);
                        break;
                    case "apc":
                        // APC is not in the AircraftType entity, but we could add it if needed
                        break;
                    case "type code":
                        // No direct mapping for typeCode in new AircraftType
                        break;
                    case "aerodrome reference code":
                        builder.aerodromeReferenceCode(value);
                        break;
                    case "rff category":
                        builder.rffCategory(value);
                        break;
                    case "engine":
                        builder.engineType(value);
                        break;
                    case "engine count":
                        try {
                            builder.engineCount(Integer.parseInt(value.trim()));
                        } catch (NumberFormatException e) {
                            // If parsing fails, just ignore this field
                        }
                        break;
                    case "landing gear":
                        builder.landingGearType(value);
                        break;
                }
            }
        }
    }

    /**
     * Extracts technical data from the "group-technical-data data-table" section.
     * 
     * @param builder The AircraftType builder to populate
     * @param dataTable The data table element to extract data from
     */
    private void extractTechnicalDataTable(AircraftType.AircraftTypeBuilder builder, Element dataTable) {
        // Extract fields from the technical data table
        Elements fields = dataTable.select("div.field");
        for (Element field : fields) {
            Element labelElement = field.selectFirst("div.field-label");
            Element itemElement = field.selectFirst("div.field-item");

            if (labelElement != null && itemElement != null) {
                String label = labelElement.text().trim();
                String value = itemElement.text().trim();

                // Remove the colon from the label if present
                if (label.endsWith(":")) {
                    label = label.substring(0, label.length() - 1).trim();
                }

                // Map the label to the appropriate field in the AircraftType
                switch (label.toLowerCase()) {
                    case "wing span (metric)":
                        try {
                            builder.wingspan(Double.parseDouble(value.replaceAll("[^0-9.]", "").trim()));
                        } catch (NumberFormatException e) {
                            // If parsing fails, just ignore this field
                        }
                        break;
                    case "length (metric)":
                        try {
                            builder.length(Double.parseDouble(value.replaceAll("[^0-9.]", "").trim()));
                        } catch (NumberFormatException e) {
                            // If parsing fails, just ignore this field
                        }
                        break;
                    case "height (metric)":
                        try {
                            builder.height(Double.parseDouble(value.replaceAll("[^0-9.]", "").trim()));
                        } catch (NumberFormatException e) {
                            // If parsing fails, just ignore this field
                        }
                        break;
                    case "powerplant":
                        // Powerplant is not directly in the AircraftType entity, but we could extract engine information
                        break;
                    case "engine model(s)":
                        builder.engineModels(new String[]{value});
                        break;
                }
            }
        }
    }

    /**
     * Extracts performance data from the "group-performance-data clearfix" section.
     * 
     * @param builder The AircraftType builder to populate
     * @param section The performance data section to extract data from
     */
    private void extractPerformanceDataSection(AircraftType.AircraftTypeBuilder builder, Element section) {
        // Extract performance data sections
        Elements performanceDataSections = section.select("div.group-perf-data");
        for (Element perfDataSection : performanceDataSections) {
            // Get the section title
            Element titleElement = perfDataSection.selectFirst("div > span");
            if (titleElement != null) {
                String sectionTitle = titleElement.text().trim();

                // Extract fields from this performance data section
                Elements fields = perfDataSection.select("div.field");
                Map<String, String> sectionData = new HashMap<>();

                for (Element field : fields) {
                    Element labelElement = field.selectFirst("div.field-label");
                    Element itemElement = field.selectFirst("div.field-item");

                    if (labelElement != null && itemElement != null) {
                        String label = labelElement.text().trim();
                        String value = itemElement.text().trim();

                        // Remove the colon from the label if present
                        if (label.endsWith(":")) {
                            label = label.substring(0, label.length() - 1).trim();
                        }

                        sectionData.put(label.toLowerCase(), value);
                    }
                }

                // Map the section data to the appropriate fields in the AircraftType
                populatePerformanceData(builder, sectionTitle, sectionData);
            }
        }
    }

    /**
     * Populates the AircraftType builder with performance data.
     * 
     * @param builder The AircraftType builder to populate
     * @param sectionTitle The title of the performance data section
     * @param sectionData The data from the performance data section
     */
    private void populatePerformanceData(AircraftType.AircraftTypeBuilder builder, String sectionTitle, Map<String, String> sectionData) {
        switch (sectionTitle.toLowerCase()) {
            case "cruise":
                if (sectionData.containsKey("tas")) {
                    try {
                        String tas = sectionData.get("tas");
                        builder.cruiseSpeedKts(Double.parseDouble(tas.replaceAll("[^0-9.]", "").trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                }
                if (sectionData.containsKey("mach")) {
                    try {
                        String mach = sectionData.get("mach");
                        builder.cruiseSpeedMach(Double.parseDouble(mach.replaceAll("[^0-9.]", "").trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                }
                if (sectionData.containsKey("ceiling")) {
                    try {
                        String ceiling = sectionData.get("ceiling");
                        builder.serviceCeiling(Integer.parseInt(ceiling.replaceAll("[^0-9]", "").trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                }
                if (sectionData.containsKey("range")) {
                    try {
                        String range = sectionData.get("range");
                        builder.rangeNM(Integer.parseInt(range.replaceAll("[^0-9]", "").trim()));
                    } catch (NumberFormatException e) {
                        // If parsing fails, just ignore this field
                    }
                }
                break;
            // Other performance data sections are not directly mapped to the new AircraftType
            // We could add more mappings here if needed in the future
        }
    }


}
