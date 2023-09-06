package org.beerinfo.converters;

import org.beerinfo.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.dto.data.BreweryCreationDTO;
import org.beerinfo.entity.BreweryEntity;

import java.util.ArrayList;
import java.util.List;

public class BreweryDTOConverter {
    public static BreweryEntity convertToBreweryEntity(BreweryCreationDTO breweryCreationDTO) {
        return BreweryEntity.builder()
                .name(breweryCreationDTO.getName())
                .city(breweryCreationDTO.getCity())
                .state(breweryCreationDTO.getState())
                .country(breweryCreationDTO.getCountry())
                .build();
    }

    public static GetBreweryResponseDTO convertBreweryEntityToResponseDTO(BreweryEntity breweryEntity) {
        return GetBreweryResponseDTO.builder()
                .breweryId(breweryEntity.getBreweryId())
                .name(breweryEntity.getName())
                .city(breweryEntity.getCity())
                .state(breweryEntity.getState())
                .country(breweryEntity.getCountry())
                .build();
    }

    public static List<GetBreweryResponseDTO> convertBreweryEntityListToResponseDTOList(List<BreweryEntity> breweryEntity) {
        List<GetBreweryResponseDTO> getBreweryResponseDTOS = new ArrayList<>();

        for (BreweryEntity entity : breweryEntity) {
            GetBreweryResponseDTO responseDTO = GetBreweryResponseDTO.builder()
                    .breweryId(entity.getBreweryId())
                    .name(entity.getName())
                    .city(entity.getCity())
                    .state(entity.getState())
                    .country(entity.getCountry())
                    .build();

            getBreweryResponseDTOS.add(responseDTO);
        }

        return getBreweryResponseDTOS;
    }
}