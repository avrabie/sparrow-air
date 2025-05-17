package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.BookingSegment;
import com.execodex.sparrowair2.entities.Seat;
import com.execodex.sparrowair2.repositories.BookingSegmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

    @InjectMocks
    private BookingSegmentService bookingSegmentService;

    private BookingSegment testBookingSegment;
    private Seat testSeat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
    void testDeleteBookingSegment_NotFound() {
        // Mock repository findById to return empty Mono (segment not found)
        when(bookingSegmentRepository.findById(999L)).thenReturn(Mono.empty());

        // Test the deleteBookingSegment method with non-existent ID
        StepVerifier.create(bookingSegmentService.deleteBookingSegment(999L))
                .expectError(RuntimeException.class)
                .verify();
    }
}