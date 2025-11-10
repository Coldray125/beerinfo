package org.beerinfo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtils {

    private JsonUtils() {
    }

    private static final ObjectMapper MAPPER = new JsonMapper();

    public static <T> T jsonStringToObject(String jsonString, Class<T> clazz) throws JsonProcessingException {
        return MAPPER.readValue(jsonString, clazz);
    }
}