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

        try {
            long id = Long.parseLong(beerId);
            boolean deleteResult = beerService.deleteBeerById(id);

            if (deleteResult) {
                context.status(200);
                context.json(Map.of("message:", STR."Beer with beerId: \{beerId} was deleted"));
            } else {
                respondWithError(context, 404, STR."Beer with id: \{beerId} not found");
            }
        } catch (NumberFormatException e) {
            respondWithError(context, 400, "Invalid Beer ID format. Only numeric values are allowed");
        } catch (Exception e) {
            respondWithInternalServerError(context);
        }
    }
}