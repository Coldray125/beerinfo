package org.beerinfo.handlers.beer;

import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.HttpResponseException;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class WrongEndpointHandler implements ExceptionHandler<HttpResponseException> {

    @Override
    public void handle(@NotNull HttpResponseException e, @NotNull Context context) {
        String baseUrl = context.scheme() + "://" + context.host();
        var errorMessage = new LinkedHashMap<String, Object>();
        errorMessage.put("error", "Endpoint not found. Check documentation for available endpoints");
        errorMessage.put("status", 404);
        errorMessage.put("documentation", Map.of(
                "swagger", baseUrl + "/swagger",
                "redoc", baseUrl + "/redoc"
        ));

        context.status(404).json(errorMessage);
    }
}