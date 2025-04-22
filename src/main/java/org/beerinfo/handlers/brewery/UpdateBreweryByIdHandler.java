package org.beerinfo.handlers.brewery;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.beerinfo.data.dto.BreweryCreationDTO;
import org.beerinfo.data.entity.BreweryEntity;
import org.beerinfo.mapper.BreweryMapper;
import org.beerinfo.service.BreweriesService;
import org.beerinfo.utils.JsonUtils;
import org.beerinfo.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.beerinfo.enums.SupportedCountry.isValidCountry;
import static org.beerinfo.utils.ResponseUtil.respondWithError;
import static org.beerinfo.utils.ResponseUtil.respondWithInternalServerError;

public class UpdateBreweryByIdHandler implements Handler {
    private final BreweriesService breweriesService;

    public UpdateBreweryByIdHandler(BreweriesService breweriesService) {
        this.breweriesService = breweriesService;
    }

    @Override
    public void handle(@NotNull Context context) {
        String breweryId = context.queryParam("breweryId");

        try {
            BreweryCreationDTO breweryCreationDTO = JsonUtils.jsonStringToObject(context.body(), BreweryCreationDTO.class);

            Map<String, List<String>> validationError = ValidationUtils.validateDTO(breweryCreationDTO);
            if (validationError != null) {
                context.status(400);
                context.json(validationError);
                return;
            }

            String country = breweryCreationDTO.getCountry();
            if (!isValidCountry(country)) {
                respondWithError(context, 400, "Country " + country + " is not supported. Please provide a valid country");
                return;
            }

            final BreweryEntity brewery = BreweryMapper.MAPPER.mapToBreweryEntity(breweryCreationDTO);

            long id = Long.parseLong(breweryId);
            boolean updateResult = breweriesService.updateBreweryById(brewery, id);

            if (updateResult) {
                context.status(200);
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("brewery", breweryCreationDTO);
                responseData.put("message", "Brewery with id: " + breweryId + " was updated.");
                context.json(responseData);
            } else {
                respondWithError(context, 404, "Brewery with id: " + breweryId + " not found");
            }
        } catch (NumberFormatException e) {
            respondWithError(context, 400, "Invalid Brewery ID format. Only numeric values are allowed");
        } catch (Exception e) {
            respondWithInternalServerError(context);
        }
    }
}