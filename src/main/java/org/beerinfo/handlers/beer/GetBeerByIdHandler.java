package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.service.BeerService;
import org.beerinfo.converters.BeerDTOConverter;
import org.beerinfo.utils.ResponseUtil;

import java.io.IOException;
import java.util.Optional;

import static spark.Spark.get;

public class GetBeerByIdHandler {
    private final BeerService beerService;
    private final ObjectMapper objectMapper;

    public GetBeerByIdHandler(BeerService beerService) {
        this.beerService = beerService;
        this.objectMapper = new ObjectMapper();
    }

    public void registerRoute() {
        get("/beer/:id", (request, response) -> {
            String idString = request.params(":id");

            long id;
            try {
                id = Long.parseLong(idString);
            } catch (NumberFormatException e) {
                return ResponseUtil.respondWithError(
                        response, 400, "Invalid Beer ID format. Only numeric values are allowed.");
            }

            try {
                Optional<BeerEntity> beerOptional = beerService.getBeerById(id);

                if (beerOptional.isPresent()) {
                    BeerEntity beer = beerOptional.get();
                    GetBeerResponseDTO getBeerResponseDTO = BeerDTOConverter.convertToGetBeerResponseDTO.apply(beer);
                    ResponseUtil.setJsonResponseCode(response, 200);
                    return objectMapper.writeValueAsString(getBeerResponseDTO);
                } else {
                    ResponseUtil.setJsonResponseCode(response, 404);
                    return String.format("{\"error\": \"Beer with id: %s not found.\"}", idString);
                }
            } catch (IOException e) {
                return ResponseUtil.respondWithError(
                        response, 500, "Error occurred while processing the request.");
            }
        });
    }
}