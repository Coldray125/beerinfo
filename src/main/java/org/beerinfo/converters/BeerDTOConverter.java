package org.beerinfo.converters;

import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.dto.api.beer.PostBeerResponseDTO;
import org.beerinfo.dto.api.beer.PutBeerResponseDTO;
import org.beerinfo.dto.data.BeerCreationDTO;
import org.beerinfo.entity.BeerEntity;

import java.util.ArrayList;
import java.util.List;

public class BeerDTOConverter {
    public static BeerEntity convertToBeerEntity(BeerCreationDTO beerCreationDTO) {
        return BeerEntity.builder()
                .abv(beerCreationDTO.getAbv())
                .ibuNumber(beerCreationDTO.getIbuNumber())
                .name(beerCreationDTO.getName())
                .style(beerCreationDTO.getStyle())
                .breweryId(beerCreationDTO.getBreweryId())
                .ounces(beerCreationDTO.getOunces())
                .build();
    }

    public static GetBeerResponseDTO convertToGetBeerResponseDTO(BeerEntity beerEntity) {
        return GetBeerResponseDTO.builder()
                .beerId(beerEntity.getBeerId())
                .abv(beerEntity.getAbv())
                .ibuNumber(beerEntity.getIbuNumber())
                .name(beerEntity.getName())
                .style(beerEntity.getStyle())
                .breweryId(beerEntity.getBreweryId())
                .ounces(beerEntity.getOunces())
                .build();
    }

    public static PostBeerResponseDTO convertToPostBeerResponseDTO(BeerEntity beerEntity) {
        return PostBeerResponseDTO.builder()
                .beerId(beerEntity.getBeerId())
                .abv(beerEntity.getAbv())
                .ibuNumber(beerEntity.getIbuNumber())
                .name(beerEntity.getName())
                .style(beerEntity.getStyle())
                .breweryId(beerEntity.getBreweryId())
                .ounces(beerEntity.getOunces())
                .build();
    }

    public static PutBeerResponseDTO convertToPutBeerResponseDTO(BeerEntity beerEntity) {
        return PutBeerResponseDTO.builder()
                .beerId(beerEntity.getBeerId())
                .abv(beerEntity.getAbv())
                .ibuNumber(beerEntity.getIbuNumber())
                .name(beerEntity.getName())
                .style(beerEntity.getStyle())
                .breweryId(beerEntity.getBreweryId())
                .ounces(beerEntity.getOunces())
                .build();
    }


    public static List<GetBeerResponseDTO> convertBeerEntityListToResponseDTOList(List<BeerEntity> beerEntities) {
        List<GetBeerResponseDTO> getBeerResponseDTOS = new ArrayList<>();

        for (BeerEntity entity : beerEntities) {
            GetBeerResponseDTO responseDTO = GetBeerResponseDTO.builder()
                    .beerId(entity.getBeerId())
                    .abv(entity.getAbv())
                    .ibuNumber(entity.getIbuNumber())
                    .name(entity.getName())
                    .style(entity.getStyle())
                    .breweryId(entity.getBreweryId())
                    .ounces(entity.getOunces())
                    .build();

            getBeerResponseDTOS.add(responseDTO);
        }
        return getBeerResponseDTOS;
    }
}