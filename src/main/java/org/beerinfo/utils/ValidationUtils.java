package org.beerinfo.utils;

import io.javalin.http.Context;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.*;
import java.util.function.Function;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();

    public static <T> List<String> validateDTO(T dto) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(dto);

        if (violations.isEmpty()) {
            return List.of();
        }

        return VALIDATOR.validate(dto)
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();
    }

    /// Extracts and validates a query parameter from the request context.
    /// <p>
    /// If the parameter is missing or blank, a 400 JSON error response is sent.
    /// If conversion fails, a 400 JSON error with the given message is returned.
    ///
    /// @param ctx          the current Javalin {@link io.javalin.http.Context}
    /// @param name         the query parameter name to extract
    /// @param converter    function used to convert the raw string value (e.g. {@code Long::parseLong})
    /// @param errorMessage message returned in case of conversion failure
    /// @param <T>          the expected type of the query parameter
    /// @return the converted value, or {@code null} if validation fails (response already sent)
    public static <T> T extractAndValidateQueryParam(Context ctx,
                                                     String name,
                                                     Function<String, T> converter,
                                                     String errorMessage) {
        String raw = ctx.queryParam(name);

        if (raw == null || raw.isBlank()) {
            ctx.status(400).json(Map.of("error", "Missing '" + name + "' query parameter"));
            return null;
        }

        try {
            return converter.apply(raw);
        } catch (Exception e) {
            ResponseUtil.respondWithError(ctx, 400, errorMessage);
            return null;
        }
    }
}
