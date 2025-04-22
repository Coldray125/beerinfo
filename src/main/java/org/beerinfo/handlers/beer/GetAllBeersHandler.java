package org.beerinfo.handlers.beer;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.data.entity.BeerEntity;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.service.BeerService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

public class GetAllBeersHandler implements Handler {
    private final BeerService beerService;

    public GetAllBeersHandler(BeerService beerService) {
        this.beerService = beerService;
    }

    @Override
    public void handle(@NotNull Context context) {
        Optional<List<BeerEntity>> beers = beerService.getAllBeers();
        List<GetBeerResponseDTO> getBeerResponseDTO;

        if (beers.isEmpty()) {
            respondWithError(context, 404, "Beers not found");
        } else {
            getBeerResponseDTO = BeerMapper.MAPPER.mapToGetBeerResponseDTOList(beers.get());
            context.status(200);
            context.json(getBeerResponseDTO);
        }
    }
}