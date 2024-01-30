package org.beerinfo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Breweries")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinedBreweryBeerEntity {
    @Id
    @Column(name = "brewery_id")
    private long breweryId;

    private String name;

    private String city;

    private String state;

    private String country;

    @OneToMany(mappedBy = "breweryId", fetch = FetchType.EAGER)
    List<BeerEntity> beerEntities;
}