package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beerinfo.converters.BeerDTOConverter;
import org.beerinfo.dto.api.beer.PostBeerResponseDTO;
import org.beerinfo.dto.data.BeerCreationDTO;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.service.BeerService;
import org.beerinfo.service.BreweriesService;
import org.beerinfo.utils.ResponseUtil;
import org.beerinfo.utils.ValidationUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static spark.Spark.post;

public class AddBeerHandler {
    private final BeerService beerService;
    private final BreweriesService breweriesService;
    private final ObjectMapper objectMapper;

    public AddBeerHandler(BeerService beerService, BreweriesService breweriesService) {
        this.beerService = beerService;
        this.breweriesService = breweriesService;
        this.objectMapper = new ObjectMapper();
    }

    public void registerRoute() {
        post("/beer", (request, response) -> {
            try {
                BeerCreationDTO beerCreationDTO = objectMapper.readValue(request.body(), BeerCreationDTO.class);

                String validationError = ValidationUtils.validateDTO(objectMapper, beerCreationDTO);
                if (validationError != null) {
                    ResponseUtil.setJsonResponseCode(response, 400);
                    return validationError;
                }

                final BeerEntity beer = BeerDTOConverter.convertToBeerEntity.apply(beerCreationDTO);

                long lastBreweryId = breweriesService.getLastBreweryId();
                int breweryIdFromRequest = beer.getBreweryId();

                if (breweryIdFromRequest > lastBreweryId) {
                    ResponseUtil.setJsonResponseCode(response, 404);
                    return String.format("{\"error\": \"Wrong brewery id: %s, brewery cannot be added.\"}", breweryIdFromRequest);
                }

                Optional<BeerEntity> beerEntity = beerService.addBeer(beer);
                if (beerEntity.isPresent()) {
                    long generatedBeerId = beerEntity.get().getBeerId();
                    beer.setBeerId(generatedBeerId);
                } else {
                    ResponseUtil.respondWithError(
                            response, 500, "Error occurred while adding the beer. Please check if the provided data is valid");
                }

                final PostBeerResponseDTO getBeerResponseDTO = BeerDTOConverter.convertToPostBeerResponseDTO.apply(beer);

                ResponseUtil.setJsonResponseCode(response, 200);

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("beer", getBeerResponseDTO);
                responseData.put("message", "Beer added successfully.");

                return objectMapper.writeValueAsString(responseData);
            } catch (IOException e) {
                return ResponseUtil.respondWithError(
                        response, 500, "Error occurred while processing the request.");
            }
        });
    }
}