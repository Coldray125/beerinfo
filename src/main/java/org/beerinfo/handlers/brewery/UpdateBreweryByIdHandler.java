package org.beerinfo.handlers.brewery;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.dto.data.BreweryCreationDTO;
import org.beerinfo.entity.BreweryEntity;
import org.beerinfo.mapper.BreweryMapper;
import org.beerinfo.service.BreweriesService;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static org.beerinfo.enums.SupportedCountry.isValidCountry;
import static org.beerinfo.utils.ResponseUtil.respondWithError;
import static org.beerinfo.utils.ResponseUtil.respondWithInternalServerError;

public class UpdateBreweryByIdHandler implements Handler {
    private final BreweriesService breweriesService;
    private final ObjectMapper objectMapper;

    public UpdateBreweryByIdHandler(BreweriesService breweriesService) {
        this.breweriesService = breweriesService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(@NotNull Context context) {
        String breweryId = context.queryParam("breweryId");

        try {
            BreweryCreationDTO breweryCreationDTO = objectMapper.readValue(context.body(), BreweryCreationDTO.class);

            String validationError = ValidationUtils.validateDTO(objectMapper, breweryCreationDTO);
            if (validationError != null) {
                respondWithError(context, 400, validationError);
            }

            String country = breweryCreationDTO.getCountry();
            if (!isValidCountry(country)) {
                respondWithError(context, 400, STR."Country \{country} is not supported. Please provide a valid country");
            }

            final BreweryEntity brewery = BreweryMapper.MAPPER.mapToBreweryEntity(breweryCreationDTO);

            long id = Long.parseLong(breweryId);
            boolean updateResult = breweriesService.updateBreweryById(brewery, id);

            if (updateResult) {
                context.status(200);
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("brewery", breweryCreationDTO);
                responseData.put("message", STR."Brewery with id: \{breweryId} was updated.");
                context.json(responseData);
            } else {
                respondWithError(context, 404, STR."Brewery with id: \{breweryId} not found");
            }
        } catch (NumberFormatException e) {
            respondWithError(context, 400, "Invalid Brewery ID format. Only numeric values are allowed");
        } catch (Exception e) {
            respondWithInternalServerError(context);
        }
    }
}