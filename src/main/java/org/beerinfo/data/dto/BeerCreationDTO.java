package org.beerinfo.data.dto;

import jakarta.validation.constraints.*;

public record BeerCreationDTO(
        String abv,
        String ibuNumber,
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Style cannot be blank")
        String style,

        @NotNull(message = "BreweryId cannot be null")
        @Min(value = 1, message = "breweryId must be a positive number and must be at least 1")
        @Max(value = 99999, message = "breweryId must be at most 99999")
        Integer breweryId,
        String ounces
) {
}