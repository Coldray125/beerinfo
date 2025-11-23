package org.beerinfo.handlers.brewery;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.data.entity.JoinedBreweryBeerEntity;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.service.BreweriesService;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

@OpenApi(
        summary = "Get all beers by brewery ID",
        operationId = "getBreweryBeers",
        path = "/brewery/beers",
        methods = HttpMethod.GET,
        tags = {"Brewery"},
        queryParams = {
                @OpenApiParam(
                        name = "breweryId",
                        required = true,
                        description = "ID of the brewery",
                        type = Long.class,
                        example = "1"
                )
        },
        responses = {
                @OpenApiResponse(
                        status = "200",
                        description = "List of beers for the brewery",
                        content = @OpenApiContent(from = GetBeerResponseDTO[].class)
                ),
                @OpenApiResponse(
                        status = "400",
                        description = "Invalid breweryId query parameter",
                        content = @OpenApiContent(
                                type = "object",
                                example = """
                                          {
                                            "breweryId": ["Invalid Brewery ID format. Only numeric values are allowed."]
                                          }
                                        """
                        )
                ),
                @OpenApiResponse(
                        status = "404",
                        description = "Brewery not found or has no beers"
                )
        }
)
public class GetBreweryBeersHandler implements Handler {
    private final BreweriesService breweriesService;

    public GetBreweryBeersHandler(BreweriesService breweriesService) {
        this.breweriesService = breweriesService;
    }

    @Override
    public void handle(@NotNull Context context) {
        Long breweryId = ValidationUtils.extractAndValidateQueryParam(
                context, "breweryId", Long::parseLong, "Invalid Brewery ID format. Only numeric values are allowed.");

        if (breweryId == null) return;

        Optional<JoinedBreweryBeerEntity> breweryOptional = breweriesService.getBreweryBeersById(breweryId);
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