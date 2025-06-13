package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.caa.MdaAircraftRegistration;
import com.execodex.sparrowair2.services.caa.MoldavianCaaAircraftParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Handlers for CAA-related routes.
 */
@Component
public class CaaRouteHandlers {
    private final MoldavianCaaAircraftParser moldavianCaaAircraftParser;

    public CaaRouteHandlers(MoldavianCaaAircraftParser moldavianCaaAircraftParser) {
        this.moldavianCaaAircraftParser = moldavianCaaAircraftParser;
    }

    /**
     * Handles requests to get aircraft registrations from the Moldavian CAA PDF.
     *
     * @param request The server request
     * @return A server response containing a list of aircraft registrations
     */
    public Mono<ServerResponse> handleGetMoldavianAircraftRegistrations(ServerRequest request) {
        try {
            // Get the path to the PDF file
            Path pdfPath = new ClassPathResource("stuff/data/mcaa/Registrul_Aerian_al_Republicii_Moldova.pdf").getFile().toPath();
            
            // Parse the PDF
            Flux<MdaAircraftRegistration> registrations = moldavianCaaAircraftParser.parseAircraftRegistrations(pdfPath);
            
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(registrations, MdaAircraftRegistration.class)
                    .onErrorResume(e -> {
                        System.err.println("Error retrieving aircraft registrations: " + e.getMessage());
                        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .bodyValue("Error retrieving aircraft registrations: " + e.getMessage());
                    });
        } catch (IOException e) {
            System.err.println("Error accessing PDF file: " + e.getMessage());
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .bodyValue("Error accessing PDF file: " + e.getMessage());
        }
    }
}