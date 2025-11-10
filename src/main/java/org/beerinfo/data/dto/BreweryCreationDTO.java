package org.beerinfo.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BreweryCreationDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "City cannot be blank")
    private String city;
    private String state;

    @NotBlank(message = "Country cannot be blank")
    private String country;
}