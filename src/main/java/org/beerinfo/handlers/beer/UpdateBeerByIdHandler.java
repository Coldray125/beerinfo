package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.dto.data.BeerCreationDTO;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.service.BeerService;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static org.beerinfo.utils.ResponseUtil.respondWithError;

public class UpdateBeerByIdHandler implements Handler {
    private final BeerService beerService;
    private final ObjectMapper objectMapper;

    public UpdateBeerByIdHandler(BeerService beerService) {
        this.beerService = beerService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(@NotNull Context context) {
        String beerId = context.queryParam("beerId");

        try {
            BeerCreationDTO beerCreationDTO = objectMapper.readValue(context.body(), BeerCreationDTO.class);
            String validationError = ValidationUtils.validateDTO(objectMapper, beerCreationDTO);
            if (validationError != null) {
                respondWithError(context, 400, validationError);
            }

            final BeerEntity beer = BeerMapper.MAPPER.mapToBeerEntity(beerCreationDTO);

            long id = Long.parseLong(beerId);
            boolean updateResult = beerService.updateBeerById(beer, id);

            if (updateResult) {
                context.status(200);
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("beer", beerCreationDTO);
                responseData.put("message", STR."Beer with id: \{beerId} was updated.");
                context.json(responseData);
            } else {
                respondWithError(context, 404, STR."Beer with id: \{beerId} not found");
            }
        } catch (NumberFormatException e) {
            respondWithError(context, 400, "Invalid Beer ID format. Only numeric values are allowed");
        } catch (Exception e) {
            respondWithError(context, 500, "Error occurred while processing the request.");
        }
    }
}