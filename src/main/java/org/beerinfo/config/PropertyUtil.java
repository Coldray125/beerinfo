package org.beerinfo.config;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public final class PropertyUtil {

    private static final String stand = "dev";
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(PropertyUtil.class.getResourceAsStream("/properties/dev.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String propertyName) {
        try {
            if (properties == null) {
                properties = new Properties();
                var fileInputStream = new FileInputStream("src/main/resources/properties/" + stand + ".properties");
                properties.load(fileInputStream);
            }

            String propertyValue = properties.getProperty(propertyName);

            if (propertyValue == null) {
                log.warn("Warning: Property '{}' is not found in the properties file.", propertyName);
                return "Default_Value";
            }
            return propertyValue;
        } catch (IOException ex) {
            log.error("Error loading properties file: {}", ex.getMessage());
            return "Error_Value";
        }
    }
}