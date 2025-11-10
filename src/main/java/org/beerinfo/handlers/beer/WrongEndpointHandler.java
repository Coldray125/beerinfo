package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.HttpResponseException;
import org.jetbrains.annotations.NotNull;

public class WrongEndpointHandler implements ExceptionHandler <HttpResponseException> {

    private static final ObjectMapper MAPPER = new JsonMapper();

    @Override
    public void handle(@NotNull HttpResponseException e, @NotNull Context context) {
        ObjectNode errorJson = MAPPER.createObjectNode();
        errorJson.put("error", "Endpoint not found.");
        errorJson.put("status", 404);

        ArrayNode availableEndpoints = MAPPER.createArrayNode();
        availableEndpoints.add("GET /beers");
        availableEndpoints.add("GET /beer-brewery?beerId");
        availableEndpoints.add("GET /beer?beerId");
        availableEndpoints.add("GET /breweries");
        availableEndpoints.add("GET /brewery-beers?breweryId");
        availableEndpoints.add("POST /beer");
        availableEndpoints.add("PUT /beer");
        availableEndpoints.add("DELETE /beer?beerId");

        errorJson.set("availableEndpoints", availableEndpoints);

        context.status(404).json(errorJson);
    }
}