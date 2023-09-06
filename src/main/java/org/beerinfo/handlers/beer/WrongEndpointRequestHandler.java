package org.beerinfo.handlers.beer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static spark.Spark.notFound;

public class WrongEndpointRequestHandler {
    public void registerRoute() {
        notFound((req, res) -> {
            res.type("application/json");
            res.status(404);

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode errorJson = objectMapper.createObjectNode();
            errorJson.put("error", "Endpoint not found.");
            errorJson.put("status", 404);

            ArrayNode availableEndpoints = objectMapper.createArrayNode();
            availableEndpoints.add("GET /beers");
            availableEndpoints.add("GET /beer/:id");
            availableEndpoints.add("GET /breweries");
            availableEndpoints.add("POST /beer");
            availableEndpoints.add("PUT /beer");
            availableEndpoints.add("DELETE /beer/:id");

            errorJson.set("availableEndpoints", availableEndpoints);

            return errorJson.toString();
        });
    }
}