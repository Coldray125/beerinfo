package api.test_data.response.beer;

import java.math.BigDecimal;

public record UpdateBeerResponse(String message, BeerDetails beer) {
    public record BeerDetails(
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