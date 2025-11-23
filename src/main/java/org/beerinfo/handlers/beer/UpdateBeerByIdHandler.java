package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.beerinfo.data.dto.BeerCreationDTO;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.service.BeerService;
import org.beerinfo.utils.JsonUtils;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

@OpenApi(
        summary = "Update beer by ID",
        operationId = "updateBeerById",
        path = "/beer",
        methods = HttpMethod.PUT,
        tags = {"Beer"},
        queryParams = {
                @OpenApiParam(
                        name = "beerId",
                        required = true,
                        description = "ID of the beer to update",
                        type = Long.class,
                        example = "42"
                )
        },
        requestBody = @OpenApiRequestBody(
                required = true,
                description = "Beer update payload",
                content = @OpenApiContent(from = BeerCreationDTO.class)
        ),
        responses = {
                @OpenApiResponse(
                        status = "200",
                        description = "Beer updated successfully",
                        content = @OpenApiContent(
                                type = "object",
                                example = """
                                          {
                                            "beer": {
                                              "name": "Updated Beer Name",
                                              "alcoholPercent": 5.0,
                                              "breweryId": 1
                                            },
                                            "message": "Beer with id: 42 was updated."
                                          }
                                        """
                        )
                ),
                @OpenApiResponse(
                        status = "400",
                        description = "Invalid query param or validation errors",
                        content = @OpenApiContent(
                                type = "object",
                                example = """
                                          {
                                            "beerId": ["Invalid Beer ID format. Only numeric values are allowed."],
                                            "name": ["must not be blank"]
                                          }
                                        """
                        )
                ),
                @OpenApiResponse(
                        status = "404",
                        description = "Beer with given ID not found"
                )
        }
)
public class UpdateBeerByIdHandler implements Handler {
    private final BeerService beerService;

    public UpdateBeerByIdHandler(BeerService beerService) {
        this.beerService = beerService;
    }

    @Override
    public void handle(@NotNull Context context) {
        Long beerId = ValidationUtils.extractAndValidateQueryParam(
                context, "beerId", Long::parseLong, "Invalid Beer ID format. Only numeric values are allowed.");

        if (beerId == null) return;

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

        final var beerEntity = BeerMapper.MAPPER.mapToBeerEntity(beerCreationDTO);
        boolean updated = beerService.updateBeerById(beerEntity, beerId);

        if (!updated) {
            respondWithError(context, 404, "Beer with id: " + beerId + " not found");
            return;
        }

        context.status(200).json(Map.of(
                "beer", beerCreationDTO,
                "message", "Beer with id: " + beerId + " was updated."));
    }
}