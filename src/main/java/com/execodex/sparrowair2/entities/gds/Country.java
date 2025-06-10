package com.execodex.sparrowair2.entities.gds;

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
@Table(name = "countries")
public class Country {
    @Id
    @Column(value = "code")
    private String code; // ISO 3166-1 alpha-2 code (e.g., "US")

    private String name;
    private String capital;
    private String continent;
    private String currency;
    private String language;
}