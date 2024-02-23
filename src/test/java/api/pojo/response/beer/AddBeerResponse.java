package api.pojo.response.beer;

public record AddBeerResponse(String message, BeerDetails beer) {
    public record BeerDetails(
            long beerId,
            String abv,
            String ibuNumber,
            String id,
            String name,
            String style,
            int breweryId,
            String ounces
    ) {
    }
}