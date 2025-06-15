package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.caa.FaaAircraftRegistration;
import com.execodex.sparrowair2.services.FaaAircraftRegistrationService;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.execodex.sparrowair2.entities.caa.FaaAircraftRegistration.parseAircraftRegistrationFromCsvLine2;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class FaaAircraftRegistrationHandler {

    private final FaaAircraftRegistrationService faaAircraftRegistrationService;

    public FaaAircraftRegistrationHandler(FaaAircraftRegistrationService faaAircraftRegistrationService) {
        this.faaAircraftRegistrationService = faaAircraftRegistrationService;
    }

    // Get all FAA aircraft registrations
    public Mono<ServerResponse> getAllFaaAircraftRegistrations(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getAllFaaAircraftRegistrations(), FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registration by N-Number
    public Mono<ServerResponse> getFaaAircraftRegistrationByNNumber(ServerRequest request) {
        String nNumber = request.pathVariable("nNumber");
        return faaAircraftRegistrationService.getFaaAircraftRegistrationByNNumber(nNumber)
                .flatMap(registration -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(registration))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registrations by engine manufacturer model code
    public Mono<ServerResponse> getFaaAircraftRegistrationsByEngineMfrModelCode(ServerRequest request) {
        String engineMfrModelCode = request.pathVariable("engineMfrModelCode");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getFaaAircraftRegistrationsByEngineMfrModelCode(engineMfrModelCode), 
                        FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registrations by type aircraft
    public Mono<ServerResponse> getFaaAircraftRegistrationsByTypeAircraft(ServerRequest request) {
        String typeAircraft = request.pathVariable("typeAircraft");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getFaaAircraftRegistrationsByTypeAircraft(typeAircraft), 
                        FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registrations by aircraft manufacturer model code
    public Mono<ServerResponse> getFaaAircraftRegistrationsByAircraftMfrModelCode(ServerRequest request) {
        String aircraftMfrModelCode = request.pathVariable("aircraftMfrModelCode");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getFaaAircraftRegistrationsByAircraftMfrModelCode(aircraftMfrModelCode), 
                        FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registrations by Mode S Code
    public Mono<ServerResponse> getFaaAircraftRegistrationsByModeSCode(ServerRequest request) {
        String modeSCode = request.pathVariable("modeSCode");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getFaaAircraftRegistrationsByModeSCode(modeSCode), 
                        FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Get FAA aircraft registrations by Mode S Code Hex
    public Mono<ServerResponse> getFaaAircraftRegistrationsByModeScodeHex(ServerRequest request) {
        String modeScodeHex = request.pathVariable("modeScodeHex");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(faaAircraftRegistrationService.getFaaAircraftRegistrationsByModeScodeHex(modeScodeHex), 
                        FaaAircraftRegistration.class)
                .onErrorResume(this::handleError);
    }

    // Create a new FAA aircraft registration
    public Mono<ServerResponse> createFaaAircraftRegistration(ServerRequest request) {
        return request.bodyToMono(FaaAircraftRegistration.class)
                .flatMap(faaAircraftRegistrationService::createFaaAircraftRegistration)
                .flatMap(registration -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(registration))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("FAA aircraft registration with N-Number already exists");
                    }
                    return handleError(e);
                });
    }

    // Update an existing FAA aircraft registration
    public Mono<ServerResponse> updateFaaAircraftRegistration(ServerRequest request) {
        String nNumber = request.pathVariable("nNumber");
        return request.bodyToMono(FaaAircraftRegistration.class)
                .flatMap(registration -> faaAircraftRegistrationService.updateFaaAircraftRegistration(nNumber, registration))
                .flatMap(updatedRegistration -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedRegistration))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete a FAA aircraft registration
    public Mono<ServerResponse> deleteFaaAircraftRegistration(ServerRequest request) {
        String nNumber = request.pathVariable("nNumber");
        return faaAircraftRegistrationService.getFaaAircraftRegistrationByNNumber(nNumber)
                .flatMap(registration -> faaAircraftRegistrationService.deleteFaaAircraftRegistration(nNumber)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in FaaAircraftRegistrationHandler occurred: " + error.getMessage());
    }

    // Upload and process FAA aircraft registrations from a file
    public Mono<ServerResponse> uploadFaaAircraftRegistrationsFile(ServerRequest request) {
        return request.multipartData()
                .flatMap(multipartData -> {
                    if (!multipartData.containsKey("file")) {
                        return ServerResponse.badRequest().bodyValue("No file part found in the request");
                    }

                    FilePart filePart = (FilePart) multipartData.getFirst("file");
                    if (filePart == null) {
                        return ServerResponse.badRequest().bodyValue("File part is null");
                    }

                    // This will hold any leftover bytes from the previous buffer that didn't contain a newline
                    AtomicReference<String> leftover = new AtomicReference<>("");

                    // Flag to skip the header row
                    AtomicReference<Boolean> headerSkipped = new AtomicReference<>(false);

                    // Process the file content
                    Flux<FaaAircraftRegistration> registrationsFlux = filePart.content()
                            .concatMap(dataBuffer -> {
                                // Convert buffer to string
                                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(bytes);
                                DataBufferUtils.release(dataBuffer);
                                String content = leftover.getAndSet("") + new String(bytes, StandardCharsets.UTF_8);

                                // Process the content to extract complete lines
                                List<String> lines = new ArrayList<>();
                                int lastNewlineIndex = content.lastIndexOf('\n');

                                if (lastNewlineIndex >= 0) {
                                    // We have at least one complete line
                                    String completeLines = content.substring(0, lastNewlineIndex + 1);

                                    // Store any remaining content for the next buffer
                                    if (lastNewlineIndex < content.length() - 1) {
                                        leftover.set(content.substring(lastNewlineIndex + 1));
                                    }

                                    // Split complete lines and add to our list
                                    for (String line : completeLines.split("\n")) {
                                        if (!line.trim().isEmpty()) {
                                            lines.add(line);
                                        }
                                    }
                                } else {
                                    // No newline found, add all content to leftover
                                    leftover.set(content);
                                }

                                return Flux.fromIterable(lines);
                            })
                            // Process any remaining content after all buffers are read
                            .concatWith(Mono.fromSupplier(() -> {
                                String remaining = leftover.getAndSet("");
                                if (!remaining.trim().isEmpty()) {
                                    return remaining;
                                }
                                return null;
                            }).filter(Objects::nonNull).flux())
                            // Skip header if present (first line)
                            .flatMap(line -> {
                                if (line == null || line.trim().isEmpty()) {
                                    return Mono.empty();
                                }

                                // Skip the header row
                                if (!headerSkipped.get()) {
                                    headerSkipped.set(true);
                                    return Mono.empty();
                                }

                                try {
                                    // Parse CSV line into FaaAircraftRegistration object
                                    FaaAircraftRegistration registration = parseAircraftRegistrationFromCsvLine2(line);
                                    return Mono.justOrEmpty(registration);
                                } catch (Exception e) {
                                    // Only log errors for non-empty lines
                                    if (!line.trim().isEmpty()) {
                                        System.err.println("Error parsing line: " + e.getMessage());
                                    }
                                    return Mono.empty();
                                }
                            })
                            // Add onErrorContinue to handle errors without breaking the stream
                            .onErrorContinue((throwable, o) -> {
                                System.err.println("Error processing registration: " + throwable.getMessage());
                            });

                    // Process registrations in the background and return a quick response
                    // This prevents connection reset issues when clients disconnect before processing completes
                    Mono<Void> processingMono = faaAircraftRegistrationService.bulkInsertFaaAircraftRegistrations(registrationsFlux)
                            .doOnNext(registration -> {
                                // Process each registration but don't include in response
                            })
                            .then()
                            .doOnError(e -> {
                                if (e instanceof java.net.SocketException && e.getMessage().contains("Connection reset")) {
                                    // Log but don't propagate connection reset errors
                                    System.err.println("Client disconnected before processing completed: " + e.getMessage());
                                } else {
                                    System.err.println("Error during bulk insert: " + e.getMessage());
                                }
                            })
                            .onErrorComplete(); // Complete even if there's an error

                    // Start processing in the background
                    processingMono.subscribe();

                    // Return immediate success response to the client
                    return ServerResponse.accepted()
                            .contentType(APPLICATION_JSON)
                            .bodyValue("File upload accepted for processing");
                })
                .onErrorResume(e -> {
                    if (e instanceof java.net.SocketException && e.getMessage().contains("Connection reset")) {
                        // Log but don't propagate connection reset errors
                        System.err.println("Client disconnected: " + e.getMessage());
                        return Mono.empty();
                    }
                    return this.handleError(e);
                });
    }
}
