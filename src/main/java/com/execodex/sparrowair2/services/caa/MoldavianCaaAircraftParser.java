package com.execodex.sparrowair2.services.caa;

import com.execodex.sparrowair2.entities.caa.MdaAircraftRegistration;
import reactor.core.publisher.Flux;

import java.nio.file.Path;

/**
 * Service for parsing aircraft registration information from Moldavian CAA PDF files.
 */
public interface MoldavianCaaAircraftParser {
    
    /**
     * Parses aircraft registration data from a Moldavian CAA PDF file.
     * 
     * @param path The path to the PDF file
     * @return A Flux containing the parsed AircraftRegistration data
     */
    Flux<MdaAircraftRegistration> parseAircraftRegistrations(Path path);
}