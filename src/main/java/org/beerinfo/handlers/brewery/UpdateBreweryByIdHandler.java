package org.beerinfo.handlers.brewery;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beerinfo.converters.BreweryDTOConverter;
import org.beerinfo.dto.data.BreweryCreationDTO;
import org.beerinfo.entity.BreweryEntity;
import org.beerinfo.service.BreweriesService;
import org.beerinfo.utils.ResponseUtil;
import org.beerinfo.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;

import static org.beerinfo.enums.SupportedCountry.isValidCountry;
import static spark.Spark.put;

public class UpdateBreweryByIdHandler {
    private final BreweriesService breweriesService;
    private final ObjectMapper objectMapper;

    public UpdateBreweryByIdHandler(BreweriesService breweriesService) {
        this.breweriesService = breweriesService;
        this.objectMapper = new ObjectMapper();
    }

    public void registerRoute() {
        put("/brewery/:id", (request, response) -> {
            String idString = request.params(":id");
            try {
                BreweryCreationDTO breweryCreationDTO = objectMapper.readValue(request.body(), BreweryCreationDTO.class);

                String validationError = ValidationUtils.validateDTO(objectMapper, breweryCreationDTO);
                if (validationError != null) {
                    ResponseUtil.setJsonResponseCode(response, 400);
                    return validationError;
                }

                String country = breweryCreationDTO.getCountry();
                if (!isValidCountry(country)) {
                    String errorMessage = String.format("Country '%s' is not supported. Please provide a valid country.", country);
                    return ResponseUtil.respondWithError(response, 400, errorMessage);
                }

                final BreweryEntity brewery = BreweryDTOConverter.convertToBreweryEntity.apply(breweryCreationDTO);

                long id = Long.parseLong(idString);
                boolean updateResult = breweriesService.updateBreweryById(brewery, id);

                if (updateResult) {
                    ResponseUtil.setJsonResponseCode(response, 200);
                    Map<String, Object> responseData = new HashMap<>();

                    responseData.put("message", String.format("Brewery with id: %s was updated.", idString));
                    responseData.put("brewery", breweryCreationDTO);

                    return objectMapper.writeValueAsString(responseData);
                } else {
                    String errorMessage = String.format("Brewery with id: %s not found.", idString);
                    return ResponseUtil.respondWithError(response, 404, errorMessage);
                }
            } catch (NumberFormatException e) {
                return ResponseUtil.respondWithError(
                        response, 400, "Invalid Brewery ID format. Only numeric values are allowed.");
            } catch (Exception e) {
                return ResponseUtil.respondWithError(
                        response, 500, "Error occurred while processing the request.");
            }
        });
    }
}