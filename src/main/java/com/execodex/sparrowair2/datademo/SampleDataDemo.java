package com.execodex.sparrowair2.datademo;


import com.execodex.sparrowair2.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SampleDataDemo {




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
                        .build(),
                // Adding Air Canada
                Airline.builder()
                        .icaoCode("ACA")
                        .name("Air Canada")
                        .headquarters("Montreal, Quebec, Canada")
                        .contactNumber("+1-888-247-2262")
                        .website("https://www.aircanada.com")
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
                        .build(),
                // Add a fleet entry for Air Canada
                AirlineFleet.builder()
                        .aircraftTypeIcao("A320")
                        .airlineIcao("ACA")
                        .registrationNumber("C-FDCA")
                        .aircraftAge(LocalDate.of(2016, 8, 10))
                        .seatConfiguration("3-3")
                        .hasWifi(true)
                        .hasPowerOutlets(true)
                        .hasEntertainmentSystem(true)
                        .firstClassSeats(0)
                        .businessSeats(16)
                        .premiumEconomySeats(12)
                        .economySeats(126)
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
                        .build(),
                // Add a flight from LROP to EGLL
                Flight.builder()
                        .airlineIcaoCode("TAR")
                        .flightNumber("RO789")
                        .departureAirportIcao("LROP")
                        .arrivalAirportIcao("EGLL")
                        .scheduledDeparture(LocalDateTime.now().plusDays(7))
                        .scheduledArrival(LocalDateTime.now().plusDays(7).plusHours(3))
                        .airlineFleetId(5L) // Using TAR's ATR 72-500
                        .status("Scheduled")
                        .build(),
                // Add 2 flights in the US by American Airlines (AAL)
                Flight.builder()
                        .airlineIcaoCode("AAL")
                        .flightNumber("AA456")
                        .departureAirportIcao("KLAX")
                        .arrivalAirportIcao("KORD")
                        .scheduledDeparture(LocalDateTime.now().plusDays(8))
                        .scheduledArrival(LocalDateTime.now().plusDays(8).plusHours(4))
                        .airlineFleetId(1L) // Using AAL's B738
                        .status("Scheduled")
                        .build(),
                Flight.builder()
                        .airlineIcaoCode("AAL")
                        .flightNumber("AA789")
                        .departureAirportIcao("KORD")
                        .arrivalAirportIcao("KATL")
                        .scheduledDeparture(LocalDateTime.now().plusDays(9))
                        .scheduledArrival(LocalDateTime.now().plusDays(9).plusHours(2))
                        .airlineFleetId(1L) // Using AAL's B738
                        .status("Scheduled")
                        .build(),
                // Add 1 flight in Canada by Air Canada (ACA)
                Flight.builder()
                        .airlineIcaoCode("ACA")
                        .flightNumber("AC123")
                        .departureAirportIcao("CYYZ")
                        .arrivalAirportIcao("KIAD")
                        .scheduledDeparture(LocalDateTime.now().plusDays(10))
                        .scheduledArrival(LocalDateTime.now().plusDays(10).plusHours(2))
                        .airlineFleetId(6L) // Using ACA's A320
                        .status("Scheduled")
                        .build(),
                Flight.builder()
                        .airlineIcaoCode("ACA")
                        .flightNumber("AC456")
                        .departureAirportIcao("CYYZ")
                        .arrivalAirportIcao("KLAX")
                        .scheduledDeparture(LocalDateTime.now().plusDays(11))
                        .scheduledArrival(LocalDateTime.now().plusDays(11).plusHours(4))
                        .airlineFleetId(6L) // Using ACA's A320
                        .status("Scheduled")
                        .build(),
                //add flight from New York to Basel
                Flight.builder()
                        .airlineIcaoCode("AAL")
                        .flightNumber("AA999")
                        .departureAirportIcao("KJFK")
                        .arrivalAirportIcao("LFSB")
                        .scheduledDeparture(LocalDateTime.now().plusDays(12))
                        .scheduledArrival(LocalDateTime.now().plusDays(12).plusHours(8))
                        .airlineFleetId(1L) // Using AAL's B738
                        .status("Scheduled")
                        .build(),
                // Add a flight from London to New York
                Flight.builder()
                        .airlineIcaoCode("BAW")
                        .flightNumber("BA789")
                        .departureAirportIcao("EGLL")
                        .arrivalAirportIcao("KJFK")
                        .scheduledDeparture(LocalDateTime.now().plusDays(13))
                        .scheduledArrival(LocalDateTime.now().plusDays(13).plusHours(7))
                        .airlineFleetId(2L) // Using BAW's A320
                        .status("Scheduled")
                        .build()

        );


        return sampleFlights;
    }
    public static List<Country> getSampleCountries() {
        List<Country> sampleCountries = Arrays.asList(
                Country.builder().code("US").name("United States").capital("Washington, D.C.").continent("North America").currency("USD").language("English").build(),
                Country.builder().code("CA").name("Canada").capital("Ottawa").continent("North America").currency("CAD").language("English, French").build(),
                Country.builder().code("MX").name("Mexico").capital("Mexico City").continent("North America").currency("MXN").language("Spanish").build(),
                Country.builder().code("BR").name("Brazil").capital("Brasília").continent("South America").currency("BRL").language("Portuguese").build(),
                Country.builder().code("AR").name("Argentina").capital("Buenos Aires").continent("South America").currency("ARS").language("Spanish").build(),
                Country.builder().code("CL").name("Chile").capital("Santiago").continent("South America").currency("CLP").language("Spanish").build(),
                Country.builder().code("CO").name("Colombia").capital("Bogotá").continent("South America").currency("COP").language("Spanish").build(),
                Country.builder().code("PE").name("Peru").capital("Lima").continent("South America").currency("PEN").language("Spanish").build(),
                Country.builder().code("VE").name("Venezuela").capital("Caracas").continent("South America").currency("VES").language("Spanish").build(),
                Country.builder().code("GB").name("United Kingdom").capital("London").continent("Europe").currency("GBP").language("English").build(),
                Country.builder().code("FR").name("France").capital("Paris").continent("Europe").currency("EUR").language("French").build(),
                Country.builder().code("DE").name("Germany").capital("Berlin").continent("Europe").currency("EUR").language("German").build(),
                Country.builder().code("IT").name("Italy").capital("Rome").continent("Europe").currency("EUR").language("Italian").build(),
                Country.builder().code("ES").name("Spain").capital("Madrid").continent("Europe").currency("EUR").language("Spanish").build(),
                Country.builder().code("PT").name("Portugal").capital("Lisbon").continent("Europe").currency("EUR").language("Portuguese").build(),
                Country.builder().code("NL").name("Netherlands").capital("Amsterdam").continent("Europe").currency("EUR").language("Dutch").build(),
                Country.builder().code("BE").name("Belgium").capital("Brussels").continent("Europe").currency("EUR").language("Dutch, French, German").build(),
                Country.builder().code("CH").name("Switzerland").capital("Bern").continent("Europe").currency("CHF").language("German, French, Italian, Romansh").build(),
                Country.builder().code("AT").name("Austria").capital("Vienna").continent("Europe").currency("EUR").language("German").build(),
                Country.builder().code("GR").name("Greece").capital("Athens").continent("Europe").currency("EUR").language("Greek").build(),
                Country.builder().code("SE").name("Sweden").capital("Stockholm").continent("Europe").currency("SEK").language("Swedish").build(),
                Country.builder().code("NO").name("Norway").capital("Oslo").continent("Europe").currency("NOK").language("Norwegian").build(),
                Country.builder().code("DK").name("Denmark").capital("Copenhagen").continent("Europe").currency("DKK").language("Danish").build(),
                Country.builder().code("FI").name("Finland").capital("Helsinki").continent("Europe").currency("EUR").language("Finnish, Swedish").build(),
                Country.builder().code("IE").name("Ireland").capital("Dublin").continent("Europe").currency("EUR").language("English, Irish").build(),
                Country.builder().code("PL").name("Poland").capital("Warsaw").continent("Europe").currency("PLN").language("Polish").build(),
                Country.builder().code("CZ").name("Czech Republic").capital("Prague").continent("Europe").currency("CZK").language("Czech").build(),
                Country.builder().code("HU").name("Hungary").capital("Budapest").continent("Europe").currency("HUF").language("Hungarian").build(),
                Country.builder().code("RO").name("Romania").capital("Bucharest").continent("Europe").currency("RON").language("Romanian").build(),
                Country.builder().code("BG").name("Bulgaria").capital("Sofia").continent("Europe").currency("BGN").language("Bulgarian").build(),
                Country.builder().code("HR").name("Croatia").capital("Zagreb").continent("Europe").currency("EUR").language("Croatian").build(),
                Country.builder().code("RS").name("Serbia").capital("Belgrade").continent("Europe").currency("RSD").language("Serbian").build(),
                Country.builder().code("UA").name("Ukraine").capital("Kyiv").continent("Europe").currency("UAH").language("Ukrainian").build(),
                Country.builder().code("RU").name("Russian Federation").capital("Moscow").continent("Europe/Asia").currency("RUB").language("Russian").build(),
                Country.builder().code("TR").name("Turkey").capital("Ankara").continent("Europe/Asia").currency("TRY").language("Turkish").build(),
                Country.builder().code("JP").name("Japan").capital("Tokyo").continent("Asia").currency("JPY").language("Japanese").build(),
                Country.builder().code("CN").name("China").capital("Beijing").continent("Asia").currency("CNY").language("Mandarin").build(),
                Country.builder().code("IN").name("India").capital("New Delhi").continent("Asia").currency("INR").language("Hindi, English").build(),
                Country.builder().code("KR").name("South Korea").capital("Seoul").continent("Asia").currency("KRW").language("Korean").build(),
                Country.builder().code("ID").name("Indonesia").capital("Jakarta").continent("Asia").currency("IDR").language("Indonesian").build(),
                Country.builder().code("MY").name("Malaysia").capital("Kuala Lumpur").continent("Asia").currency("MYR").language("Malay").build(),
                Country.builder().code("SG").name("Singapore").capital("Singapore").continent("Asia").currency("SGD").language("English, Malay, Mandarin, Tamil").build(),
                Country.builder().code("TH").name("Thailand").capital("Bangkok").continent("Asia").currency("THB").language("Thai").build(),
                Country.builder().code("VN").name("Vietnam").capital("Hanoi").continent("Asia").currency("VND").language("Vietnamese").build(),
                Country.builder().code("PH").name("Philippines").capital("Manila").continent("Asia").currency("PHP").language("Filipino, English").build(),
                Country.builder().code("SA").name("Saudi Arabia").capital("Riyadh").continent("Asia").currency("SAR").language("Arabic").build(),
                Country.builder().code("AE").name("United Arab Emirates").capital("Abu Dhabi").continent("Asia").currency("AED").language("Arabic").build(),
                Country.builder().code("IL").name("Israel").capital("Jerusalem").continent("Asia").currency("ILS").language("Hebrew, Arabic").build(),
                Country.builder().code("AU").name("Australia").capital("Canberra").continent("Oceania").currency("AUD").language("English").build(),
                Country.builder().code("NZ").name("New Zealand").capital("Wellington").continent("Oceania").currency("NZD").language("English, Māori").build(),
                Country.builder().code("FJ").name("Fiji").capital("Suva").continent("Oceania").currency("FJD").language("English, Fijian, Fiji Hindi").build(),
                Country.builder().code("EG").name("Egypt").capital("Cairo").continent("Africa").currency("EGP").language("Arabic").build(),
                Country.builder().code("ZA").name("South Africa").capital("Pretoria").continent("Africa").currency("ZAR").language("Afrikaans, English, and others").build(),
                Country.builder().code("NG").name("Nigeria").capital("Abuja").continent("Africa").currency("NGN").language("English").build(),
                Country.builder().code("KE").name("Kenya").capital("Nairobi").continent("Africa").currency("KES").language("Swahili, English").build(),
                Country.builder().code("MA").name("Morocco").capital("Rabat").continent("Africa").currency("MAD").language("Arabic, Berber").build(),
                Country.builder().code("GH").name("Ghana").capital("Accra").continent("Africa").currency("GHS").language("English").build(),
                Country.builder().code("ET").name("Ethiopia").capital("Addis Ababa").continent("Africa").currency("ETB").language("Amharic").build(),
                Country.builder().code("TZ").name("Tanzania").capital("Dodoma").continent("Africa").currency("TZS").language("Swahili, English").build(),
                Country.builder().code("DZ").name("Algeria").capital("Algiers").continent("Africa").currency("DZD").language("Arabic, Berber").build(),
                Country.builder().code("TN").name("Tunisia").capital("Tunis").continent("Africa").currency("TND").language("Arabic").build(),
                Country.builder().code("LY").name("Libya").capital("Tripoli").continent("Africa").currency("LYD").language("Arabic").build(),
                Country.builder().code("MD").name("Moldova").capital("Chișinău").continent("Europe").currency("MDL").language("Romanian").build()
        );
        return sampleCountries;
    }
}
