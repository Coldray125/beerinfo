package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.beerinfo.data.dto.BeerCreationDTO;
import org.beerinfo.data.dto.api.beer.PostBeerResponseDTO;
import org.beerinfo.data.entity.BeerEntity;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.service.BeerService;
import org.beerinfo.service.BreweriesService;
import org.beerinfo.utils.JsonUtils;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

@OpenApi(
        summary = "Add a new beer",
        operationId = "addBeer",
        path = "/beer",
        methods = HttpMethod.POST,
        tags = {"Beer"},
        requestBody = @OpenApiRequestBody(
                required = true,
                description = "Beer creation payload",
                content = @OpenApiContent(from = BeerCreationDTO.class)
        ),
        responses = {
                @OpenApiResponse(
                        status = "200",
                        description = "Beer added successfully",
                        content = {@OpenApiContent(from = PostBeerResponseDTO[].class)}
                ),
                @OpenApiResponse(
                        status = "400",
                        description = "Invalid body or validation errors",
                        content = @OpenApiContent(
                                type = "object",
                                example = """
                                          {
                                            "fieldName": ["must not be null", "must be greater than 0"]
                                          }
                                        """
                        )
                ),
                @OpenApiResponse(
                        status = "404",
                        description = "Wrong brewery id: {breweryIdFromRequest} brewery cannot be added"
                )
        }
)
public class AddBeerHandler implements Handler {
    private final BeerService beerService;
    private final BreweriesService breweriesService;

    public AddBeerHandler(BeerService beerService, BreweriesService breweriesService) {
        this.beerService = beerService;
        this.breweriesService = breweriesService;
    }

    @Override
    public void handle(@NotNull Context context) {
        BeerCreationDTO beerCreationDTO;

        try {
            beerCreationDTO = JsonUtils.jsonStringToObject(context.body(), BeerCreationDTO.class);
        } catch (JsonProcessingException e) {
            respondWithError(context, 400, "Invalid value format in body");
            return;
        }

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
            respondWithError(context, 404,
                    "Wrong brewery id: " + breweryIdFromRequest + ", brewery cannot be added.");
            return;
        }

        Optional<BeerEntity> addedBeer = beerService.addBeer(beer);
        if (addedBeer.isEmpty()) {
            respondWithError(context, 500,
                    "Error occurred while adding the beer. Please check if the provided data is valid");
            return;
        }

        beer.setBeerId(addedBeer.get().getBeerId());
        final var postBeerResponseDTO = BeerMapper.MAPPER.mapToPostBeerResponseDTO(beer);

        context.status(200).json(Map.of(
                "beer", postBeerResponseDTO,
                "message", "Beer added successfully."
        ));
    }
}