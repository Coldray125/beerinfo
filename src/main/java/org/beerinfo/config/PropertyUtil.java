package org.beerinfo.config;

import lombok.extern.slf4j.Slf4j;
import org.beerinfo.MyClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
public final class PropertyUtil {

    private static final String stand = "dev";
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(PropertyUtil.class.getResourceAsStream("/properties/dev.properties"));
            System.out.println(properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String propertyName) {
        try {
            if (properties == null) {
                properties = new Properties();
                var fileInputStream = new FileInputStream(STR."src/main/resources/properties/\{stand}.properties");
                properties.load(fileInputStream);
            }

            String propertyValue = properties.getProperty(propertyName);

            if (propertyValue == null) {
                log.warn(STR."Warning: Property '\{propertyName}' is not found in the properties file.");
                return "Default_Value";
            }
            return propertyValue;
        } catch (IOException ex) {
            log.error(STR."Error loading properties file: \{ex.getMessage()}");
            return "Error_Value";
        }
    }
}