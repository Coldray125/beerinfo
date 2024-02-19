package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.service.BeerService;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;
import static org.beerinfo.utils.ResponseUtil.respondWithInternalServerError;

public class GetBeerByIdHandler implements Handler {
    private final BeerService beerService;
    private final ObjectMapper objectMapper;

    public GetBeerByIdHandler(BeerService beerService) {
        this.beerService = beerService;
        this.objectMapper = new ObjectMapper();
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

        Optional<BeerEntity> beerOptional = beerService.getBeerById(id);

        if (beerOptional.isPresent()) {
            BeerEntity beer = beerOptional.get();
            GetBeerResponseDTO getBeerResponseDTO = BeerMapper.MAPPER.mapToGetBeerResponseDTO(beer);
            context.status(200);
            context.json(getBeerResponseDTO);
        } else {
           respondWithError(context, 404, STR."Beer with id: \{beerId} not found");
        }
    }
}