package org.beerinfo.mapper;

import org.beerinfo.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.dto.data.BreweryCreationDTO;
import org.beerinfo.entity.BreweryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BreweryMapper {
    BreweryMapper MAPPER = Mappers.getMapper(BreweryMapper.class);

    BreweryEntity mapToBreweryEntity(BreweryCreationDTO dto);

    GetBreweryResponseDTO mapToGetBreweryResponseDTO(BreweryEntity entity);

    List<GetBreweryResponseDTO> mapToGetBeerResponseDTOList(List<BreweryEntity> entity);
}