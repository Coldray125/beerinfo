package org.beerinfo.converters;

import org.beerinfo.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.dto.data.BreweryCreationDTO;
import org.beerinfo.entity.BreweryEntity;

import java.util.List;
import java.util.function.Function;

public class BreweryDTOConverter {

    public static Function<BreweryCreationDTO, BreweryEntity> convertToBreweryEntity = dto ->
            BreweryEntity.builder()
                    .name(dto.getName())
                    .city(dto.getCity())
                    .state(dto.getState())
                    .country(dto.getCountry())
                    .build();

    public static Function<BreweryEntity, GetBreweryResponseDTO> convertBreweryEntityToResponseDTO = entity ->
            GetBreweryResponseDTO.builder()
                    .breweryId(entity.getBreweryId())
                    .name(entity.getName())
                    .city(entity.getCity())
                    .state(entity.getState())
                    .country(entity.getCountry())
                    .build();


    public static List<GetBreweryResponseDTO> convertBreweryEntityListToResponseDTOList(List<BreweryEntity> breweryEntity) {
        return breweryEntity
                .stream()
                .map(convertBreweryEntityToResponseDTO)
                .toList();
    }
}