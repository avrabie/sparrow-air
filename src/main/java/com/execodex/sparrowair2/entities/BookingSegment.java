package com.execodex.sparrowair2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("booking_segments")
public class BookingSegment {
    @Id
    private Long id;

    @Column("booking_id")
    private Long bookingId;

    @Column("flight_id")
    private Long flightId;

    @Column("seat_id")
    private Long seatId;

    @Column("fare_class")
    private String fareClass;

    @Column("ticket_number")
    private String ticketNumber;


    // ... accessors
}
