package org.beerinfo.handlers.brewery;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.entity.BreweryEntity;
import org.beerinfo.mapper.BreweryMapper;
import org.beerinfo.service.BreweriesService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

public class GetAllBreweriesHandler implements Handler {
    private final BreweriesService breweriesService;

    public GetAllBreweriesHandler(BreweriesService breweriesService) {
        this.breweriesService = breweriesService;
    }

    @Override
    public void handle(@NotNull Context context) {
        Optional<List<BreweryEntity>> breweries = breweriesService.getAllBreweries();
        List<GetBreweryResponseDTO> getBreweryResponseDTO;

        if (breweries.isEmpty()) {
            respondWithError(context, 404, "Breweries not found");
        } else {
            getBreweryResponseDTO = BreweryMapper.MAPPER.mapToGetBeerResponseDTOList(breweries.get());
            context.status(200);
            context.json(getBreweryResponseDTO);
        }
    }
}