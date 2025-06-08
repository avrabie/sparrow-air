package com.execodex.sparrowair2.services.computing;

import com.execodex.sparrowair2.entities.Airport;
import com.execodex.sparrowair2.entities.Flight;
import com.execodex.sparrowair2.services.AirportService;
import com.execodex.sparrowair2.services.FlightService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
     * @return A Mono emitting a map of airports to their destination airports
     */
    public Mono<Map<Airport, List<Airport>>> computeFlightsMatrix() {
        // If we already have computed the matrix, return it
        if (!airportListMap.isEmpty()) {
            return Mono.just(airportListMap);
        }

        return refreshFlightsMatrix()
                .doOnNext(matrix -> airportListMap = matrix);
    }

    /**
     * Refreshes the flights matrix by recomputing it.
     * This is useful when flights or airports are added, updated, or deleted.
     *
     * @return A Mono emitting the refreshed flights matrix
     */
    public Mono<Map<Airport, List<Airport>>> refreshFlightsMatrix() {
        Map<String, Airport> airportMap = new HashMap<>();

        // First, get all airports and create a map of ICAO code to Airport
        return airportService.getAllAirports()
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
                });
    }

    public Flux<Map<String, Collection<String>>> airpotToAirportsIcao() {

        Flux<String> airportIcaoCodes = airportService.getAllAirports().map(Airport::getIcaoCode);
        Flux<Map<String, Collection<String>>> airpotToAirportsFlights =
                airportIcaoCodes
                        .flatMap(airportIcaoCode -> flightService
                                .getAllFlightsFromAirportCode(airportIcaoCode)
                                .collectMultimap(flight -> flight.getDepartureAirportIcao(),
                                        flight -> flight.getArrivalAirportIcao())
                        );
        return airpotToAirportsFlights;
    }

    public Flux<Map<String, Collection<Flight>>> airpotsToFlights() {

        Flux<String> airportIcaoCodes = airportService.getAllAirports().map(Airport::getIcaoCode);
        Flux<Map<String, Collection<Flight>>> airpotToAirportsFlights =
                airportIcaoCodes
                        .flatMap(airportIcaoCode -> flightService
                                .getAllFlightsFromAirportCode(airportIcaoCode)
                                .collectMultimap(flight -> flight.getDepartureAirportIcao(),
                                        flight -> flight)
                        );
        return airpotToAirportsFlights;
    }

    // this method will determine the route from one airport to another and will return the list Flights
    public Mono<List<Flight>> getRoute(Airport departureAirport, Airport arrivalAirport) {
        // If departure and arrival are the same, return empty route
        if (departureAirport.getIcaoCode().equals(arrivalAirport.getIcaoCode())) {
            return Mono.just(new ArrayList<>());
        }

        List<Flight> route = new ArrayList<>();
        List<Airport> visitedAirports = new ArrayList<>();
        visitedAirports.add(departureAirport);

        // Start the search from the departure airport
        return findRoute(departureAirport, arrivalAirport, route, visitedAirports)
                .thenReturn(route);
    }

    private Mono<Void> findRoute(Airport departureAirport, Airport arrivalAirport, List<Flight> route, List<Airport> visitedAirports) {
        // Get all flights from the departure airport

        return flightService.getAllFlightsFromAirportCode(departureAirport.getIcaoCode())
                .collectList()
                .flatMap(flights -> {
                    if (flights.isEmpty()) {
                        return Mono.empty(); // No flights from this airport
                    }

                    // Process each flight recursively
                    return processFlights(flights, departureAirport, arrivalAirport, route, visitedAirports, 0);
                })
                .switchIfEmpty(Mono.empty());
    }

    private Mono<Void> processFlights(Collection<Flight> flights, Airport departureAirport, Airport arrivalAirport,
                                      List<Flight> route, List<Airport> visitedAirports, int index) {
        // Base case: if we've processed all flights
        if (index >= flights.size()) {
            return Mono.empty();
        }

        // Get the current flight
        Flight flight = (Flight) flights.toArray()[index];
        String nextAirportIcao = flight.getArrivalAirportIcao();

        // Skip if we've already visited this airport (to avoid cycles)
        boolean alreadyVisited = visitedAirports.stream()
                .anyMatch(airport -> airport.getIcaoCode().equals(nextAirportIcao));

        if (alreadyVisited) {
            // Try the next flight
            return processFlights(flights, departureAirport, arrivalAirport, route, visitedAirports, index + 1);
        }

        // Get the next airport
        return airportService.getAirportByIcaoCode(nextAirportIcao)
                .flatMap(nextAirport -> {
                    // Add the flight to the route
                    route.add(flight);

                    // Add the next airport to visited airports
                    visitedAirports.add(nextAirport);

                    // If we've reached the arrival airport, we're done
                    if (nextAirportIcao.equals(arrivalAirport.getIcaoCode())) {
                        return Mono.empty();
                    }

                    // Continue the search from the next airport
                    return findRoute(nextAirport, arrivalAirport, route, visitedAirports)
                            .then(Mono.defer(() -> {
                                // If we've found a route to the arrival airport, we're done
                                if (route.stream().anyMatch(f -> f.getArrivalAirportIcao().equals(arrivalAirport.getIcaoCode()))) {
                                    return Mono.empty();
                                }

                                // If we haven't found a route, backtrack
                                route.remove(flight);
                                visitedAirports.remove(nextAirport);

                                // Try the next flight
                                return processFlights(flights, departureAirport, arrivalAirport, route, visitedAirports, index + 1);
                            }));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // If airport not found, try the next flight
                    return processFlights(flights, departureAirport, arrivalAirport, route, visitedAirports, index + 1);
                }));
    }


    // Using Dijkstra's algorithm to find the minimum cost route
    public Mono<List<Flight>> getRouteMinimumCost(Airport departureAirport, Airport arrivalAirport){
        // If departure and arrival are the same, return empty route
        if (departureAirport.getIcaoCode().equals(arrivalAirport.getIcaoCode())) {
            return Mono.just(new ArrayList<>());
        }

        // Get all airports and flights to build the graph
        Mono<List<Flight>> result = airportService.getAllAirports()
                .collectList()
                .flatMap(airports -> {
                    // Create a map of ICAO code to Airport for easy lookup
                    Map<String, Airport> airportMap = new HashMap<>();
                    for (Airport airport : airports) {
                        airportMap.put(airport.getIcaoCode(), airport);
                    }

                    // Get all flights to build the graph
                    return flightService.getAllFlights()
                            .collectList()
                            .map(flights -> {
                                // Apply Dijkstra's algorithm to find the shortest path
                                return applyDijkstra(departureAirport, arrivalAirport, airports, flights, airportMap);
                            });
                });
        return result;
    }

    private List<Flight> applyDijkstra(Airport source, Airport target, List<Airport> airports, List<Flight> flights, Map<String, Airport> airportMap) {
        // Create a map of airport ICAO code to its outgoing flights
        Map<String, List<Flight>> outgoingFlights = new HashMap<>();
        for (Flight flight : flights) {
            outgoingFlights.computeIfAbsent(flight.getDepartureAirportIcao(), k -> new ArrayList<>()).add(flight);
        }

        // Initialize distances with infinity for all airports except the source
        Map<String, Double> distances = new HashMap<>();
        Map<String, Flight> previousFlights = new HashMap<>();
        Set<String> unvisited = new HashSet<>();

        for (Airport airport : airports) {
            String icaoCode = airport.getIcaoCode();
            distances.put(icaoCode, Double.MAX_VALUE);
            unvisited.add(icaoCode);
        }
        distances.put(source.getIcaoCode(), 0.0);

        // Dijkstra's algorithm
        while (!unvisited.isEmpty()) {
            // Find the unvisited airport with the smallest distance
            String current = null;
            double smallestDistance = Double.MAX_VALUE;
            for (String icaoCode : unvisited) {
                double distance = distances.get(icaoCode);
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    current = icaoCode;
                }
            }

            // If we've reached the target or there's no path to any remaining unvisited airports
            if (current == null || current.equals(target.getIcaoCode())) {
                break;
            }

            // Remove the current airport from unvisited
            unvisited.remove(current);

            // Get the current airport
            Airport currentAirport = airportMap.get(current);

            // Update distances to neighbors
            List<Flight> currentOutgoingFlights = outgoingFlights.getOrDefault(current, Collections.emptyList());
            for (Flight flight : currentOutgoingFlights) {
                String neighborIcao = flight.getArrivalAirportIcao();
                if (!unvisited.contains(neighborIcao)) {
                    continue; // Skip already visited airports
                }

                Airport neighborAirport = airportMap.get(neighborIcao);
                double distance = airportService.distance(currentAirport, neighborAirport);
                double newDistance = distances.get(current) + distance;

                if (newDistance < distances.get(neighborIcao)) {
                    distances.put(neighborIcao, newDistance);
                    previousFlights.put(neighborIcao, flight);
                }
            }
        }

        // Reconstruct the path
        List<Flight> path = new ArrayList<>();
        String current = target.getIcaoCode();

        // If there's no path to the target
        if (!previousFlights.containsKey(current)) {
            return path; // Return empty path
        }

        // Build the path by following the previous flights
        while (!current.equals(source.getIcaoCode())) {
            Flight flight = previousFlights.get(current);
            path.add(0, flight); // Add to the beginning of the list
            current = flight.getDepartureAirportIcao();
        }

        return path;
    }
}
