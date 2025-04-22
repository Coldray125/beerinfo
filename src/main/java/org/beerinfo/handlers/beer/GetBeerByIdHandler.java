package org.beerinfo.handlers.beer;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.data.entity.BeerEntity;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.service.BeerService;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

/// Get all beers by breweryId with query param
public class GetBeerByIdHandler implements Handler {
    private final BeerService beerService;

    public GetBeerByIdHandler(BeerService beerService) {
        this.beerService = beerService;
    }

    @Override
    public void handle(@NotNull Context context) {
        String beerId = context.queryParam("beerId");

        long id;
        try {
            id = Long.parseLong(beerId);
        } catch (NumberFormatException e) {
            respondWithError(context, 400, "Invalid Beer ID format. Only numeric values are allowed.");
            return;
        }

        Optional<BeerEntity> beerOptional = beerService.getBeerById(id);

        if (beerOptional.isPresent()) {
            BeerEntity beer = beerOptional.get();
            GetBeerResponseDTO getBeerResponseDTO = BeerMapper.MAPPER.mapToGetBeerResponseDTO(beer);
            context.status(200);
            context.json(getBeerResponseDTO);
        } else {
            respondWithError(context, 404, "Beer with id: " + beerId + " not found");
        }
    }
}