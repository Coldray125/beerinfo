package org.beerinfo.handlers.beer;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.entity.JoinedBeerBreweryEntity;
import org.beerinfo.mapper.BreweryMapper;
import org.beerinfo.service.BeerService;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

public class GetBeerBreweryHandler implements Handler {
    private final BeerService beerService;

    public GetBeerBreweryHandler(BeerService beerService) {
        this.beerService = beerService;
    }

    @Override
    public void handle(@NotNull Context context) {
        String beerId = context.queryParam("beerId");

        long id = 0;
        try {
            id = Long.parseLong(beerId);
        } catch (NumberFormatException e) {
            context.status(400);
            context.json("Invalid Beer ID format. Only numeric values are allowed.");
        }

        Optional<JoinedBeerBreweryEntity> beerOptional = beerService.getBeerBreweryById(id);
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