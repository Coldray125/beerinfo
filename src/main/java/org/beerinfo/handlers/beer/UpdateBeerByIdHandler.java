package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beerinfo.dto.data.BeerCreationDTO;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.service.BeerService;
import org.beerinfo.utils.ResponseUtil;
import org.beerinfo.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.put;

public class UpdateBeerByIdHandler {
    private final BeerService beerService;
    private final ObjectMapper objectMapper;

    public UpdateBeerByIdHandler(BeerService beerService) {
        this.beerService = beerService;
        this.objectMapper = new ObjectMapper();
    }

    public void registerRoute() {
        put("/beer/:id", (request, response) -> {
            String idString = request.params(":id");
            try {
                BeerCreationDTO beerCreationDTO = objectMapper.readValue(request.body(), BeerCreationDTO.class);

                String validationError = ValidationUtils.validateDTO(objectMapper, beerCreationDTO);
                if (validationError != null) {
                    ResponseUtil.setJsonResponseCode(response, 400);
                    return validationError;
                }

                final BeerEntity beer = BeerMapper.MAPPER.mapToBeerEntity(beerCreationDTO);

                long id = Long.parseLong(idString);
                boolean updateResult = beerService.updateBeerById(beer, id);

                if (updateResult) {
                    ResponseUtil.setJsonResponseCode(response, 200);
                    Map<String, Object> responseData = new HashMap<>();

                    responseData.put("beer", beerCreationDTO);
                    responseData.put("message", String.format("Beer with id: %s was updated.", idString));

                    return objectMapper.writeValueAsString(responseData);
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