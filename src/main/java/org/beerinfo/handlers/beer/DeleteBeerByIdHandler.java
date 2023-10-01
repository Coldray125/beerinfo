package org.beerinfo.handlers.beer;

import org.beerinfo.service.BeerService;
import org.beerinfo.utils.ResponseUtil;

import static spark.Spark.delete;

public class DeleteBeerByIdHandler {
    private final BeerService beerService;

    public DeleteBeerByIdHandler(BeerService beerService) {
        this.beerService = beerService;
    }

    public void registerRoute() {
        delete("/beer/:id", (request, response) -> {
            String idString = request.params(":id");

            try {
                long id = Long.parseLong(idString);
                boolean deleteResult = beerService.deleteBeerById(id);

                if (deleteResult) {
                    ResponseUtil.setJsonResponseCode(response, 200);
                    return String.format("{\"message\": \"Beer with id: %s was deleted.\"}", idString);
                } else {
                    ResponseUtil.setJsonResponseCode(response, 404);
                    return String.format("{\"error\": \"Beer with id: %s not found.\"}", idString);
                }
            } catch (NumberFormatException e) {
                return ResponseUtil.respondWithError(
                        response, 400, "Invalid Beer ID format. Only numeric values are allowed.");
            } catch (Exception e) {
                return ResponseUtil.respondWithError(
                        response, 500, "Error occurred while processing the request.");
            }
        });
    }
}