package org.beerinfo.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BeerReadDTO {
    private long beerId;
    private String abv;
    private String ibuNumber;
    private String name;
    private String style;
    private Integer breweryId;
    private String ounces;
}