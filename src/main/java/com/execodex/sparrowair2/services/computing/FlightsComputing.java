package com.execodex.sparrowair2.services.computing;

import com.execodex.sparrowair2.entities.Airport;
import com.execodex.sparrowair2.entities.Flight;
import com.execodex.sparrowair2.services.AirportService;
import com.execodex.sparrowair2.services.FlightService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
public class FlightsComputing {
    private final AirportService airportService;
    private final FlightService flightService;
    private Map<Airport, List<Airport>> airportListMap = new HashMap<>();

    public FlightsComputing(AirportService airportService, FlightService flightService) {
        this.airportService = airportService;
        this.flightService = flightService;
    }
    // This class is responsible for computing flight-related data
    // it will show a matrix of all flights between airports

    // first we need to get all the airports
    // then we need to get all the flights between airports
    // then we need to compute the matrix of flights between airports

    /**
     * Computes a matrix of flights between airports.
     * Returns a map where each key is an airport and the value is a list of airports
     * that can be reached directly from the key airport.
     *
     * @return A map of airports to their destination airports
     */
    public Map<Airport, List<Airport>> computeFlightsMatrix() {
        // If we already have computed the matrix, return it
        if (!airportListMap.isEmpty()) {
            return airportListMap;
        }

        return refreshFlightsMatrix();
    }

    /**
     * Refreshes the flights matrix by recomputing it.
     * This is useful when flights or airports are added, updated, or deleted.
     *
     * @return The refreshed flights matrix
     */
    public Map<Airport, List<Airport>> refreshFlightsMatrix() {
        Map<String, Airport> airportMap = new HashMap<>();

        // First, get all airports and create a map of ICAO code to Airport
        airportListMap = airportService.getAllAirports()
                .collectMap(Airport::getIcaoCode, airport -> airport)
                .flatMap(airports -> {
                    airportMap.putAll(airports);

                    // Get all flights
                    return flightService.getAllFlights()
                            .collectMultimap(Flight::getDepartureAirportIcao, flight -> flight.getArrivalAirportIcao());
                })
                .map(flightMap -> {
                    Map<Airport, List<Airport>> result = new HashMap<>();

                    // For each airport, find all its destination airports
                    for (String departureIcao : airportMap.keySet()) {
                        Airport departureAirport = airportMap.get(departureIcao);
                        List<Airport> destinationAirports = new ArrayList<>();

                        // Get all destination ICAO codes for this departure airport
                        if (flightMap.containsKey(departureIcao)) {
                            for (String arrivalIcao : flightMap.get(departureIcao)) {
                                Airport arrivalAirport = airportMap.get(arrivalIcao);
                                if (arrivalAirport != null) {
                                    destinationAirports.add(arrivalAirport);
                                }
                            }
                        }

                        result.put(departureAirport, destinationAirports);
                    }

                    return result;
                })
                .block(); // Block to get the result synchronously

        return airportListMap;
    }

    public Flux<Map<String, Collection<String>>> airpotToAirportsFlights() {


        Flux<String> airportIcaoCodes = airportService.getAllAirports().map(Airport::getIcaoCode);
        Flux<Map<String, Collection<String>>> airpotToAirportsFlights =
                airportIcaoCodes
                        .flatMap(airportIcaoCode -> flightService
                                .getAllFlights()
                                .filter(flight -> flight.getDepartureAirportIcao().equals(airportIcaoCode))
                                .collectMultimap(flight -> flight.getDepartureAirportIcao(),
                                        flight -> flight.getArrivalAirportIcao())
                        );


        return airpotToAirportsFlights;

    }
}
