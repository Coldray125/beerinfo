package api.pojo.response.beer;

public record GetBeerResponse(
        long beerId,
        String abv,
        String ibuNumber,
        String name,
        String style,
        int breweryId,
        String ounces) {
}