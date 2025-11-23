package org.beerinfo.handlers.beer;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.data.entity.BeerEntity;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.service.BeerService;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

@OpenApi(
        summary = "Get beer by ID",
        operationId = "getBeerById",
        path = "/beer",
        methods = HttpMethod.GET,
        tags = {"Beer"},
        queryParams = {
                @OpenApiParam(
                        name = "beerId",
                        required = true,
                        description = "ID of the beer",
                        type = Long.class,
                        example = "42"
                )
        },
        responses = {
                @OpenApiResponse(
                        status = "200",
                        description = "Beer found",
                        content = @OpenApiContent(from = GetBeerResponseDTO.class)
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
                        description = "Beer not found"
                )
        }
)
public class GetBeerByIdHandler implements Handler {
    private final BeerService beerService;

    public GetBeerByIdHandler(BeerService beerService) {
        this.beerService = beerService;
    }

    @Override
    public void handle(@NotNull Context context) {
        Long beerId = ValidationUtils.extractAndValidateQueryParam(
                context,"beerId", Long::parseLong,"Invalid Beer ID format. Only numeric values are allowed.");

        if (beerId == null) return;

        Optional<BeerEntity> beerOptional = beerService.getBeerById(beerId);

        if (beerOptional.isPresent()) {
            BeerEntity beer = beerOptional.get();
            GetBeerResponseDTO getBeerResponseDTO = BeerMapper.MAPPER.mapToGetBeerResponseDTO(beer);
            context.status(200);
            context.json(getBeerResponseDTO);
        } else {
            respondWithError(context, 404, "Beer with id: " + beerId + " not found");
        }
    }
}