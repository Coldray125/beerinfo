package org.beerinfo.handlers.beer;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.beerinfo.data.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.data.entity.JoinedBeerBreweryEntity;
import org.beerinfo.mapper.BreweryMapper;
import org.beerinfo.service.BeerService;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

@OpenApi(
        summary = "Get brewery by beerId with query param",
        operationId = "getBeerBrewery",
        path = "/beer/brewery",
        methods = HttpMethod.GET,
        tags = {"Beer"},
        queryParams = {
                @OpenApiParam(
                        name = "beerId",
                        required = true,
                        description = "ID of the beer",
                        type = Long.class,
                        example = "123"
                )
        },
        responses = {
                @OpenApiResponse(
                        status = "200",
                        description = "Brewery info for the beer",
                        content = @OpenApiContent(from = GetBreweryResponseDTO.class)
                ),
                @OpenApiResponse(
                        status = "400",
                        description = "Invalid beerId query parameter",
                        content = @OpenApiContent(
                                type = "object",
                                example = """
                                          {
                                            "beerId": ["Invalid Beer ID format. Only numeric values are allowed."]
                                          }
                                        """
                        )
                ),
                @OpenApiResponse(
                        status = "404",
                        description = "Entity not found (beer or brewery missing)"
                )
        }
)
public class GetBeerBreweryHandler implements Handler {
    private final BeerService beerService;

    public GetBeerBreweryHandler(BeerService beerService) {
        this.beerService = beerService;
    }

    @Override
    public void handle(@NotNull Context context) {
        Long beerId = ValidationUtils.extractAndValidateQueryParam(
                context, "beerId", Long::parseLong, "Invalid Beer ID format. Only numeric values are allowed.");

        if (beerId == null) return;

        Optional<JoinedBeerBreweryEntity> beerOptional = beerService.getBeerBreweryById(beerId);
        GetBreweryResponseDTO breweryDTO;
        if (beerOptional.isEmpty()) {
            respondWithError(context, 404, "Entity not found");
        } else {
            breweryDTO = BreweryMapper.MAPPER.mapToGetBreweryResponseDTO(beerOptional.get().getBreweryEntity());
            context.status(200);
            context.json(breweryDTO);
        }
    }
}