package org.beerinfo.data.dto.api.beer;

import lombok.Builder;

@Builder
public record PutBeerResponseDTO(
        long beerId,
        String abv,
        String ibuNumber,
        String name,
        String style,
        int breweryId,
        String ounces
) {
}