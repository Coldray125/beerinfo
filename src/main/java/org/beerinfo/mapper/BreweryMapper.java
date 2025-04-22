package org.beerinfo.mapper;

import org.beerinfo.data.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.data.dto.BreweryCreationDTO;
import org.beerinfo.data.entity.BreweryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BreweryMapper {
    BreweryMapper MAPPER = Mappers.getMapper(BreweryMapper.class);

    @Mapping(target = "breweryId", ignore = true)
    BreweryEntity mapToBreweryEntity(BreweryCreationDTO dto);

    GetBreweryResponseDTO mapToGetBreweryResponseDTO(BreweryEntity entity);

    List<GetBreweryResponseDTO> mapToGetBeerResponseDTOList(List<BreweryEntity> entity);
}