package org.beerinfo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public final class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T jsonStringToObject(String jsonString, Class<T> clazz) {
        T parsedObject = null;
        try {
            parsedObject = objectMapper.reader().readValue(jsonString, clazz);
        } catch (IOException e) {
            log.error("Error converting JSON to parsedObject: {}", e.getMessage(), e);
        }
        return parsedObject;
    }
}