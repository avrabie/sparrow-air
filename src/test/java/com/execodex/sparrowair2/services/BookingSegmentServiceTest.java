package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.BookingSegment;
import com.execodex.sparrowair2.entities.Flight;
import com.execodex.sparrowair2.entities.Seat;
import com.execodex.sparrowair2.entities.SeatClass;
import com.execodex.sparrowair2.entities.SeatStatus;
import com.execodex.sparrowair2.repositories.BookingSegmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BookingSegmentServiceTest {

    @Mock
    private BookingSegmentRepository bookingSegmentRepository;

    @Mock
    private SeatService seatService;

    @Mock
    private FlightService flightService;

    @Mock
    private ReactiveTransactionManager transactionManager;

    @InjectMocks
    private BookingSegmentService bookingSegmentService;

    private BookingSegment testBookingSegment;
    private Seat testSeat;
    private Flight testFlight;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up TransactionalOperator to pass through the original Mono
        when(transactionManager.getReactiveTransaction(any())).thenReturn(Mono.empty());
        when(transactionManager.commit(any())).thenReturn(Mono.empty());
        when(transactionManager.rollback(any())).thenReturn(Mono.empty());

        testBookingSegment = BookingSegment.builder()
                .id(1L)
                .bookingId(100L)
                .flightId(200L)
                .seatId(300L)
                .fareClass("ECONOMY")
                .ticketNumber("TKT12345")
                .build();

        testSeat = Seat.builder()
                .id(300L)
                .flightId(200L)
                .seatNumber("15A")
                .seatClass(SeatClass.ECONOMY)
                .status(SeatStatus.AVAILABLE)
                .build();

        testFlight = Flight.builder()
                .id(200L)
                .airlineIcaoCode("ABC")
                .flightNumber("123")
                .departureAirportIcao("ABCD")
                .arrivalAirportIcao("EFGH")
                .scheduledDeparture(LocalDateTime.now().plusDays(1))
                .scheduledArrival(LocalDateTime.now().plusDays(1).plusHours(2))
                .status("SCHEDULED")
                .build();
    }

    @Test
    void testDeleteBookingSegment_Success() {
        // Mock repository findById to return the test booking segment
        when(bookingSegmentRepository.findById(1L)).thenReturn(Mono.just(testBookingSegment));

        // Mock seatService.updateSeatStatus to return the test seat
        when(seatService.updateSeatStatus(anyLong(), anyString())).thenReturn(Mono.just(testSeat));

        // Mock repository delete to return empty Mono
        when(bookingSegmentRepository.delete(any(BookingSegment.class))).thenReturn(Mono.empty());

        // Test the deleteBookingSegment method
        StepVerifier.create(bookingSegmentService.deleteBookingSegment(1L))
                .expectNext(testBookingSegment)
                .verifyComplete();
    }

    @Test
    void testDeleteBookingSegment_SeatStatusUpdatedToAvailable() {
        // Set up a booking segment with a seat that has a status of BOOKED
        // Using different IDs to avoid conflicts with other tests
        BookingSegment bookedSegment = BookingSegment.builder()
                .id(2L)
                .bookingId(101L)
                .flightId(201L)
                .seatId(301L)
                .fareClass("ECONOMY")
                .ticketNumber("TKT12346")
                .build();

        Seat bookedSeat = Seat.builder()
                .id(301L)
                .flightId(201L)
                .seatNumber("16A")
                .seatClass(SeatClass.ECONOMY)
                .status(SeatStatus.BOOKED)
                .build();

        Seat availableSeat = Seat.builder()
                .id(301L)
                .flightId(201L)
                .seatNumber("16A")
                .seatClass(SeatClass.ECONOMY)
                .status(SeatStatus.AVAILABLE)
                .build();

        // Mock repository findById to return the booked segment
        when(bookingSegmentRepository.findById(2L)).thenReturn(Mono.just(bookedSegment));

        // Mock seatService.updateSeatStatus to verify it's called with the correct parameters
        // and return the seat with AVAILABLE status
        when(seatService.updateSeatStatus(301L, "AVAILABLE")).thenReturn(Mono.just(availableSeat));

        // Mock repository delete to return empty Mono
        when(bookingSegmentRepository.delete(any(BookingSegment.class))).thenReturn(Mono.empty());

        // Test the deleteBookingSegment method
        StepVerifier.create(bookingSegmentService.deleteBookingSegment(2L))
                .expectNext(bookedSegment)
                .verifyComplete();

        // Verify that seatService.updateSeatStatus was called with the correct parameters
        org.mockito.Mockito.verify(seatService).updateSeatStatus(301L, "AVAILABLE");
    }

    @Test
    void testDeleteBookingSegment_NotFound() {
        // Mock repository findById to return empty Mono (segment not found)
        when(bookingSegmentRepository.findById(999L)).thenReturn(Mono.empty());

        // Test the deleteBookingSegment method with non-existent ID
        StepVerifier.create(bookingSegmentService.deleteBookingSegment(999L))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testGetBookingSegmentByFlightIdAndSeatId_Success() {
        // Mock repository findByFlightIdAndSeatId to return the test booking segment
        when(bookingSegmentRepository.findByFlightIdAndSeatId(200L, 300L)).thenReturn(Mono.just(testBookingSegment));

        // Test the getBookingSegmentByFlightIdAndSeatId method
        StepVerifier.create(bookingSegmentService.getBookingSegmentByFlightIdAndSeatId(200L, 300L))
                .expectNext(testBookingSegment)
                .verifyComplete();
    }

    @Test
    void testGetBookingSegmentByFlightIdAndSeatId_NotFound() {
        // Mock repository findByFlightIdAndSeatId to return empty Mono (segment not found)
        when(bookingSegmentRepository.findByFlightIdAndSeatId(999L, 999L)).thenReturn(Mono.empty());

        // Test the getBookingSegmentByFlightIdAndSeatId method with non-existent flight ID and seat ID
        StepVerifier.create(bookingSegmentService.getBookingSegmentByFlightIdAndSeatId(999L, 999L))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testCreateBookingSegment_Success() {
        // Mock flightService to return the test flight
        when(flightService.getFlightById(200L)).thenReturn(Mono.just(testFlight));

        // Mock seatService to return the test seat
        when(seatService.getSeatById(300L)).thenReturn(Mono.just(testSeat));

        // Mock repository findByFlightIdAndSeatId to return empty Mono (no existing booking segment)
        when(bookingSegmentRepository.findByFlightIdAndSeatId(200L, 300L)).thenReturn(Mono.empty());

        // Mock repository insert to return the test booking segment
        when(bookingSegmentRepository.insert(any(BookingSegment.class))).thenReturn(Mono.just(testBookingSegment));

        // Mock seatService.updateSeatStatus to return a seat with RESERVED status
        Seat reservedSeat = Seat.builder()
                .id(300L)
                .flightId(200L)
                .seatNumber("15A")
                .seatClass(SeatClass.ECONOMY)
                .status(SeatStatus.RESERVED)
                .build();
        when(seatService.updateSeatStatus(300L, "RESERVED")).thenReturn(Mono.just(reservedSeat));

        // Test the createBookingSegment method
        StepVerifier.create(bookingSegmentService.createBookingSegment(testBookingSegment))
                .expectNext(testBookingSegment)
                .verifyComplete();
    }

    @Test
    void testCreateBookingSegment_FlightNotFound() {
        // Mock flightService to return empty Mono (flight not found)
        when(flightService.getFlightById(200L)).thenReturn(Mono.empty());

        // Mock seatService to return the test seat (even though we won't get to use it)
        when(seatService.getSeatById(300L)).thenReturn(Mono.just(testSeat));

        // Test the createBookingSegment method with non-existent flight ID
        StepVerifier.create(bookingSegmentService.createBookingSegment(testBookingSegment))
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException && 
                    throwable.getMessage().contains("Flight not found with ID: 200"))
                .verify();
    }

    @Test
    void testCreateBookingSegment_SeatNotFound() {
        // Mock flightService to return the test flight
        when(flightService.getFlightById(200L)).thenReturn(Mono.just(testFlight));

        // Mock seatService to return empty Mono (seat not found)
        when(seatService.getSeatById(300L)).thenReturn(Mono.empty());

        // Test the createBookingSegment method with non-existent seat ID
        StepVerifier.create(bookingSegmentService.createBookingSegment(testBookingSegment))
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException && 
                    throwable.getMessage().contains("Seat not found with ID: 300"))
                .verify();
    }

    @Test
    void testCreateBookingSegment_SeatNotAvailable() {
        // Create a seat that is not available (already booked)
        Seat bookedSeat = Seat.builder()
                .id(300L)
                .flightId(200L)
                .seatNumber("15A")
                .seatClass(SeatClass.ECONOMY)
                .status(SeatStatus.BOOKED)
                .build();

        // Mock flightService to return the test flight
        when(flightService.getFlightById(200L)).thenReturn(Mono.just(testFlight));

        // Mock seatService to return the booked seat
        when(seatService.getSeatById(300L)).thenReturn(Mono.just(bookedSeat));

        // Test the createBookingSegment method with a seat that is not available
        StepVerifier.create(bookingSegmentService.createBookingSegment(testBookingSegment))
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException && 
                    throwable.getMessage().contains("Seat with ID 300 is not available"))
                .verify();
    }

    @Test
    void testCreateBookingSegment_SeatStatusChangedToReserved() {
        // Create a seat with AVAILABLE status
        Seat availableSeat = Seat.builder()
                .id(300L)
                .flightId(200L)
                .seatNumber("15A")
                .seatClass(SeatClass.ECONOMY)
                .status(SeatStatus.AVAILABLE)
                .build();

        // Create a seat with RESERVED status (after booking)
        Seat reservedSeat = Seat.builder()
                .id(300L)
                .flightId(200L)
                .seatNumber("15A")
                .seatClass(SeatClass.ECONOMY)
                .status(SeatStatus.RESERVED)
                .build();

        // Mock flightService to return the test flight
        when(flightService.getFlightById(200L)).thenReturn(Mono.just(testFlight));

        // Mock seatService to return the available seat
        when(seatService.getSeatById(300L)).thenReturn(Mono.just(availableSeat));

        // Mock repository findByFlightIdAndSeatId to return empty Mono (no existing booking segment)
        when(bookingSegmentRepository.findByFlightIdAndSeatId(200L, 300L)).thenReturn(Mono.empty());

        // Mock repository insert to return the test booking segment
        when(bookingSegmentRepository.insert(any(BookingSegment.class))).thenReturn(Mono.just(testBookingSegment));

        // Mock seatService.updateSeatStatus to return the reserved seat
        when(seatService.updateSeatStatus(300L, "RESERVED")).thenReturn(Mono.just(reservedSeat));

        // Test the createBookingSegment method
        StepVerifier.create(bookingSegmentService.createBookingSegment(testBookingSegment))
                .expectNext(testBookingSegment)
                .verifyComplete();

        // Verify that seatService.updateSeatStatus was called with the correct parameters
        org.mockito.Mockito.verify(seatService).updateSeatStatus(300L, "RESERVED");
    }
}
