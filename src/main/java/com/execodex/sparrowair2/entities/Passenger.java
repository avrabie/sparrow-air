package com.execodex.sparrowair2.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("passengers")
public class Passenger {
    @Id
    private Long id;
    private String firstName;
    private String lastName;

    @Column("passport_number")
    private String passportNumber;
    @Column("date_of_birth")
    private LocalDate dateOfBirth;

    private String nationality;
    private String email;
    private String phone;


}