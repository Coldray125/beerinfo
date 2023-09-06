package api.pojo.response.brewery;

import lombok.Data;

@Data
public class UpdateBreweryResponse {
    private String message;
    private BreweryDetails brewery;

    @Data
    public static class BreweryDetails {
        private String name;
        private String city;
        private String state;
        private String country;
    }
}