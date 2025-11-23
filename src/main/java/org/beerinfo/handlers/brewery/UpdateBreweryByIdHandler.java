package org.beerinfo.handlers.brewery;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.beerinfo.data.dto.BreweryCreationDTO;
import org.beerinfo.data.entity.BreweryEntity;
import org.beerinfo.mapper.BreweryMapper;
import org.beerinfo.service.BreweriesService;
import org.beerinfo.utils.JsonUtils;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static org.beerinfo.enums.SupportedCountry.isValidCountry;
import static org.beerinfo.utils.ResponseUtil.respondWithError;

@OpenApi(
        summary = "Update brewery by ID",
        operationId = "updateBreweryById",
        path = "/brewery",
        methods = HttpMethod.PUT,
        tags = {"Brewery"},
        queryParams = {
                @OpenApiParam(
                        name = "breweryId",
                        required = true,
                        description = "ID of the brewery to update",
                        type = Long.class,
                        example = "1"
                )
        },
        requestBody = @OpenApiRequestBody(
                required = true,
                description = "Brewery update payload",
                content = @OpenApiContent(from = BreweryCreationDTO.class)
        ),
        responses = {
                @OpenApiResponse(
                        status = "200",
                        description = "Brewery updated successfully",
                        content = @OpenApiContent(
                                type = "object",
                                example = """
                                          {
                                            "brewery": {
                                              "name": "Updated Brewery Name",
                                              "country": "Germany"
                                            },
                                            "message": "Brewery with id: 1 was updated."
                                          }
                                        """
                        )
                ),
                @OpenApiResponse(
                        status = "400",
                        description = "Invalid query param, body format, or validation errors",
                        content = @OpenApiContent(
                                type = "object",
                                example = """
                                          {
                                            "breweryId": ["Invalid Brewery ID format. Only numeric values are allowed."],
                                            "country": ["Country France is not supported. Please provide a valid country"]
                                          }
                                        """
                        )
                ),
                @OpenApiResponse(
                        status = "404",
                        description = "Brewery with given ID not found"
                )
        }
)
public class UpdateBreweryByIdHandler implements Handler {
    private final BreweriesService breweriesService;

    public UpdateBreweryByIdHandler(BreweriesService breweriesService) {
        this.breweriesService = breweriesService;
    }

    @Override
    public void handle(@NotNull Context context) {
        Long breweryId = ValidationUtils.extractAndValidateQueryParam(
                context, "breweryId", Long::parseLong, "Invalid Brewery ID format. Only numeric values are allowed.");

        if (breweryId == null) return;

        BreweryCreationDTO breweryCreationDTO;

        try {
            breweryCreationDTO = JsonUtils.jsonStringToObject(context.body(), BreweryCreationDTO.class);
        } catch (JsonProcessingException e) {
            respondWithError(context, 400, "Invalid value format in body");
            return;
        }

        Map<String, List<String>> validationError = ValidationUtils.validateDTO(breweryCreationDTO);
        if (validationError != null) {
            context.status(400);
            context.json(validationError);
            return;
        }

        String country = breweryCreationDTO.getCountry();
        if (!isValidCountry(country)) {
            respondWithError(context, 400, "Country " + country + " is not supported. Please provide a valid country");
            return;
        }

        final BreweryEntity brewery = BreweryMapper.MAPPER.mapToBreweryEntity(breweryCreationDTO);

        boolean updated = breweriesService.updateBreweryById(brewery, breweryId);

        if (!updated) {
            respondWithError(context, 404, "Brewery with id: " + breweryId + " not found");
            return;
        }

        context.status(200).json(Map.of(
                "brewery", breweryCreationDTO,
                "message", "Brewery with id: " + breweryId + " was updated."
        ));
    }
}