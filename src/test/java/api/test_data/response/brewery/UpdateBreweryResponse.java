package api.test_data.response.brewery;

public record UpdateBreweryResponse(String message, BreweryDetails brewery) {
    public record BreweryDetails(
            String name,
            String city,
            String state,
            String country
    ) {
    }
}