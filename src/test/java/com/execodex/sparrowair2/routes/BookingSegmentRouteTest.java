package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.entities.BookingSegment;
import com.execodex.sparrowair2.handlers.BookingSegmentHandler;
import com.execodex.sparrowair2.services.BookingSegmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@WebFluxTest
@Import({BookingSegmentRoute.class, BookingSegmentHandler.class})
public class BookingSegmentRouteTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookingSegmentService bookingSegmentService;

    @Test
    public void testGetAllBookingSegments() {
        // Create test booking segments
        BookingSegment bookingSegment1 = BookingSegment.builder()
                .id(1L)
                .bookingId(1L)
                .flightId(100L)
                .seatId(200L)
                .fareClass("First")
                .ticketNumber("TKT123456")
                .build();

        BookingSegment bookingSegment2 = BookingSegment.builder()
                .id(2L)
                .bookingId(2L)
                .flightId(101L)
                .seatId(201L)
                .fareClass("Business")
                .ticketNumber("TKT789012")
                .build();

        List<BookingSegment> bookingSegments = Arrays.asList(bookingSegment1, bookingSegment2);

        // Mock the service response
        when(bookingSegmentService.getAllBookingSegments()).thenReturn(Flux.fromIterable(bookingSegments));

        // Test the GET /booking-segments endpoint
        webTestClient.get()
                .uri("/booking-segments")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookingSegment.class)
                .hasSize(2)
                .contains(bookingSegment1, bookingSegment2);
    }

    @Test
    public void testGetBookingSegmentById() {
        // Create a test booking segment
        BookingSegment bookingSegment = BookingSegment.builder()
                .id(1L)
                .bookingId(1L)
                .flightId(100L)
                .seatId(200L)
                .fareClass("Business")
                .ticketNumber("TKT789012")
                .build();

        // Mock the service response
        when(bookingSegmentService.getBookingSegmentById(1L)).thenReturn(Mono.just(bookingSegment));

        // Test the GET /booking-segments/{id} endpoint
        webTestClient.get()
                .uri("/booking-segments/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookingSegment.class)
                .isEqualTo(bookingSegment);
    }

    @Test
    public void testCreateBookingSegment() {
        // Create a test booking segment
        BookingSegment bookingSegment = BookingSegment.builder()
                .bookingId(1L)
                .flightId(100L)
                .seatId(200L)
                .fareClass("Economy")
                .ticketNumber("TKT345678")
                .build();

        BookingSegment savedBookingSegment = BookingSegment.builder()
                .id(1L)
                .bookingId(1L)
                .flightId(100L)
                .seatId(200L)
                .fareClass("Economy")
                .ticketNumber("TKT345678")
                .build();

        // Mock the service response
        when(bookingSegmentService.createBookingSegment(any(BookingSegment.class))).thenReturn(Mono.just(savedBookingSegment));

        // Test the POST /booking-segments endpoint
        webTestClient.post()
                .uri("/booking-segments")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookingSegment), BookingSegment.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookingSegment.class)
                .isEqualTo(savedBookingSegment);
    }

    @Test
    public void testUpdateBookingSegment() {
        // Create a test booking segment
        BookingSegment updatedBookingSegment = BookingSegment.builder()
                .id(1L)
                .bookingId(1L)
                .flightId(100L)
                .seatId(200L)
                .fareClass("First") // Updated fare class
                .ticketNumber("TKT901234")
                .build();

        // Mock the service response
        when(bookingSegmentService.updateBookingSegment(anyLong(), any(BookingSegment.class))).thenReturn(Mono.just(updatedBookingSegment));

        // Test the PUT /booking-segments/{id} endpoint
        webTestClient.put()
                .uri("/booking-segments/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedBookingSegment), BookingSegment.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookingSegment.class)
                .isEqualTo(updatedBookingSegment);
    }
}
