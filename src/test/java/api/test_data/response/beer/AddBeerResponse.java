package api.test_data.response.beer;

import java.math.BigDecimal;

public record AddBeerResponse(String message, BeerDetails beer) {
    public record BeerDetails(
            long beerId,
            BigDecimal abv,
            Integer ibuNumber,
            String id,
            String name,
            String style,
            long breweryId,
            BigDecimal ounces
    ) {
    }
}