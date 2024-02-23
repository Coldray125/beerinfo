package api.extensions.annotation.brewery;

import api.pojo.request.BeerRequestPojo;
import api.pojo.request.BreweryRequestPojo;
import api.test_utils.data_generators.BreweryObjectGenerator;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class RandomBreweryRequestPojoExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        Class<?> testClass = context.getRequiredTestClass();
        Object testInstance = context.getRequiredTestInstance();
        injectFields(testClass, testInstance, ModifierSupport::isNotStatic);
    }

    private void injectFields(Class<?> testClass, Object testInstance, Predicate<Field> predicate) {
        AnnotationSupport.findAnnotatedFields(testClass, RandomBreweryPojo.class, predicate)
                .forEach(field -> {
                    if (BreweryRequestPojo.class.isAssignableFrom(field.getType())) {
                        try {
                            field.setAccessible(true);
                            field.set(testInstance, BreweryObjectGenerator.generateRandomBreweryPojo());
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(STR."Failed to inject random BreweryRequestPojo into field: \{field.getName()}", ex);
                        }
                    }
                });
    }
}