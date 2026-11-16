package api.test_data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerRequest {
    private BigDecimal abv;
    private Integer ibuNumber;
    private String name;
    private String style;
    private Long breweryId;
    private BigDecimal ounces;
}