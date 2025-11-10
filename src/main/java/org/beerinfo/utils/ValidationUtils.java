package org.beerinfo.utils;

import io.javalin.http.Context;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.*;
import java.util.function.Function;

public class ValidationUtils {

    public static <T> Map<String, List<String>> validateDTO(T dto) {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<T> violation : violations) {
                errorMessages.add(violation.getMessage());
            }

            Map<String, List<String>> errors = new HashMap<>();
            errors.put("error", errorMessages);
            return errors;
        }
        return null;
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
    public static <T> T extractAndValidateQueryParam(Context ctx, String name, Function<String, T> converter, String errorMessage) {
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
