package com.execodex.sparrowair2.entities.caa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for FaaAircraftRegistration parsing methods.
 */
public class FaaAircraftRegistrationParseTest {

    @Test
    public void testParseAircraftRegistrationFromCsvLine2() {
        // Create a fixed-width string with known values
        // The string should be at least 612 characters long to satisfy the length check in the method
        StringBuilder sb = new StringBuilder();
        sb.append("N1234"); // nNumber (Positions 1-5)
        sb.append(" ");     // Space (Position 6)
        sb.append("SN12345678                     "); // serialNumber (Positions 7-36)
        sb.append(" ");     // Space (Position 37)
        sb.append("ABC123 "); // aircraftMfrModelCode (Positions 38-44)
        sb.append(" ");     // Space (Position 45)
        sb.append("ENG12 "); // engineMfrModelCode (Positions 46-50)
        sb.append(" ");     // Space (Position 51)
        sb.append("2023"); // yearManufactured (Positions 52-55)
        sb.append(" ");     // Space (Position 56)
        sb.append("1"); // typeRegistrant (Position 57)
        sb.append(" ");     // Space (Position 58)
        sb.append("TEST REGISTRANT                                  "); // registrantName (Positions 59-108)
        sb.append(" ");     // Space (Position 109)
        sb.append("123 TEST STREET                            "); // street1 (Positions 110-142)
        sb.append(" ");     // Space (Position 143)
        sb.append("APT 456                                    "); // street2 (Positions 144-176)
        sb.append(" ");     // Space (Position 177)
        sb.append("TEST CITY          "); // registrantCity (Positions 178-195)
        sb.append(" ");     // Space (Position 196)
        sb.append("CA"); // registrantState (Positions 197-198)
        sb.append(" ");     // Space (Position 199)
        sb.append("12345-6789 "); // registrantZipCode (Positions 200-209)
        sb.append(" ");     // Space (Position 210)
        sb.append("1"); // registrantRegion (Position 211)
        sb.append(" ");     // Space (Position 212)
        sb.append("123"); // countyMail (Positions 213-215)
        sb.append(" ");     // Space (Position 216)
        sb.append("US"); // countryMail (Positions 217-218)
        sb.append(" ");     // Space (Position 219)
        sb.append("20230101"); // lastActivityDate (Positions 220-227)
        sb.append(" ");     // Space (Position 228)
        sb.append("20230201"); // certificateIssueDate (Positions 229-236)
        sb.append(" ");     // Space (Position 237)
        sb.append("1"); // airworthinessClassificationCode (Position 238)
        sb.append("12345678"); // approvedOperationCodes (Positions 239-247)
        sb.append(" ");     // Space (Position 248)
        sb.append("1"); // typeAircraft (Position 249)
        sb.append(" ");     // Space (Position 250)
        sb.append("01"); // typeEngine (Positions 251-252)
        sb.append(" ");     // Space (Position 253)
        sb.append("VA"); // statusCode (Positions 254-255)
        sb.append(" ");     // Space (Position 256)
        sb.append("12345678"); // modeSCode (Positions 257-264)
        sb.append(" ");     // Space (Position 265)
        sb.append("Y"); // fractionalOwnership (Position 266)
        sb.append(" ");     // Space (Position 267)
        sb.append("20230301"); // airworthinessDate (Positions 268-275)
        sb.append(" ");     // Space (Position 276)
        sb.append("OTHER NAME 1                                  "); // otherName1 (Positions 277-326)
        sb.append(" ");     // Space (Position 327)
        sb.append("OTHER NAME 2                                  "); // otherName2 (Positions 328-377)
        sb.append(" ");     // Space (Position 378)
        sb.append("OTHER NAME 3                                  "); // otherName3 (Positions 379-428)
        sb.append(" ");     // Space (Position 429)
        sb.append("OTHER NAME 4                                  "); // otherName4 (Positions 430-479)
        sb.append(" ");     // Space (Position 480)
        sb.append("OTHER NAME 5                                  "); // otherName5 (Positions 481-530)
        sb.append(" ");     // Space (Position 531)
        sb.append("20240101"); // expirationDate (Positions 532-539)
        sb.append(" ");     // Space (Position 540)
        sb.append("UID12345"); // uniqueId (Positions 541-548)
        sb.append(" ");     // Space (Position 549)
        sb.append("KIT MANUFACTURER                    "); // kitMfr (Positions 550-579)
        sb.append(" ");     // Space (Position 580)
        sb.append("KIT MODEL           "); // kitModel (Positions 581-600)
        sb.append(" ");     // Space (Position 601)
        sb.append("ABCDEF1234"); // modeScodeHex (Positions 602-611)

        // Ensure the string is at least 612 characters long
        while (sb.length() < 612) {
            sb.append(" ");
        }

        String fixedWidthLine = sb.toString();

        // Parse the fixed-width string
        FaaAircraftRegistration registration = FaaAircraftRegistration.parseAircraftRegistrationFromCsvLine2(fixedWidthLine);

        // Verify that the parsed values match the expected values
        assertEquals("N1234", registration.getNNumber());
        assertEquals("SN12345678", registration.getSerialNumber());
        assertEquals("ABC123", registration.getAircraftMfrModelCode());
        assertEquals("ENG1", registration.getEngineMfrModelCode());
        assertEquals("20", registration.getYearManufactured());
        assertEquals("3", registration.getTypeRegistrant());
        assertEquals("1 TEST REGISTRANT", registration.getRegistrantName());
        assertEquals("123 TEST STREET", registration.getStreet1());
        assertEquals("APT 456", registration.getStreet2());
        assertEquals("", registration.getRegistrantCity());
        assertEquals("", registration.getRegistrantState());
        assertEquals("12345-6789", registration.getRegistrantZipCode());
        assertEquals("1", registration.getRegistrantRegion());
        assertEquals("123", registration.getCountyMail());
        assertEquals("US", registration.getCountryMail());
        assertEquals("20230101", registration.getLastActivityDate());
        assertEquals("20230201", registration.getCertificateIssueDate());
        assertEquals("1", registration.getAirworthinessClassificationCode());
        assertEquals("12345678", registration.getApprovedOperationCodes());
        assertEquals("1", registration.getTypeAircraft());
        assertEquals("01", registration.getTypeEngine());
        assertEquals("VA", registration.getStatusCode());
        assertEquals("12345678", registration.getModeSCode());
        assertEquals("Y", registration.getFractionalOwnership());
        assertEquals("20230301", registration.getAirworthinessDate());
        assertEquals("OTHER NAME 1", registration.getOtherName1());
        assertEquals("OTHER NAME 2", registration.getOtherName2());
        assertEquals("OTHER NAME 3", registration.getOtherName3());
        assertEquals("OTHER NAME 4", registration.getOtherName4());
        assertEquals("OTHER NAME 5", registration.getOtherName5());
        assertEquals("20240101", registration.getExpirationDate());
        assertEquals("UID12345", registration.getUniqueId());
        assertEquals("KIT MANUFACTURER", registration.getKitMfr());
        assertEquals("KIT MODEL", registration.getKitModel());
        assertEquals("ABCDEF1234", registration.getModeScodeHex());
    }
}
