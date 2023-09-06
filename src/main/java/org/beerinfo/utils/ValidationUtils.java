package org.beerinfo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.*;

public class ValidationUtils {

    public static  <T> String validateDTO(ObjectMapper objectMapper, T dto) throws JsonProcessingException {
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
            return objectMapper.writeValueAsString(errors);
        }

        return null;
    }
}
