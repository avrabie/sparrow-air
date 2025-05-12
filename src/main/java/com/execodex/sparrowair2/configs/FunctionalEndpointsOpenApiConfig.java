package com.execodex.sparrowair2.configs;

import com.execodex.sparrowair2.routes.AirportRoutes;
import com.execodex.sparrowair2.routes.HelloRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FunctionalEndpointsOpenApiConfig {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/airports",
                    beanClass = AirportRoutes.class,
                    beanMethod = "airportRoutesFunction",
                    operation = @Operation(
                            operationId = "getAllAirports",
                            summary = "Get all airports",
                            description = "Returns a list of all airports",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            )
//            ,
//            @RouterOperation(
//                    path = "/airports/{icaoCode}",
//                    beanClass = AirportRoutes.class,
//                    beanMethod = "airportRoutesFunction",
//                    operation = @Operation(
//                            operationId = "getAirportByIcaoCode",
//                            summary = "Get airport by ICAO code",
//                            description = "Returns an airport by its ICAO code",
//                            parameters = {
//                                    @Parameter(
//                                            name = "icaoCode",
//                                            in = ParameterIn.PATH,
//                                            required = true,
//                                            description = "ICAO code of the airport"
//                                    )
//                            },
//                            responses = {
//                                    @ApiResponse(
//                                            responseCode = "200",
//                                            description = "Successful operation",
//                                            content = @Content(mediaType = "application/json")
//                                    )
//                                    ,
//                                    @ApiResponse(
//                                            responseCode = "404",
//                                            description = "Airport not found"
//                                    )
//                            }
//                    )
//            )
//            ,
//            @RouterOperation(
//                    path = "/hello",
//                    beanClass = HelloRoutes.class,
//                    beanMethod = "helloRoute",
//                    operation = @Operation(
//                            operationId = "hello",
//                            summary = "Hello endpoint",
//                            description = "Returns a hello message",
//                            responses = {
//                                    @ApiResponse(
//                                            responseCode = "200",
//                                            description = "Successful operation",
//                                            content = @Content(mediaType = "text/plain")
//                                    )
//                            }
//                    )
//            )
            // Add more @RouterOperation annotations for other endpoints as needed
    })


    public RouterFunction<ServerResponse> combinedRoutes(
            HelloRoutes helloRoutes,
            AirportRoutes airportRoutes) {
        return airportRoutes.airportRoutesFunction()
                .and(helloRoutes.helloRoute());
    }
}
