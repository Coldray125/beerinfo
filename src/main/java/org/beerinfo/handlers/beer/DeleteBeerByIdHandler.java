package org.beerinfo.handlers.beer;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.service.BeerService;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.beerinfo.utils.ResponseUtil.respondWithError;
import static org.beerinfo.utils.ResponseUtil.respondWithInternalServerError;

public class DeleteBeerByIdHandler implements Handler {
    private final BeerService beerService;

    public DeleteBeerByIdHandler(BeerService beerService) {
        this.beerService = beerService;
    }

    @Override
    public void handle(@NotNull Context context) {
        Long beerId = ValidationUtils.extractAndValidateQueryParam(
                context,"beerId", Long::parseLong,"Invalid Beer ID format. Only numeric values are allowed.");

        if (beerId == null) return;

        try {
            boolean deleteResult = beerService.deleteBeerById(beerId);

            if (deleteResult) {
                context.status(200);
                context.json(Map.of("message", "Beer with id: " + beerId + " was deleted"));
            } else {
                respondWithError(context, 404, "Beer with id: " + beerId + " not found");
            }
        } catch (Exception e) {
            respondWithInternalServerError(context);
        }
    }
}