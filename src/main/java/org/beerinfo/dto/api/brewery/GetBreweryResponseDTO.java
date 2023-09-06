package org.beerinfo.dto.api.brewery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetBreweryResponseDTO {
    private long breweryId;
    private String name;
    private String city;
    private String state;
    private String country;
}