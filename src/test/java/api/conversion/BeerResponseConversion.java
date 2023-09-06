package api.conversion;

import api.pojo.response.beer.GetBeerResponse;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;

public class BeerResponseConversion {
    public static GetBeerResponseDTO convertToGetBeerResponseDTO(GetBeerResponse getBeerResponse) {
        return GetBeerResponseDTO.builder()
                .beerId(getBeerResponse.getBeerId())
                .abv(getBeerResponse.getAbv())
                .ibuNumber(getBeerResponse.getIbuNumber())
                .name(getBeerResponse.getName())
                .style(getBeerResponse.getStyle())
                .breweryId(getBeerResponse.getBreweryId())
                .ounces(getBeerResponse.getOunces())
                .build();
    }
}