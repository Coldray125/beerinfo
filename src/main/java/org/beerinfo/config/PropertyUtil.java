package org.beerinfo.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public final class PropertyUtil {

    private static final String DEFAULT_ENV = "local";

    private static final Properties PROPERTIES = loadProperties();

    private PropertyUtil() {
    }

    /// Loads environment-specific properties based on the APP_ENV variable (local or docker).
    /// Default is local, for convenient run from IDE
    private static Properties loadProperties() {
        String environment = System.getenv().getOrDefault("APP_ENV", DEFAULT_ENV);

        String propertiesFile = switch (environment) {
            case "local" -> "/properties/local.properties";
            case "docker" -> "/properties/docker.properties";
            default -> throw new IllegalArgumentException(
                    "Unsupported application environment: " + environment
            );
        };

        Properties properties = new Properties();

        try (InputStream input = PropertyUtil.class.getResourceAsStream(propertiesFile)) {
            if (input == null) {
                throw new IllegalStateException("Properties file not found: " + propertiesFile);
            }

            properties.load(input);
            log.info("Loaded configuration for environment: {}", environment);

            return properties;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties: " + propertiesFile, e);
        }
    }

    public static String getProperty(String propertyName) {
        String propertyValue = PROPERTIES.getProperty(propertyName);

        if (propertyValue == null) {
            throw new IllegalArgumentException(
                    "Property '%s' not found in configuration".formatted(propertyName)
            );
        }

        return propertyValue;
    }
}