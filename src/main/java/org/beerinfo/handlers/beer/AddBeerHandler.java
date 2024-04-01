package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.dto.api.beer.PostBeerResponseDTO;
import org.beerinfo.dto.data.BeerCreationDTO;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.service.BeerService;
import org.beerinfo.service.BreweriesService;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;
import static org.beerinfo.utils.ResponseUtil.respondWithInternalServerError;

public class AddBeerHandler implements Handler {
    private final BeerService beerService;
    private final BreweriesService breweriesService;
    private final ObjectMapper objectMapper;

    public AddBeerHandler(BeerService beerService, BreweriesService breweriesService) {
        this.beerService = beerService;
        this.breweriesService = breweriesService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(@NotNull Context context) {
        try {
            BeerCreationDTO beerCreationDTO = objectMapper.readValue(context.body(), BeerCreationDTO.class);

            Map<String, List<String>> validationError = ValidationUtils.validateDTO(beerCreationDTO);
            if (validationError != null) {
                context.status(400);
                context.json(validationError);
                return;
            }

            final BeerEntity beer = BeerMapper.MAPPER.mapToBeerEntity(beerCreationDTO);

            long lastBreweryId = breweriesService.getLastBreweryId();
            long breweryIdFromRequest = beer.getBreweryId();

            if (breweryIdFromRequest > lastBreweryId) {
                respondWithError(context, 404, STR."Wrong brewery id: \{breweryIdFromRequest}, brewery cannot be added.");
                return;
            }

            Optional<BeerEntity> beerEntity = beerService.addBeer(beer);
            if (beerEntity.isPresent()) {
                long generatedBeerId = beerEntity.get().getBeerId();
                beer.setBeerId(generatedBeerId);
            } else {
                respondWithError(context, 500, "Error occurred while adding the beer. Please check if the provided data is valid");
            }

            final PostBeerResponseDTO getBeerResponseDTO = BeerMapper.MAPPER.mapToPostBeerResponseDTO(beer);
            context.status(200);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("beer", getBeerResponseDTO);
            responseData.put("message", "Beer added successfully.");
            context.json(responseData);
        } catch (IOException e) {
            respondWithInternalServerError(context);
        }
    }
}