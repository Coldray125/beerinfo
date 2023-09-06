package api.pojo.response.beer;

import lombok.Data;

@Data
public class UpdateBeerResponse {
    private String message;
    private BeerDetails beer;

    @Data
    public static class BeerDetails {
        private long beerId;
        private String abv;
        private String ibuNumber;
        private String id;
        private String name;
        private String style;
        private int breweryId;
        private String ounces;
    }
}