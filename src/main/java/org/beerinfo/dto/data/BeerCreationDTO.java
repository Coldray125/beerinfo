package org.beerinfo.dto.data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BeerCreationDTO {

    private String abv;
    private String ibuNumber;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Style cannot be blank")
    private String style;

    @NotNull(message = "BreweryId cannot be empty")
    @Min(value = 1, message = "breweryId must be a positive number and must be at least 1")
    @Max(value = 99999, message = "breweryId must be at most 99999")
    private Integer breweryId;

    private String ounces;
}