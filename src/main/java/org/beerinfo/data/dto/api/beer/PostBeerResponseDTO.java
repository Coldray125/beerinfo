package org.beerinfo.data.dto.api.beer;

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