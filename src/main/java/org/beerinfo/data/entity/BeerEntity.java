package org.beerinfo.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "beer")
public class BeerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "beer_seq")
    @SequenceGenerator(
            name = "beer_seq",
            sequenceName = "beer_beer_id_seq",
            allocationSize = 1
    )
    @Column(name = "beer_id", nullable = false)
    private Long beerId;

    @Column(name = "abv", precision = 4, scale = 3)
    private BigDecimal abv;

    @Column(name = "ibu_number")
    private Integer ibuNumber;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "style", nullable = false, length = 40)
    private String style;

    @Column(name = "brewery_id", nullable = false)
    private Long breweryId;

    @Column(name = "ounces", precision = 5, scale = 2)
    private BigDecimal ounces;
}