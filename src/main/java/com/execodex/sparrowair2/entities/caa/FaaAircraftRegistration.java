package com.execodex.sparrowair2.entities.caa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents an FAA aircraft registration record from a fixed-width text file.
 * Based on the FAA aircraft registration database format.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "faa_aircraft_registration")
public class FaaAircraftRegistration {
    @Id
    private String nNumber; // Positions 1-5: Identification number assigned to aircraft

    private String serialNumber; // Positions 7-36: Complete aircraft serial number
    private String aircraftMfrModelCode; // Positions 38-44: Code for manufacturer, model, and series
    private String engineMfrModelCode; // Positions 46-50: Code for engine manufacturer and model
    private String yearManufactured; // Positions 52-55: Year manufactured

    private String typeRegistrant; // Position 57: Type of registrant (1-9)
    private String registrantName; // Positions 59-108: Registrant's name
    private String street1; // Positions 110-142: Street address
    private String street2; // Positions 144-176: 2nd street address
    private String registrantCity; // Positions 178-195: City name
    private String registrantState; // Positions 197-198: State name
    private String registrantZipCode; // Positions 200-209: Postal Zip Code
    private String registrantRegion; // Position 211: Region code (1-8, C, E, S)

    private String countyMail; // Positions 213-215: County code
    private String countryMail; // Positions 217-218: Country code

    private String lastActivityDate; // Positions 220-227: Format YYYY/MM/DD
    private String certificateIssueDate; // Positions 229-236: Format YYYY/MM/DD

    private String airworthinessClassificationCode; // Position 238: Airworthiness certificate class
    private String approvedOperationCodes; // Positions 239-247: Approved operations

    private String typeAircraft; // Position 249: Type of aircraft (1-9, H, O)
    private String typeEngine; // Positions 251-252: Type of engine (0-11)

    private String statusCode; // Positions 254-255: Status code (limited to 2 characters in database)

    private String modeSCode; // Positions 257-264: Aircraft Transponder Code
    private String fractionalOwnership; // Position 266: Y if fractional ownership, blank otherwise (limited to 2 characters in database)

    // Custom setters to ensure values fit within database constraints
    public void setStatusCode(String statusCode) {
        if (statusCode != null && statusCode.length() > 2) {
            this.statusCode = statusCode.substring(0, 2);
        } else {
            this.statusCode = statusCode;
        }
    }

    public void setFractionalOwnership(String fractionalOwnership) {
        if (fractionalOwnership != null && fractionalOwnership.length() > 2) {
            this.fractionalOwnership = fractionalOwnership.substring(0, 2);
        } else {
            this.fractionalOwnership = fractionalOwnership;
        }
    }

    private String airworthinessDate; // Positions 268-275: Date of Airworthiness

    private String otherName1; // Positions 277-326: 1st co-owner or partnership name
    private String otherName2; // Positions 328-377: 2nd co-owner or partnership name
    private String otherName3; // Positions 379-428: 3rd co-owner or partnership name
    private String otherName4; // Positions 430-479: 4th co-owner or partnership name
    private String otherName5; // Positions 481-530: 5th co-owner or partnership name

    private String expirationDate; // Positions 532-539: Format YYYY/MM/DD
    private String uniqueId; // Positions 541-548: Unique Identification Number

    private String kitMfr; // Positions 550-579: Kit Manufacturer Name
    private String kitModel; // Positions 581-600: Kit Model Name

    private String modeScodeHex; // Positions 602-611: Mode S Code in Hexadecimal Format

//    /**
//     * Parses a line from the FAA aircraft registration fixed-width text file.
//     *
//     * @param line The line to parse
//     * @return A new FaaAircraftRegistration object
//     */
//    public static FaaAircraftRegistration fromFixedWidthLine(String line) {
//        if (line == null || line.length() < 612) {
//            throw new IllegalArgumentException("Line is too short to be a valid FAA aircraft registration record");
//        }
//
//        return FaaAircraftRegistration.builder()
//                .nNumber(line.substring(0, 5).trim())
//                .serialNumber(line.substring(6, 36).trim())
//                .aircraftMfrModelCode(line.substring(37, 44).trim())
//                .engineMfrModelCode(line.substring(45, 50).trim())
//                .yearManufactured(line.substring(51, 55).trim())
//                .typeRegistrant(line.substring(56, 57).trim())
//                .registrantName(line.substring(58, 108).trim())
//                .street1(line.substring(109, 142).trim())
//                .street2(line.substring(143, 176).trim())
//                .registrantCity(line.substring(177, 195).trim())
//                .registrantState(line.substring(196, 198).trim())
//                .registrantZipCode(line.substring(199, 209).trim())
//                .registrantRegion(line.substring(210, 211).trim())
//                .countyMail(line.substring(212, 215).trim())
//                .countryMail(line.substring(216, 218).trim())
//                .lastActivityDate(line.substring(219, 227).trim())
//                .certificateIssueDate(line.substring(228, 236).trim())
//                .airworthinessClassificationCode(line.substring(237, 238).trim())
//                .approvedOperationCodes(line.substring(238, 247).trim())
//                .typeAircraft(line.substring(248, 249).trim())
//                .typeEngine(line.substring(250, 252).trim())
//                .statusCode(line.substring(253, 255).trim())
//                .modeSCode(line.substring(256, 264).trim())
//                .fractionalOwnership(line.substring(265, 266).trim())
//                .airworthinessDate(line.substring(267, 275).trim())
//                .otherName1(line.substring(276, 326).trim())
//                .otherName2(line.substring(327, 377).trim())
//                .otherName3(line.substring(378, 428).trim())
//                .otherName4(line.substring(429, 479).trim())
//                .otherName5(line.substring(480, 530).trim())
//                .expirationDate(line.substring(531, 539).trim())
//                .uniqueId(line.substring(540, 548).trim())
//                .kitMfr(line.substring(549, 579).trim())
//                .kitModel(line.substring(580, 600).trim())
//                .modeScodeHex(line.substring(601, 611).trim())
//                .build();
//    }
}
