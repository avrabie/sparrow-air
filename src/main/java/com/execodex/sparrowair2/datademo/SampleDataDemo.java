package com.execodex.sparrowair2.datademo;


import com.execodex.sparrowair2.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SampleDataDemo {

    public static List<Airport> getDemoAirports() {
        List<Airport> sampleAirports = Arrays.asList(
                Airport.builder()
                        .icaoCode("KJFK")
                        .name("John F. Kennedy International Airport")
                        .city("New York")
                        .country("United States")
                        .timezone("America/New_York")
                        .latitude(40.6413)
                        .longitude(-73.7781)
                        .build(),
                Airport.builder()
                        .icaoCode("EGLL")
                        .name("London Heathrow Airport")
                        .city("London")
                        .country("United Kingdom")
                        .timezone("Europe/London")
                        .latitude(51.4700)
                        .longitude(-0.4543)
                        .build(),
                Airport.builder()
                        .icaoCode("RJTT")
                        .name("Tokyo Haneda Airport")
                        .city("Tokyo")
                        .country("Japan")
                        .timezone("Asia/Tokyo")
                        .latitude(35.5494)
                        .longitude(139.7798)
                        .build(),
                Airport.builder()
                        .icaoCode("YSSY")
                        .name("Sydney Kingsford Smith Airport")
                        .city("Sydney")
                        .country("Australia")
                        .timezone("Australia/Sydney")
                        .latitude(-33.9399)
                        .longitude(151.1753)
                        .build(),
                Airport.builder()
                        .icaoCode("EDDF")
                        .name("Frankfurt Airport")
                        .city("Frankfurt")
                        .country("Germany")
                        .timezone("Europe/Berlin")
                        .latitude(50.0379)
                        .longitude(8.5622)
                        .build(),
                Airport.builder()
                        .icaoCode("LUKK")
                        .name("Chisinau International Airport")
                        .city("Chisinau")
                        .country("Moldova")
                        .timezone("Europe/Chisinau")
                        .latitude(46.9271)
                        .longitude(28.9302)
                        .build(),
                Airport.builder()
                        .icaoCode("LROP")
                        .name("Bucharest Henri Coanda International Airport")
                        .city("Bucharest")
                        .country("Romania")
                        .timezone("Europe/Bucharest")
                        .latitude(44.5711)
                        .longitude(26.0858)
                        .build(),
                Airport.builder()
                        .icaoCode("LFSB")
                        .name("EuroAirport Basel-Mulhouse-Freiburg")
                        .city("Basel")
                        .country("Switzerland")
                        .timezone("Europe/Zurich")
                        .latitude(47.5915)
                        .longitude(7.5294)
                        .build(),
                Airport.builder()
                        .icaoCode("LSZH")
                        .name("Zurich Airport")
                        .city("Zurich")
                        .country("Switzerland")
                        .timezone("Europe/Zurich")
                        .latitude(47.4502)
                        .longitude(8.5619)
                        .build(),
                Airport.builder()
                        .icaoCode("OMDB")
                        .name("Dubai International Airport")
                        .city("Dubai")
                        .country("United Arab Emirates")
                        .timezone("Asia/Dubai")
                        .latitude(25.2532)
                        .longitude(55.3657)
                        .build(),
                Airport.builder()
                        .icaoCode("KIAD")
                        .name("Washington Dulles International Airport")
                        .city("Washington, D.C.")
                        .country("United States")
                        .timezone("America/New_York")
                        .latitude(38.9531)
                        .longitude(-77.4470)
                        .build()

        );
        return sampleAirports;
    }

    public static List<AircraftType> getDemoAircraftTypes() {
        List<AircraftType> sampleAircraftTypes = Arrays.asList(
                AircraftType.builder()
                        .icaoCode("B738")
                        .modelName("737-800")
                        .manufacturer("Boeing")
                        .seatingCapacity(189)
                        .maxRangeKm(5765)
                        .mtow(79010)
                        .build(),
                AircraftType.builder()
                        .icaoCode("A320")
                        .modelName("A320-200")
                        .manufacturer("Airbus")
                        .seatingCapacity(180)
                        .maxRangeKm(6100)
                        .mtow(77000)
                        .build(),
                AircraftType.builder()
                        .icaoCode("B77W")
                        .modelName("777-300ER")
                        .manufacturer("Boeing")
                        .seatingCapacity(396)
                        .maxRangeKm(13650)
                        .mtow(351500)
                        .build(),
                AircraftType.builder()
                        .icaoCode("A388")
                        .modelName("A380-800")
                        .manufacturer("Airbus")
                        .seatingCapacity(853)
                        .maxRangeKm(15700)
                        .mtow(575000)
                        .build(),
                AircraftType.builder()
                        .icaoCode("E190")
                        .modelName("E190")
                        .manufacturer("Embraer")
                        .seatingCapacity(114)
                        .maxRangeKm(4537)
                        .mtow(51800)
                        .build(),
                AircraftType.builder()
                        .icaoCode("CRJ2")
                        .modelName("CRJ-200")
                        .manufacturer("Bombardier")
                        .seatingCapacity(50)
                        .maxRangeKm(3150)
                        .mtow(23133)
                        .build(),
                AircraftType.builder()
                        .modelName("ATR 72-500")
                        .icaoCode("AT75")
                        .manufacturer("ATR")
                        .seatingCapacity(70)
                        .maxRangeKm(1500)
                        .mtow(22000)
                        .build()
        );
        return sampleAircraftTypes;
    }

    public static List<Airline> getSampleAirlines(){
        List<Airline> sampleAirlines = Arrays.asList(
                Airline.builder()
                        .icaoCode("AAL")
                        .name("American Airlines")
                        .headquarters("Fort Worth, Texas, United States")
                        .contactNumber("+1-800-433-7300")
                        .website("https://www.aa.com")
                        .build(),
                Airline.builder()
                        .icaoCode("BAW")
                        .name("British Airways")
                        .headquarters("London, United Kingdom")
                        .contactNumber("+44-20-8738-5050")
                        .website("https://www.britishairways.com")
                        .build(),
                Airline.builder()
                        .icaoCode("DLH")
                        .name("Lufthansa")
                        .headquarters("Cologne, Germany")
                        .contactNumber("+49-69-86-799-799")
                        .website("https://www.lufthansa.com")
                        .build(),
                Airline.builder()
                        .icaoCode("UAE")
                        .name("Emirates")
                        .headquarters("Dubai, United Arab Emirates")
                        .contactNumber("+971-600-555-555")
                        .website("https://www.emirates.com")
                        .build(),
                Airline.builder()
                        .icaoCode("SIA")
                        .name("Singapore Airlines")
                        .headquarters("Singapore")
                        .contactNumber("+65-6223-8888")
                        .website("https://www.singaporeair.com")
                        .build(),
                Airline.builder()
                        .icaoCode("THY")
                        .name("Turkish Airlines")
                        .headquarters("Istanbul, Turkey")
                        .contactNumber("+90-212-463-6363")
                        .website("https://www.turkishairlines.com")
                        .build(),
                Airline.builder()
                        .icaoCode("WZZ")
                        .name("Wizz Air")
                        .headquarters("Budapest, Hungary")
                        .contactNumber("+36-1-777-9300")
                        .website("https://wizzair.com")
                        .build(),
                Airline.builder()
                        .icaoCode("TAR")
                        .name("TAROM")
                        .headquarters("Bucharest, Romania")
                        .contactNumber("+40-21-303-4444")
                        .website("https://www.tarom.ro")
                        .build()
        );
        return sampleAirlines;
    }


    public static List<AirlineFleet> getSampleAirlineFleet() {
        List<AirlineFleet> sampleAirlineFleet = Arrays.asList(
                AirlineFleet.builder()
                        .aircraftTypeIcao("B738")
                        .airlineIcao("AAL")
                        .registrationNumber("UA12345")
                        .aircraftAge(LocalDate.of(2015, 5, 12))
                        .seatConfiguration("3-3")
                        .hasWifi(true)
                        .hasPowerOutlets(true)
                        .hasEntertainmentSystem(true)
                        .firstClassSeats(8)
                        .businessSeats(14)
                        .premiumEconomySeats(10)
                        .economySeats(126)
                        .build(),
                AirlineFleet.builder()
                        .aircraftTypeIcao("A320")
                        .airlineIcao("BAW")
                        .registrationNumber("UA67890")
                        .aircraftAge(LocalDate.of(2018, 3, 24))
                        .seatConfiguration("3-3")
                        .hasWifi(true)
                        .hasPowerOutlets(true)
                        .hasEntertainmentSystem(false)
                        .firstClassSeats(0)
                        .businessSeats(32)
                        .premiumEconomySeats(10)
                        .economySeats(120)
                        .build(),
                AirlineFleet.builder()
                        .aircraftTypeIcao("B77W")
                        .airlineIcao("DLH")
                        .registrationNumber("D-AILM")
                        .aircraftAge(LocalDate.of(2012, 11, 7))
                        .seatConfiguration("3-4-3")
                        .hasWifi(true)
                        .hasPowerOutlets(true)
                        .hasEntertainmentSystem(true)
                        .firstClassSeats(8)
                        .businessSeats(22)
                        .premiumEconomySeats(14)
                        .economySeats(304)
                        .build(),
                AirlineFleet.builder()
                        .aircraftTypeIcao("A388")
                        .airlineIcao("UAE")
                        .registrationNumber("A6-EDB")
                        .aircraftAge(LocalDate.of(2010, 8, 15))
                        .seatConfiguration("3-4-3")
                        .hasWifi(true)
                        .hasPowerOutlets(true)
                        .hasEntertainmentSystem(true)
                        .firstClassSeats(14)
                        .businessSeats(76)
                        .premiumEconomySeats(10)
                        .economySeats(427)
                        .build(),
                AirlineFleet.builder()
                        .aircraftTypeIcao("E190")
                        .airlineIcao("WZZ")
                        .registrationNumber("HA-LYF")
                        .aircraftAge(LocalDate.of(2019, 2, 3))
                        .seatConfiguration("2-2")
                        .hasWifi(false)
                        .hasPowerOutlets(true)
                        .hasEntertainmentSystem(false)
                        .firstClassSeats(0)
                        .businessSeats(12)
                        .premiumEconomySeats(5)
                        .economySeats(88)
                        .build(),
                // Add a fleet entry for the ATR 72-500
                AirlineFleet.builder()
                        .aircraftTypeIcao("AT75")
                        .airlineIcao("TAR")
                        .registrationNumber("YR-ATS")
                        .aircraftAge(LocalDate.of(2010, 6, 20))
                        .seatConfiguration("2-2")
                        .hasWifi(false)
                        .hasPowerOutlets(false)
                        .hasEntertainmentSystem(false)
                        .firstClassSeats(0)
                        .businessSeats(14)
                        .premiumEconomySeats(0)
                        .economySeats(56)
                        .build()
        );
        return sampleAirlineFleet;
    }

    public static List<Flight>  getSampleFlights(){
        List<Flight> sampleFlights = Arrays.asList(
                Flight.builder()
                        .airlineIcaoCode("AAL")
                        .flightNumber("AA123")
                        .departureAirportIcao("KJFK")
                        .arrivalAirportIcao("EGLL")
                        .scheduledDeparture(LocalDateTime.now().plusDays(1))
                        .scheduledArrival(LocalDateTime.now().plusDays(1).plusHours(7))
                        .airlineFleetId(1L)
                        .status("Scheduled")
                        .build(),
                Flight.builder()
                        .airlineIcaoCode("BAW")
                        .flightNumber("BA456")
                        .departureAirportIcao("EGLL")
                        .arrivalAirportIcao("RJTT")
                        .scheduledDeparture(LocalDateTime.now().plusDays(2))
                        .scheduledArrival(LocalDateTime.now().plusDays(2).plusHours(12))
                        .airlineFleetId(2L)
                        .status("Scheduled")
                        .build(),
                Flight.builder()
                        .airlineIcaoCode("DLH")
                        .flightNumber("LH789")
                        .departureAirportIcao("EDDF")
                        .arrivalAirportIcao("KJFK")
                        .scheduledDeparture(LocalDateTime.now().plusDays(3))
                        .scheduledArrival(LocalDateTime.now().plusDays(3).plusHours(9))
                        .airlineFleetId(3L) // Using BAW's A320 as fallback
                        .status("Scheduled")
                        .build(),
                Flight.builder()
                        .airlineIcaoCode("UAE")
                        .flightNumber("EK101")
                        .departureAirportIcao("OMDB")
                        .arrivalAirportIcao("YSSY")
                        .scheduledDeparture(LocalDateTime.now().plusDays(4))
                        .scheduledArrival(LocalDateTime.now().plusDays(4).plusHours(14))
                        .airlineFleetId(4L) // Using UAE's A388
                        .status("Scheduled")
                        .build(),
                // Add a flight from LUKK to LROP
                Flight.builder()
                        .airlineIcaoCode("TAR")
                        .flightNumber("RO123")
                        .departureAirportIcao("LUKK")
                        .arrivalAirportIcao("LROP")
                        .scheduledDeparture(LocalDateTime.now().plusDays(5))
                        .scheduledArrival(LocalDateTime.now().plusDays(5).plusHours(2))
                        .airlineFleetId(5L) // Using TAR's ATR 72-500
                        .status("Scheduled")
                        .build(),
                // Add a flight from LROP to LFSB
                Flight.builder()
                        .airlineIcaoCode("TAR")
                        .flightNumber("RO456")
                        .departureAirportIcao("LROP")
                        .arrivalAirportIcao("LFSB")
                        .scheduledDeparture(LocalDateTime.now().plusDays(6))
                        .scheduledArrival(LocalDateTime.now().plusDays(6).plusHours(1))
                        .airlineFleetId(5L) // Using TAR's ATR 72-500
                        .status("Scheduled")
                        .build()
        );


        return sampleFlights;
    }
}
