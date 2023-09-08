package org.beerinfo.dto.api.beer;

import lombok.Builder;

@Builder
public record PostBeerResponseDTO(
        long beerId,
        String abv,
        String ibuNumber,
        String name,
        String style,
        int breweryId,
        String ounces
) {
}