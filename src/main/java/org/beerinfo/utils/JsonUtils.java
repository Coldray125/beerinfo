package org.beerinfo.utils;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
public final class JsonUtils {

    private JsonUtils() {
    }

    private static final ObjectMapper MAPPER = new JsonMapper();

    public static <T> T jsonStringToObject(String jsonString, Class<T> clazz) throws JacksonException {
        return MAPPER.readValue(jsonString, clazz);
    }
}