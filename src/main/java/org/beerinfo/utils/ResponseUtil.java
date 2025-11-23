package org.beerinfo.utils;

import io.javalin.http.Context;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseUtil {

    public static void respondWithError(Context ctx, int statusCode, String errorMessage) {
        ctx.status(statusCode);
        ctx.json(Map.of("error", errorMessage));
    }

    public static void respondWithInternalServerError(Context ctx) {
        var errorResponse = new LinkedHashMap<String, Object>();
        errorResponse.put("error", "Internal server error");
        errorResponse.put("status", 500);
        errorResponse.put("message", "An unexpected error occurred. Please try again later.");

        ctx.status(500).json(errorResponse);
    }
}