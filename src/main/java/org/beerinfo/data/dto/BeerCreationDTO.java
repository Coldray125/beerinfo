package org.beerinfo.data.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BeerCreationDTO(
        @DecimalMin(value = "0.000", message = "ABV must be at least 0")
        @DecimalMax(value = "1.000", message = "ABV must not exceed 1.000 (100%)")
        @Digits(integer = 1, fraction = 3, message = "ABV must have at most 3 decimal places")
        BigDecimal abv,

        @Min(value = 0, message = "IBU must be at least 0")
        @Max(value = 120, message = "IBU must not exceed 120")
        Integer ibuNumber,

        @NotBlank(message = "Name cannot be blank")
        @Size(max = 60, message = "Name must not exceed 60 characters")
        String name,

        @NotBlank(message = "Style cannot be blank")
        @Size(max = 40, message = "Style must not exceed 40 characters")
        String style,

        @NotNull(message = "BreweryId cannot be null")
        @Positive(message = "BreweryId must be positive")
        Long breweryId,

        @DecimalMin(value = "0.01", message = "Ounces must be greater than 0")
        @Digits(integer = 3, fraction = 2, message = "Ounces must have at most 2 decimal places")
        BigDecimal ounces
) {
}