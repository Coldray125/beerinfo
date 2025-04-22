package org.beerinfo.mapper;

import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.data.dto.api.beer.PostBeerResponseDTO;
import org.beerinfo.data.dto.api.beer.PutBeerResponseDTO;
import org.beerinfo.data.dto.BeerCreationDTO;
import org.beerinfo.data.entity.BeerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BeerMapper {
    BeerMapper MAPPER = Mappers.getMapper(BeerMapper.class);

    @Mapping(target = "beerId", ignore = true)
    BeerEntity mapToBeerEntity(BeerCreationDTO dto);

    PostBeerResponseDTO mapToPostBeerResponseDTO(BeerEntity entity);

    GetBeerResponseDTO mapToGetBeerResponseDTO(BeerEntity entity);

    PutBeerResponseDTO mapToPutBeerResponseDTO(BeerEntity entity);

    List<GetBeerResponseDTO> mapToGetBeerResponseDTOList(List<BeerEntity> entity);
}