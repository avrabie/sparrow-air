package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.entities.caa.FaaAircraftRegistration;
import com.execodex.sparrowair2.handlers.FaaAircraftRegistrationHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class FaaAircraftRegistrationRoutes {

    private final FaaAircraftRegistrationHandler faaAircraftRegistrationHandler;

    public FaaAircraftRegistrationRoutes(FaaAircraftRegistrationHandler faaAircraftRegistrationHandler) {
        this.faaAircraftRegistrationHandler = faaAircraftRegistrationHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/faa-aircraft-registrations",
                    method = RequestMethod.GET,
                    beanClass = FaaAircraftRegistrationHandler.class,
                    beanMethod = "getAllFaaAircraftRegistrations",
                    operation = @Operation(
                            operationId = "getAllFaaAircraftRegistrations",
                            summary = "Get all FAA aircraft registrations",
                            description = "Returns a list of all FAA aircraft registrations 📝",
                            tags = {"FAA Aircraft Registration 📝"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/faa-aircraft-registrations/{nNumber}",
                    method = RequestMethod.GET,
                    beanClass = FaaAircraftRegistrationHandler.class,
                    beanMethod = "getFaaAircraftRegistrationByNNumber",
                    operation = @Operation(
                            operationId = "getFaaAircraftRegistrationByNNumber",
                            summary = "Get FAA aircraft registration by N-Number",
                            description = "Returns a FAA aircraft registration 📝 by N-Number",
                            tags = {"FAA Aircraft Registration 📝"},
                            parameters = {
                                    @Parameter(name = "nNumber", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "FAA aircraft registration not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/faa-aircraft-registrations",
                    method = RequestMethod.POST,
                    beanClass = FaaAircraftRegistrationHandler.class,
                    beanMethod = "createFaaAircraftRegistration",
                    operation = @Operation(
                            operationId = "createFaaAircraftRegistration",
                            summary = "Create a new FAA aircraft registration",
                            tags = {"FAA Aircraft Registration 📝"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "FAA aircraft registration to create",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = FaaAircraftRegistration.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "FAA aircraft registration created",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/faa-aircraft-registrations/{nNumber}",
                    method = RequestMethod.PUT,
                    beanClass = FaaAircraftRegistrationHandler.class,
                    beanMethod = "updateFaaAircraftRegistration",
                    operation = @Operation(
                            operationId = "updateFaaAircraftRegistration",
                            summary = "Update an existing FAA aircraft registration",
                            tags = {"FAA Aircraft Registration 📝"},
                            parameters = {
                                    @Parameter(name = "nNumber", in = ParameterIn.PATH, required = true, 
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "FAA aircraft registration to update",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = FaaAircraftRegistration.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "FAA aircraft registration updated",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "FAA aircraft registration not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/faa-aircraft-registrations/{nNumber}",
                    method = RequestMethod.DELETE,
                    beanClass = FaaAircraftRegistrationHandler.class,
                    beanMethod = "deleteFaaAircraftRegistration",
                    operation = @Operation(
                            operationId = "deleteFaaAircraftRegistration",
                            summary = "Delete FAA aircraft registration by N-Number",
                            tags = {"FAA Aircraft Registration 📝"},
                            parameters = {
                                    @Parameter(name = "nNumber", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "FAA aircraft registration deleted"),
                                    @ApiResponse(responseCode = "404", description = "FAA aircraft registration not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/faa-aircraft-registrations/engine-mfr-model-code/{engineMfrModelCode}",
                    method = RequestMethod.GET,
                    beanClass = FaaAircraftRegistrationHandler.class,
                    beanMethod = "getFaaAircraftRegistrationsByEngineMfrModelCode",
                    operation = @Operation(
                            operationId = "getFaaAircraftRegistrationsByEngineMfrModelCode",
                            summary = "Get FAA aircraft registrations by engine manufacturer model code",
                            description = "Returns a list of FAA aircraft registrations 📝 by engine manufacturer model code",
                            tags = {"FAA Aircraft Registration 📝"},
                            parameters = {
                                    @Parameter(name = "engineMfrModelCode", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/faa-aircraft-registrations/type-aircraft/{typeAircraft}",
                    method = RequestMethod.GET,
                    beanClass = FaaAircraftRegistrationHandler.class,
                    beanMethod = "getFaaAircraftRegistrationsByTypeAircraft",
                    operation = @Operation(
                            operationId = "getFaaAircraftRegistrationsByTypeAircraft",
                            summary = "Get FAA aircraft registrations by type aircraft",
                            description = "Returns a list of FAA aircraft registrations 📝 by type aircraft",
                            tags = {"FAA Aircraft Registration 📝"},
                            parameters = {
                                    @Parameter(name = "typeAircraft", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/faa-aircraft-registrations/aircraft-mfr-model-code/{aircraftMfrModelCode}",
                    method = RequestMethod.GET,
                    beanClass = FaaAircraftRegistrationHandler.class,
                    beanMethod = "getFaaAircraftRegistrationsByAircraftMfrModelCode",
                    operation = @Operation(
                            operationId = "getFaaAircraftRegistrationsByAircraftMfrModelCode",
                            summary = "Get FAA aircraft registrations by aircraft manufacturer model code",
                            description = "Returns a list of FAA aircraft registrations 📝 by aircraft manufacturer model code",
                            tags = {"FAA Aircraft Registration 📝"},
                            parameters = {
                                    @Parameter(name = "aircraftMfrModelCode", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/faa-aircraft-registrations/mode-s-code/{modeSCode}",
                    method = RequestMethod.GET,
                    beanClass = FaaAircraftRegistrationHandler.class,
                    beanMethod = "getFaaAircraftRegistrationsByModeSCode",
                    operation = @Operation(
                            operationId = "getFaaAircraftRegistrationsByModeSCode",
                            summary = "Get FAA aircraft registrations by Mode S Code",
                            description = "Returns a list of FAA aircraft registrations 📝 by Mode S Code",
                            tags = {"FAA Aircraft Registration 📝"},
                            parameters = {
                                    @Parameter(name = "modeSCode", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/faa-aircraft-registrations/mode-scode-hex/{modeScodeHex}",
                    method = RequestMethod.GET,
                    beanClass = FaaAircraftRegistrationHandler.class,
                    beanMethod = "getFaaAircraftRegistrationsByModeScodeHex",
                    operation = @Operation(
                            operationId = "getFaaAircraftRegistrationsByModeScodeHex",
                            summary = "Get FAA aircraft registrations by Mode S Code Hex",
                            description = "Returns a list of FAA aircraft registrations 📝 by Mode S Code Hex",
                            tags = {"FAA Aircraft Registration 📝"},
                            parameters = {
                                    @Parameter(name = "modeScodeHex", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> faaAircraftRegistrationRoutesFunction() {
        return RouterFunctions.route()
                .GET("faa-aircraft-registrations", accept(MediaType.APPLICATION_JSON), faaAircraftRegistrationHandler::getAllFaaAircraftRegistrations)
                .GET("faa-aircraft-registrations/{nNumber}", accept(MediaType.APPLICATION_JSON), faaAircraftRegistrationHandler::getFaaAircraftRegistrationByNNumber)
                .GET("faa-aircraft-registrations/engine-mfr-model-code/{engineMfrModelCode}", accept(MediaType.APPLICATION_JSON), faaAircraftRegistrationHandler::getFaaAircraftRegistrationsByEngineMfrModelCode)
                .GET("faa-aircraft-registrations/type-aircraft/{typeAircraft}", accept(MediaType.APPLICATION_JSON), faaAircraftRegistrationHandler::getFaaAircraftRegistrationsByTypeAircraft)
                .GET("faa-aircraft-registrations/aircraft-mfr-model-code/{aircraftMfrModelCode}", accept(MediaType.APPLICATION_JSON), faaAircraftRegistrationHandler::getFaaAircraftRegistrationsByAircraftMfrModelCode)
                .GET("faa-aircraft-registrations/mode-s-code/{modeSCode}", accept(MediaType.APPLICATION_JSON), faaAircraftRegistrationHandler::getFaaAircraftRegistrationsByModeSCode)
                .GET("faa-aircraft-registrations/mode-scode-hex/{modeScodeHex}", accept(MediaType.APPLICATION_JSON), faaAircraftRegistrationHandler::getFaaAircraftRegistrationsByModeScodeHex)
                .POST("faa-aircraft-registrations", accept(MediaType.APPLICATION_JSON), faaAircraftRegistrationHandler::createFaaAircraftRegistration)
                .PUT("faa-aircraft-registrations/{nNumber}", accept(MediaType.APPLICATION_JSON), faaAircraftRegistrationHandler::updateFaaAircraftRegistration)
                .DELETE("faa-aircraft-registrations/{nNumber}", faaAircraftRegistrationHandler::deleteFaaAircraftRegistration)
                .build();
    }
}
