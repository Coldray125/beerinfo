package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class WrongEndpointRequestHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode errorJson = objectMapper.createObjectNode();
        errorJson.put("error", "Endpoint not found.");
        errorJson.put("status", 404);

        ArrayNode availableEndpoints = objectMapper.createArrayNode();
        availableEndpoints.add("GET /beers");
        availableEndpoints.add("GET /beer?beerId");
        availableEndpoints.add("GET /breweries");
        availableEndpoints.add("POST /beer");
        availableEndpoints.add("PUT /beer");
        availableEndpoints.add("DELETE /beer?beerId");

        errorJson.set("availableEndpoints", availableEndpoints);

        context.json(errorJson);
    }
}