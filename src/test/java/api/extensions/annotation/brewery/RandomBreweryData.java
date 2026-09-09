package api.extensions.annotation.brewery;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Annotation for random test data generation for non-static BreweryRequestData fields.
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(RandomBreweryRequestDataExtension.class)
public @interface RandomBreweryData {
}