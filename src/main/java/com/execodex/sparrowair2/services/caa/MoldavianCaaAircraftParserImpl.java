package com.execodex.sparrowair2.services.caa;

import com.execodex.sparrowair2.entities.caa.AircraftRegistration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the MoldavianCaaAircraftParser interface.
 * Parses aircraft registration data from Moldavian CAA PDF files.
 */
@Service
public class MoldavianCaaAircraftParserImpl implements MoldavianCaaAircraftParser {
    private static final Logger logger = LoggerFactory.getLogger(MoldavianCaaAircraftParserImpl.class);

    @Override
    public Flux<AircraftRegistration> parseAircraftRegistrations(Path path) {
        return Mono.fromCallable(() -> extractTextFromPdf(path))
                .flatMapMany(this::parseTextToAircraftRegistrations)
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Extracts text from a PDF file.
     *
     * @param path The path to the PDF file
     * @return The extracted text
     * @throws IOException If an error occurs while reading the PDF
     */
    private String extractTextFromPdf(Path path) throws IOException {
        try (PDDocument document = PDDocument.load(path.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            System.out.println("[DEBUG_LOG] Raw text from PDF:");
            System.out.println("[DEBUG_LOG] " + text.replace("\n", "\n[DEBUG_LOG] "));
            return text;
        } catch (IOException e) {
            logger.error("Error extracting text from PDF: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Parses the extracted text into AircraftRegistration objects.
     *
     * @param text The text extracted from the PDF
     * @return A Flux of AircraftRegistration objects
     */
    private Flux<AircraftRegistration> parseTextToAircraftRegistrations(String text) {
        List<AircraftRegistration> registrations = new ArrayList<>();

        // Split the text into lines
        String[] lines = text.split("\\r?\\n");

        // Define patterns to match registration data
        // This is a simplified approach and may need adjustment based on the actual PDF structure
        Pattern regNumberPattern = Pattern.compile("(ER-[A-Z0-9]+)");

        AircraftRegistration.AircraftRegistrationBuilder builder = null;

        for (String line : lines) {
            // Skip empty lines
            if (line.trim().isEmpty()) {
                continue;
            }

            // Look for registration number (e.g., ER-AAA)
            Matcher regNumberMatcher = regNumberPattern.matcher(line);
            if (regNumberMatcher.find()) {
                // If we were building a registration, add it to the list
                if (builder != null) {
                    registrations.add(builder.build());
                }

                // Start a new registration
                String registrationNumber = regNumberMatcher.group(1);
                builder = AircraftRegistration.builder()
                        .registrationNumber(registrationNumber);

                // Try to extract other information from the line
                extractAircraftInfo(line, builder);
            } else if (builder != null) {
                // Continue extracting information for the current registration
                extractAircraftInfo(line, builder);
            }
        }

        // Add the last registration if any
        if (builder != null) {
            registrations.add(builder.build());
        }

        return Flux.fromIterable(registrations);
    }

    /**
     * Extracts aircraft information from a line of text.
     *
     * @param line The line of text
     * @param builder The builder for the current AircraftRegistration
     */
    private void extractAircraftInfo(String line, AircraftRegistration.AircraftRegistrationBuilder builder) {
        // This is a simplified approach and may need adjustment based on the actual PDF structure

        // Check if the line contains table data
        // The PDF has a table with columns: Nr., Type of aircraft, Registration marks, Serial No., Operator*
        if (line.contains(builder.build().getRegistrationNumber())) {
            // Split the line by whitespace to extract table columns
            String[] parts = line.trim().split("\\s+");

            // Find the index of the registration number in the parts array
            int regIndex = -1;
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals(builder.build().getRegistrationNumber())) {
                    regIndex = i;
                    break;
                }
            }

            if (regIndex > 0) {
                // Extract Type of aircraft (should be before registration number)
                StringBuilder aircraftType = new StringBuilder();
                for (int i = 1; i < regIndex; i++) { // Start from 1 to skip the Nr. column
                    aircraftType.append(parts[i]).append(" ");
                }
                if (aircraftType.length() > 0) {
                    builder.aircraftType(aircraftType.toString().trim());
                }

                // Extract Serial No. (should be after registration number)
                if (regIndex + 1 < parts.length) {
                    builder.msn(parts[regIndex + 1]);
                }

                // Extract Operator (should be after Serial No.)
                if (regIndex + 2 < parts.length) {
                    StringBuilder operator = new StringBuilder();
                    for (int i = regIndex + 2; i < parts.length; i++) {
                        operator.append(parts[i]).append(" ");
                    }
                    if (operator.length() > 0) {
                        builder.operatorIcaoCode(operator.toString().trim());
                    }
                }
            }
        }

        // Try to extract owner name
        if (line.contains("Owner:") || line.contains("Proprietar:")) {
            String[] parts = line.split(":");
            if (parts.length > 1) {
                builder.ownerName(parts[1].trim());
            }
        }

        // Try to extract owner address
        if (line.contains("Address:") || line.contains("Adresa:")) {
            String[] parts = line.split(":");
            if (parts.length > 1) {
                builder.ownerAddress(parts[1].trim());
            }
        }

        // Try to extract status
        if (line.contains("Status:")) {
            String[] parts = line.split(":");
            if (parts.length > 1) {
                builder.status(parts[1].trim());
            }
        }

        // Try to extract date of registration
        if (line.contains("Date of Registration:") || line.contains("Registration Date:")) {
            String[] parts = line.split(":");
            if (parts.length > 1) {
                builder.dateOfRegistration(parts[1].trim());
            }
        }

        // Set country of registration to Moldova
        builder.countryOfRegistration("Moldova");
    }
}
