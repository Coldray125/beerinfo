package api.test_data.response.beer;

import java.math.BigDecimal;

public record GetBeerResponse(
        long beerId,
        BigDecimal abv,
        Integer ibuNumber,
        String name,
        String style,
        Integer breweryId,
        BigDecimal ounces
) {
}