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
@Table("seats")
public class Seat {
    @Id
    private Long id;

    @Column("flight_id")
    private Long flightId;

    @Column("seat_number")
    private String seatNumber;

    @Column("class")
    private SeatClass seatClass;

    @Column("status")
    private SeatStatus status;
}
