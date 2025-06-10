package com.execodex.sparrowair2.services.utilities;

import com.execodex.sparrowair2.entities.skybrary.AirportNew;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * Service for parsing airport information from HTML files.
 */
@Service
public class ParseAirportSkyBrary {
    private final WebClient webClient;

    public ParseAirportSkyBrary(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder != null ? webClientBuilder.baseUrl("https://skybrary.aero").build() : null;
    }

    /**
     * Crawls the Skybrary airports page and extracts all ICAO code links.
     * Handles pagination to fetch all airports across multiple pages.
     * 
     * @return A Flux of ICAO codes
     * @param totalPages
     */
    public Flux<String> crawlAirportLinks(int totalPages) {
        // There are 4800 airports in total, with 200 per page = 24 pages
        final int itemsPerPage = 200;

        return Flux.range(1, totalPages)
                .flatMap(page -> fetchAirportPage(page, itemsPerPage));
    }

    /**
     * Fetches a single page of airport data.
     * 
     * @param page The page number to fetch
     * @param itemsPerPage The number of items per page
     * @return A Flux of ICAO codes from the specified page
     */
    private Flux<String> fetchAirportPage(int page, int itemsPerPage) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/airports")
                        .queryParam("items_per_page", itemsPerPage)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMapMany(this::extractIcaoCodes);
    }

    /**
     * Extracts ICAO codes from the HTML content of the airports page.
     * 
     * @param htmlContent The HTML content to parse
     * @return A Flux of ICAO codes
     */
    private Flux<String> extractIcaoCodes(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        Elements links = doc.select("a[href^=/airports/]");

        // ICAO codes are typically 4 characters, consisting of letters and numbers
        Pattern icaoPattern = Pattern.compile("^[A-Z0-9]{4}$");

        return Flux.fromIterable(links)
                .map(link -> {
                    String href = link.attr("href");
                    // Extract the part after /airports/
                    String potentialIcao = href.substring("/airports/".length());
                    return potentialIcao.toUpperCase();
                })
                .filter(code -> !code.isEmpty() && icaoPattern.matcher(code).matches())
                .distinct();
    }



    /**
     * Fetches and parses airport data from Skybrary website.
     * 
     * @param airportIcaoCode The ICAO code of the airport (e.g., "KJFK")
     * @return A Mono containing the parsed Airport data
     */
    public Mono<AirportNew> parseOnlineAirport(String airportIcaoCode) {
        String path = "/airports/" + airportIcaoCode.toLowerCase();

        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseHtmlContent)
                .map(airportNew -> {
                    airportNew.setIcaoCode(airportIcaoCode.toUpperCase());
                    return airportNew;
                });
    }

    /**
     * Parses HTML content from a file and extracts airport information.
     * 
     * @param filePath The path to the HTML file
     * @return The extracted Airport object
     * @throws IOException If there's an error reading the file
     */
    public AirportNew parseHtmlFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        String htmlContent = Files.readString(path);
        return parseHtmlContent(htmlContent);
    }

    /**
     * Parses HTML content and extracts airport information.
     * 
     * @param htmlContent The HTML content to parse
     * @return The extracted Airport object
     */
    private AirportNew parseHtmlContent(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);

        AirportNew airportNew = new AirportNew();

        // Extract airport data
        extractAirportData(airportNew, doc);

        return airportNew;
    }

    /**
     * Parses a DMS (Degrees-Minutes-Seconds) string to a decimal degree value.
     * 
     * @param dmsString The DMS string (e.g., "51° 28' 39" N" or "0° 27' 41" W")
     * @param isLatitude True if parsing latitude, false if parsing longitude
     * @return The decimal degree value
     */
    private double parseDMSToDecimal(String dmsString, boolean isLatitude) {
        // Example formats: "51° 28' 39" N" or "0° 27' 41" W"

        // Remove all non-alphanumeric characters except spaces and cardinal directions
        String cleanedString = dmsString.replaceAll("[^0-9NSEW\\s]", "").trim();

        // Split by spaces
        String[] parts = cleanedString.split("\\s+");

        // The last part should be the cardinal direction (N, S, E, W)
        String direction = parts[parts.length - 1];

        // Parse degrees, minutes, seconds
        double degrees = 0;
        double minutes = 0;
        double seconds = 0;

        // Parse the numeric parts
        if (parts.length >= 3) {
            degrees = Double.parseDouble(parts[0]);
            minutes = Double.parseDouble(parts[1]);
            seconds = Double.parseDouble(parts[2]);
        } else if (parts.length == 2) {
            degrees = Double.parseDouble(parts[0]);
            minutes = Double.parseDouble(parts[1]);
        } else if (parts.length == 1) {
            degrees = Double.parseDouble(parts[0]);
        }

        // Convert to decimal degrees
        double decimalDegrees = degrees + (minutes / 60.0) + (seconds / 3600.0);

        // Apply sign based on direction
        if (isLatitude) {
            // For latitude: S is negative
            if (direction.equals("S")) {
                decimalDegrees = -decimalDegrees;
            }
        } else {
            // For longitude: W is negative
            if (direction.equals("W")) {
                decimalDegrees = -decimalDegrees;
            }
        }

        return decimalDegrees;
    }

    /**
     * Extracts airport information from the HTML document.
     *
     * @param airportNew The Airport object to populate
     * @param doc The HTML document to extract data from
     */
    private void extractAirportData(AirportNew airportNew, Document doc) {
        // Extract ICAO code
        Element icaoElement = doc.selectFirst("div.field-label:contains(ICAO code) + div.field-items div.field-item");
        if (icaoElement != null) {
            airportNew.setIcaoCode(icaoElement.text().trim());
        }

        // Extract IATA code
        Element iataElement = doc.selectFirst("div.field-label:contains(IATA Code) + div.field-items div.field-item");
        if (iataElement != null) {
            airportNew.setIataCode(iataElement.text().trim());
        }

        // Extract name
        Element nameElement = doc.selectFirst("h1.title");
        if (nameElement != null) {
            airportNew.setName(nameElement.text().trim());
        }

        // Extract ICAO region
        Element regionElement = doc.selectFirst("div.field-label:contains(ICAO Region) + div.field-items div.field-item");
        if (regionElement != null) {
            airportNew.setIcaoRegion(regionElement.text().trim());
        }

        // Extract ICAO territory
        Element territoryElement = doc.selectFirst("div.field-label:contains(ICAO Territory) + div.field-items div.field-item a");
        if (territoryElement != null) {
            airportNew.setIcaoTerritory(territoryElement.text().trim());
        }

        // Extract location
        Element locationElement = doc.selectFirst("div.field-label:contains(Location) + div.field-items div.field-item");
        if (locationElement != null) {
            airportNew.setLocation(locationElement.text().trim());
        }

        // Extract city
        Element cityElement = doc.selectFirst("div.field-label:contains(Serving) + div.field-items div.field-item");
        if (cityElement != null) {
            airportNew.setCity(cityElement.text().trim());
        }

        // Extract country (from territory)
        if (territoryElement != null) {
            airportNew.setCountry(territoryElement.text().trim());
        }

        // Extract elevation
        Element elevationElement = doc.selectFirst("div.field-label:contains(Elevation) + div.field-items div.field-item");
        if (elevationElement != null) {
            String elevationText = elevationElement.text().trim();
            // Remove " ft" from the end if present
            if (elevationText.endsWith(" ft")) {
                elevationText = elevationText.substring(0, elevationText.length() - 3);
            }
            airportNew.setElevation(elevationText);
        }

        // Extract coordinates (latitude and longitude)
        Element coordinatesElement = doc.selectFirst("div.field-label:contains(Coordinates) + div.field-items div.field-item");
        if (coordinatesElement != null) {
            // Extract latitude
            Element latElement = coordinatesElement.selectFirst("span.dms-lat");
            if (latElement != null) {
                String latText = latElement.text().trim();
                airportNew.setLatitude(parseDMSToDecimal(latText, true));
            }

            // Extract longitude
            Element lonElement = coordinatesElement.selectFirst("span.dms-lon");
            if (lonElement != null) {
                String lonText = lonElement.text().trim();
                airportNew.setLongitude(parseDMSToDecimal(lonText, false));
            }
        }

        // Extract KC Code
        Element kcCodeElement = doc.selectFirst("div.field-label:contains(KCC) + div.field-items div.field-item");
        if (kcCodeElement != null) {
            airportNew.setKCCode(kcCodeElement.text().trim());
        }

        // Extract Airport BS
        Element bsElement = doc.selectFirst("div.field-label:contains(Airport BS) + div.field-items div.field-item");
        if (bsElement != null) {
            airportNew.setAirportBS(bsElement.text().trim());
        }

        // Extract Airport LOS
        Element losElement = doc.selectFirst("div.field-label:contains(Airport LOS) + div.field-items div.field-item");
        if (losElement != null) {
            airportNew.setAirportLOS(losElement.text().trim());
        }

        // Extract Airport RE
        Element reElement = doc.selectFirst("div.field-label:contains(Airport RE) + div.field-items div.field-item");
        if (reElement != null) {
            airportNew.setAirportRE(reElement.text().trim());
        }
    }
}
