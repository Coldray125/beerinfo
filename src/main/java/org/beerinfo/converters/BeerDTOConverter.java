package org.beerinfo.converters;

import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.dto.api.beer.PostBeerResponseDTO;
import org.beerinfo.dto.api.beer.PutBeerResponseDTO;
import org.beerinfo.dto.data.BeerCreationDTO;
import org.beerinfo.entity.BeerEntity;

import java.util.List;
import java.util.function.Function;

public class BeerDTOConverter {
    public static Function<BeerCreationDTO, BeerEntity> convertToBeerEntity = dto ->
            BeerEntity.builder()
                    .abv(dto.getAbv())
                    .ibuNumber(dto.getIbuNumber())
                    .name(dto.getName())
                    .style(dto.getStyle())
                    .breweryId(dto.getBreweryId())
                    .ounces(dto.getOunces())
                    .build();
    public static Function<BeerEntity, PostBeerResponseDTO> convertToPostBeerResponseDTO = entity ->
            PostBeerResponseDTO.builder()
                    .beerId(entity.getBeerId())
                    .abv(entity.getAbv())
                    .ibuNumber(entity.getIbuNumber())
                    .name(entity.getName())
                    .style(entity.getStyle())
                    .breweryId(entity.getBreweryId())
                    .ounces(entity.getOunces())
                    .build();
    public static Function<BeerEntity, GetBeerResponseDTO> convertToGetBeerResponseDTO = entity ->
            GetBeerResponseDTO.builder()
                    .beerId(entity.getBeerId())
                    .abv(entity.getAbv())
                    .ibuNumber(entity.getIbuNumber())
                    .name(entity.getName())
                    .style(entity.getStyle())
                    .breweryId(entity.getBreweryId())
                    .ounces(entity.getOunces())
                    .build();

    public static Function<BeerEntity, PutBeerResponseDTO> convertToPutBeerResponseDTO = entity ->
            PutBeerResponseDTO.builder()
                    .beerId(entity.getBeerId())
                    .abv(entity.getAbv())
                    .ibuNumber(entity.getIbuNumber())
                    .name(entity.getName())
                    .style(entity.getStyle())
                    .breweryId(entity.getBreweryId())
                    .ounces(entity.getOunces())
                    .build();

    public static List<GetBeerResponseDTO> convertBeerEntityListToResponseDTOList(List<BeerEntity> beerEntity) {
        return beerEntity
                .stream()
                .map(convertToGetBeerResponseDTO)
                .toList();
    }
}