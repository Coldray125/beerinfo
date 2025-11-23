package org.beerinfo.handlers.beer;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.beerinfo.service.BeerService;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

@OpenApi(
        summary = "Delete beer by id",
        operationId = "deleteBeerById",
        path = "/beer",
        methods = HttpMethod.DELETE,
        tags = {"Beer"},
        queryParams = {
                @OpenApiParam(
                        name = "beerId",
                        required = true,
                        description = "ID of the beer to delete",
                        type = Long.class
                )
        },
        responses = {
                @OpenApiResponse(
                        status = "200",
                        description = "Beer successfully deleted",
                        content = @OpenApiContent(from = String.class, example = "Beer with id: 123 was deleted"
                        )
                ),
                @OpenApiResponse(status = "404", description = "Beer not found"),
                @OpenApiResponse(status = "400", description = "Invalid or missing beerId")
        }
)
public class DeleteBeerByIdHandler implements Handler {
    private final BeerService beerService;

    public DeleteBeerByIdHandler(BeerService beerService) {
        this.beerService = beerService;
    }

    @Override
    public void handle(@NotNull Context context) {
        Long beerId = ValidationUtils.extractAndValidateQueryParam(
                context, "beerId", Long::parseLong, "Invalid Beer ID format. Only numeric values are allowed.");

        if (beerId == null) return;

        boolean deleteResult = beerService.deleteBeerById(beerId);

        if (deleteResult) {
            context.status(200);
            context.json(Map.of("message", "Beer with id: " + beerId + " was deleted"));
        } else {
            respondWithError(context, 404, "Beer with id: " + beerId + " not found");
        }
    }
}