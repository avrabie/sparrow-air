package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.entities.Country;
import com.execodex.sparrowair2.handlers.CountryHandler;
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
public class CountryRoutes {

    private final CountryHandler countryHandler;

    public CountryRoutes(CountryHandler countryHandler) {
        this.countryHandler = countryHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/gds/countries",
                    method = RequestMethod.GET,
                    beanClass = CountryHandler.class,
                    beanMethod = "getAllCountries",
                    operation = @Operation(
                            operationId = "getAllCountries",
                            summary = "Get all countries",
                            description = "Returns a list of all countries 🌍",
                            tags = {"General Data Sets 🌐"},
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
                    path = "/gds/countries/name/{name}",
                    method = RequestMethod.GET,
                    beanClass = CountryHandler.class,
                    beanMethod = "getCountriesByNameSimilarity",
                    operation = @Operation(
                            operationId = "getCountriesByNameSimilarity",
                            summary = "Get countries by name similarity",
                            description = "Returns a list of countries with names similar to the provided name",
                            tags = {"General Data Sets 🌐"},
                            parameters = {
                                    @Parameter(name = "name", in = ParameterIn.PATH, required = true,
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
                    path = "/gds/countries/{code}",
                    method = RequestMethod.GET,
                    beanClass = CountryHandler.class,
                    beanMethod = "getCountryByCode",
                    operation = @Operation(
                            operationId = "getCountryByCode",
                            summary = "Get country by code",
                            description = "Returns a country by its ISO code",
                            tags = {"General Data Sets 🌐"},
                            parameters = {
                                    @Parameter(name = "code", in = ParameterIn.PATH, required = true,
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
                                            description = "Country not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/gds/countries",
                    method = RequestMethod.POST,
                    beanClass = CountryHandler.class,
                    beanMethod = "createCountry",
                    operation = @Operation(
                            operationId = "createCountry",
                            summary = "Create a new country",
                            tags = {"General Data Sets 🌐"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Country to create",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Country.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Country created",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "Country with code already exists"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/gds/countries/{code}",
                    method = RequestMethod.PUT,
                    beanClass = CountryHandler.class,
                    beanMethod = "updateCountry",
                    operation = @Operation(
                            operationId = "updateCountry",
                            summary = "Update an existing country",
                            tags = {"General Data Sets 🌐"},
                            parameters = {
                                    @Parameter(name = "code", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Country to update",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Country.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Country updated",
                                            content = @Content(mediaType = "application/json")
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Country not found"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/gds/countries/{code}",
                    method = RequestMethod.DELETE,
                    beanClass = CountryHandler.class,
                    beanMethod = "deleteCountry",
                    operation = @Operation(
                            operationId = "deleteCountry",
                            summary = "Delete country by code",
                            tags = {"General Data Sets 🌐"},
                            parameters = {
                                    @Parameter(name = "code", in = ParameterIn.PATH, required = true,
                                              content = @Content(schema = @Schema(type = "string")))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Country deleted"),
                                    @ApiResponse(responseCode = "404", description = "Country not found")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> countryRoutesFunction() {
        return RouterFunctions.route()
                .GET("/gds/countries", accept(MediaType.APPLICATION_JSON), countryHandler::getAllCountries)
                .GET("/gds/countries/name/{name}", accept(MediaType.APPLICATION_JSON), countryHandler::getCountriesByNameSimilarity)
                .GET("/gds/countries/{code}", accept(MediaType.APPLICATION_JSON), countryHandler::getCountryByCode)
                .POST("/gds/countries", accept(MediaType.APPLICATION_JSON), countryHandler::createCountry)
                .PUT("/gds/countries/{code}", accept(MediaType.APPLICATION_JSON), countryHandler::updateCountry)
                .DELETE("/gds/countries/{code}", countryHandler::deleteCountry)
                .build();
    }
}
