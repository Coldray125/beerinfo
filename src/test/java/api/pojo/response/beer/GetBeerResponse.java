package api.pojo.response.beer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetBeerResponse {
    private long beerId;
    private String abv;
    private String ibuNumber;
    private String name;
    private String style;
    private int breweryId;
    private String ounces;
}