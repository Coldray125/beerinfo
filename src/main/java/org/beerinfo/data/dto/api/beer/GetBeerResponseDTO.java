package org.beerinfo.data.dto.api.beer;

import java.math.BigDecimal;

public record GetBeerResponseDTO(
        long beerId,
        BigDecimal abv,
        Integer ibuNumber,
        String name,
        String style,
        Integer breweryId,
        BigDecimal ounces
) {
}