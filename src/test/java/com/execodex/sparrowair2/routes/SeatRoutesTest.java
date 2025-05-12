package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.entities.Seat;
import com.execodex.sparrowair2.entities.SeatClass;
import com.execodex.sparrowair2.entities.SeatStatus;
import com.execodex.sparrowair2.handlers.SeatHandler;
import com.execodex.sparrowair2.services.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest
@Import({SeatRoutes.class, SeatHandler.class})
public class SeatRoutesTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SeatService seatService;

    @Test
    public void testGetAllSeats() {
        // Create test seats
        Seat seat1 = Seat.builder()
                .id(1L)
                .flightId(100L)
                .seatNumber("F1")
                .seatClass(SeatClass.FIRST_CLASS)
                .status(SeatStatus.AVAILABLE)
                .build();

        Seat seat2 = Seat.builder()
                .id(2L)
                .flightId(100L)
                .seatNumber("B1")
                .seatClass(SeatClass.BUSINESS)
                .status(SeatStatus.AVAILABLE)
                .build();

        Seat seat3 = Seat.builder()
                .id(3L)
                .flightId(100L)
                .seatNumber("E1")
                .seatClass(SeatClass.ECONOMY)
                .status(SeatStatus.BOOKED)
                .build();

        List<Seat> seats = Arrays.asList(seat1, seat2, seat3);

        // Mock the service response
        when(seatService.getAllSeats()).thenReturn(Flux.fromIterable(seats));

        // Test the endpoint
        webTestClient.get()
                .uri("/seats")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Seat.class)
                .hasSize(3)
                .contains(seat1, seat2, seat3);
    }
}
