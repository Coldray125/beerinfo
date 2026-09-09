package api.extensions.annotation.brewery;

import api.test_data.request.BreweryRequest;
import api.test_utils.data_generators.BreweryObjectGenerator;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class RandomBreweryRequestDataExtension implements BeforeEachCallback {

    private final Predicate<Field> predicate = field ->
            ModifierSupport.isNotStatic(field) && field.getType().isAssignableFrom(BreweryRequest.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        Class<?> testClass = context.getRequiredTestClass();
        Object testInstance = context.getRequiredTestInstance();
        injectFields(testClass, testInstance, predicate);
    }

    private void injectFields(Class<?> testClass, Object testInstance, Predicate<Field> predicate) {
        AnnotationSupport.findAnnotatedFields(testClass, RandomBreweryData.class, predicate)
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        field.set(testInstance, BreweryObjectGenerator.generateRandomBreweryRequest());
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException("Failed to inject random BreweryRequestPojo into field: " + field.getName(), ex);
                    }
                });
    }
}