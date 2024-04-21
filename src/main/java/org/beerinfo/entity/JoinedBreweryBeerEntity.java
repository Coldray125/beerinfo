package org.beerinfo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    List<BeerEntity> beerEntities;
}