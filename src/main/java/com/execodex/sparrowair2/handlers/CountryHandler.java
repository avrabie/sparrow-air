package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.gds.Country;
import com.execodex.sparrowair2.services.CountryService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class CountryHandler {

    private final CountryService countryService;

    public CountryHandler(CountryService countryService) {
        this.countryService = countryService;
    }

    // Get all countries
    public Mono<ServerResponse> getAllCountries(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(countryService.getAllCountries(), Country.class)
                .onErrorResume(this::handleError);
    }

    // Get countries by name similarity
    public Mono<ServerResponse> getCountriesByNameSimilarity(ServerRequest request) {
        String name = request.pathVariable("name");
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(countryService.getCountriesByNameSimilarity(name), Country.class)
                .onErrorResume(this::handleError);
    }

    // Get country by code
    public Mono<ServerResponse> getCountryByCode(ServerRequest request) {
        String code = request.pathVariable("code");
        return countryService.getCountryByCode(code)
                .flatMap(country -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(country))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Create a new country
    public Mono<ServerResponse> createCountry(ServerRequest request) {
        return request.bodyToMono(Country.class)
                .flatMap(countryService::createCountry)
                .flatMap(country -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(country))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("Country with code already exists");
                    }
                    return handleError(e);
                });
    }

    // Update an existing country
    public Mono<ServerResponse> updateCountry(ServerRequest request) {
        String code = request.pathVariable("code");
        return request.bodyToMono(Country.class)
                .flatMap(country -> countryService.updateCountry(code, country))
                .flatMap(updatedCountry -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedCountry))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Delete a country
    public Mono<ServerResponse> deleteCountry(ServerRequest request) {
        String code = request.pathVariable("code");
        return countryService.getCountryByCode(code)
                .flatMap(country -> countryService.deleteCountry(code)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in CountryHandler occurred: " + error.getMessage());
    }
}
