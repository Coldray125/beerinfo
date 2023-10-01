package api.conversion;

import api.pojo.response.beer.GetBeerResponse;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.dto.data.BeerCreationDTO;
import org.beerinfo.entity.BeerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BeerConverter {
    BeerConverter MAPPER = Mappers.getMapper(BeerConverter.class);

    GetBeerResponseDTO convertToGetBeerResponseDTO(GetBeerResponse response);
}