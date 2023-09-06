package org.beerinfo.dto.api.beer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostBeerResponseDTO {
        private long beerId;
        private String abv;
        private String ibuNumber;
        private String name;
        private String style;
        private int breweryId;
        private String ounces;
}