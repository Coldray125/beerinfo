package org.beerinfo.handlers.brewery;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beerinfo.converters.BreweryDTOConverter;
import org.beerinfo.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.entity.BreweryEntity;
import org.beerinfo.service.BreweriesService;
import org.beerinfo.utils.ResponseUtil;

import java.util.List;
import java.util.Optional;

import static spark.Spark.get;

public class GetAllBreweriesHandler {
    private final BreweriesService breweriesService;
    private final ObjectMapper objectMapper;

    public GetAllBreweriesHandler(BreweriesService breweriesService) {
        this.breweriesService = breweriesService;
        this.objectMapper = new ObjectMapper();
    }

    public void registerRoute() {
        get("/breweries", (request, response) -> {
            Optional<List<BreweryEntity>> breweries = breweriesService.getAllBreweries();
            List<GetBreweryResponseDTO> getBreweryResponseDTO;

            if (breweries.isEmpty()) {
                return ResponseUtil.respondWithError(response, 404, "Breweries not found.");
            } else {
                getBreweryResponseDTO = BreweryDTOConverter.convertBreweryEntityListToResponseDTOList(breweries.get());
                ResponseUtil.setJsonResponseCode(response, 200);
                return objectMapper.writeValueAsString(getBreweryResponseDTO);
            }
        });
    }
}