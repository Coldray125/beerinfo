package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.service.BeerService;
import org.beerinfo.converters.BeerDTOConverter;
import org.beerinfo.utils.ResponseUtil;

import java.util.List;
import java.util.Optional;

import static spark.Spark.get;

public class GetAllBeersHandler {
    private final BeerService beerService;
    private final ObjectMapper objectMapper;

    public GetAllBeersHandler(BeerService beerService) {
        this.beerService = beerService;
        this.objectMapper = new ObjectMapper();
    }

    public void registerRoute() {
        get("/beers", (request, response) -> {
            Optional<List<BeerEntity>> beers = beerService.getAllBeers();
            List<GetBeerResponseDTO> getBeerResponseDTO;

            if (beers.isEmpty()) {
                return ResponseUtil.respondWithError(response, 404, "Beers not found.");
            } else {
                getBeerResponseDTO = BeerDTOConverter.convertBeerEntityListToResponseDTOList(beers.get());
                ResponseUtil.setJsonResponseCode(response, 200);
                return objectMapper.writeValueAsString(getBeerResponseDTO);
            }
        });
    }
}