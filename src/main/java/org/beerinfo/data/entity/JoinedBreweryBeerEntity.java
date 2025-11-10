package org.beerinfo.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Breweries")
public class JoinedBreweryBeerEntity {
    @Id
    @Column(name = "brewery_id")
    private Long breweryId;

    private String name;

    private String city;

    private String state;

    private String country;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "brewery_id")
    private List<BeerEntity> beerEntities;
}