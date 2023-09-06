package org.beerinfo.utils;

import spark.Response;

public class ResponseUtil {
    public static String respondWithError(Response response, int statusCode, String errorMessage) {
        response.status(statusCode);
        response.type("application/json");
        return String.format("{\"error\": \"%s\"}", errorMessage);
    }

    public static void setJsonResponseCode(Response response, int statusCode) {
        response.status(statusCode);
        response.type("application/json");
    }
}