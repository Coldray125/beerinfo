package org.beerinfo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Breweries")
public class BreweryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brewery_seq")
    @SequenceGenerator(name = "brewery_seq", sequenceName = "breweries_brewery_id_seq", allocationSize = 1)
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