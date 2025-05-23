package org.beerinfo.handlers.beer;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.service.BeerService;
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
        String beerId = context.queryParam("beerId");

        long id;
        try {
            id = Long.parseLong(beerId);
        } catch (NumberFormatException e) {
            respondWithError(context, 400, "Invalid Beer ID format. Only numeric values are allowed.");
            return;
        }

        try {
            boolean deleteResult = beerService.deleteBeerById(id);

            if (deleteResult) {
                context.status(200);
                context.json(Map.of("message", "Beer with beerId: " + beerId + " was deleted"));
            } else {
                respondWithError(context, 404, "Beer with id: " + beerId + " not found");
            }
        } catch (Exception e) {
            respondWithInternalServerError(context);
        }
    }
}