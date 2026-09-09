package api.test_data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerRequest {
    private String abv;
    private String ibuNumber;
    private String name;
    private String style;
    private Long breweryId;
    private String ounces;
}