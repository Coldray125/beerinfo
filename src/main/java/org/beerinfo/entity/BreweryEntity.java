package org.beerinfo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Breweries")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BreweryEntity {
    @Id
    @Column(name = "brewery_id")
    private long breweryId;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;
}