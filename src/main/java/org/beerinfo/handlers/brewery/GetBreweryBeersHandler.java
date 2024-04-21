package org.beerinfo.handlers.brewery;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.entity.JoinedBreweryBeerEntity;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.service.BreweriesService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

public class GetBreweryBeersHandler implements Handler {
    private final BreweriesService breweriesService;


    public GetBreweryBeersHandler(BreweriesService breweriesService) {
        this.breweriesService = breweriesService;
    }

    @Override
    public void handle(@NotNull Context context) {
        String breweryId = context.queryParam("breweryId");

        long id;
        try {
            id = Long.parseLong(breweryId);
        } catch (NumberFormatException e) {
            respondWithError(context, 400, "Invalid Brewery ID format. Only numeric values are allowed.");
            return;
        }

        Optional<JoinedBreweryBeerEntity> breweryOptional = breweriesService.getBreweryBeersById(id);
        List<GetBeerResponseDTO> listBeerDTO;
        if (breweryOptional.isEmpty()) {
            respondWithError(context, 404, "Entity not found");
        } else {
            listBeerDTO = BeerMapper.MAPPER.mapToGetBeerResponseDTOList(breweryOptional.get().getBeerEntities());
            context.status(200);
            context.json(listBeerDTO);
        }
    }
}