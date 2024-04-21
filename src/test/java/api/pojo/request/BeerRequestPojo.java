package api.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerRequestPojo {
    private String abv;
    private String ibuNumber;
    private String name;
    private String style;
    private Long breweryId;
    private String ounces;
}