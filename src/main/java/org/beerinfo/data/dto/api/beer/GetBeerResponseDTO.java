package org.beerinfo.data.dto.api.beer;

public record GetBeerResponseDTO(
        long beerId,
        String abv,
        String ibuNumber,
        String name,
        String style,
        int breweryId,
        String ounces
) {
}