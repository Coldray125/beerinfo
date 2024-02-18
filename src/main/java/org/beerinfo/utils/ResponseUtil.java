package org.beerinfo.utils;

import io.javalin.http.Context;

import java.util.Map;

public class ResponseUtil {

    public static void respondWithError(Context ctx, int statusCode, String errorMessage) {
        ctx.status(statusCode);
        ctx.json(Map.of("error", errorMessage));
    }

    public static void respondWithInternalServerError(Context ctx) {
        ctx.status(500);
        ctx.json(Map.of("error", "Error occurred while processing the request"));
    }
}