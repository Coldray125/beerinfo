package api.test_data.response.beer;

public record UpdateBeerResponse(String message, BeerDetails beer) {
    public record BeerDetails(
            String abv,
            String ibuNumber,
            String id,
            String name,
            String style,
            long breweryId,
            String ounces
    ) {
    }
}