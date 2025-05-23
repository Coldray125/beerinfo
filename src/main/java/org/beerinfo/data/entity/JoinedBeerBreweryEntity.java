package org.beerinfo.data.entity;

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
@Table(name = "Beer")
public class JoinedBeerBreweryEntity {
    @Id
    @Column(name = "beer_id", nullable = false, unique = true)
    private Long beerId;

    private String abv;

    @Column(name = "ibu_number")
    private String ibuNumber;

    private String name;

    private String style;

    @Column(name = "brewery_id")
    private Long breweryId;

    private String ounces;

    @ManyToOne
    @JoinColumn(name = "brewery_id", insertable = false, updatable = false)
    private BreweryEntity breweryEntity;
}