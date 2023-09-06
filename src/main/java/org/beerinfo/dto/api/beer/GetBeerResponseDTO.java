package org.beerinfo.dto.api.beer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetBeerResponseDTO {
    private long beerId;
    private String abv;
    private String ibuNumber;
    private String name;
    private String style;
    private int breweryId;
    private String ounces;
}