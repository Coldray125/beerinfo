package org.beerinfo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Beer")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinedBeerBreweryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "beer_seq")
    @SequenceGenerator(name = "beer_seq", sequenceName = "beer_beer_id_seq", allocationSize = 1)
    @Column(name = "beer_id",nullable = false, unique = true)
    private long beerId;

    @Column(name = "abv")
    private String abv;

    @Column(name = "ibu_number")
    private String ibuNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "style")
    private String style;

    @Column(name = "brewery_id")
    private int breweryId;

    @Column(name = "ounces")
    private String ounces;

    @ManyToOne
    @JoinColumn(name = "brewery_id", insertable = false, updatable = false)
    BreweryEntity breweryEntity;
}