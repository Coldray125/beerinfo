package org.beerinfo.data.dto.api.brewery;

public record GetBreweryResponseDTO(
        long breweryId,
        String name,
        String city,
        String state,
        String country
) {
}