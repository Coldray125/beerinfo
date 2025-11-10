package org.beerinfo.handlers.beer;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.data.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.data.entity.JoinedBeerBreweryEntity;
import org.beerinfo.mapper.BreweryMapper;
import org.beerinfo.service.BeerService;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

/// Get brewery by beerId with query param
public class GetBeerBreweryHandler implements Handler {
    private final BeerService beerService;

    public GetBeerBreweryHandler(BeerService beerService) {
        this.beerService = beerService;
    }

    @Override
    public void handle(@NotNull Context context) {
        Long beerId = ValidationUtils.extractAndValidateQueryParam(
                context,"beerId", Long::parseLong,"Invalid Beer ID format. Only numeric values are allowed.");

        if (beerId == null) return;

        Optional<JoinedBeerBreweryEntity> beerOptional = beerService.getBeerBreweryById(beerId);
        GetBreweryResponseDTO breweryDTO;
        if (beerOptional.isEmpty()) {
            respondWithError(context, 404, "Entity not found");
        } else {
            breweryDTO = BreweryMapper.MAPPER.mapToGetBreweryResponseDTO(beerOptional.get().getBreweryEntity());
            context.status(200);
            context.json(breweryDTO);
        }
    }
}