package com.execodex.sparrowair2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("bookings")
public class Booking {
    @Id
    private Long id;

    @Column("booking_reference")
    private String bookingReference;

    @Column("passenger_id")
    private Long passengerId;

    @Column("status")
    private String status;

    @Column("created_at")
    private LocalDateTime createdAt;

    public Booking(String bookingReference, Long passengerId,
                   String status) {
        this.bookingReference = bookingReference;
        this.passengerId = passengerId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

}