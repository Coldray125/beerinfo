package api.extensions.annotation.beer;

import api.pojo.request.BeerRequestPojo;
import api.test_utils.data_generators.BeerObjectGenerator;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class RandomBeerRequestPojoExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        Class<?> testClass = context.getRequiredTestClass();
        Object testInstance = context.getRequiredTestInstance();
        injectFields(testClass, testInstance, ModifierSupport::isNotStatic);
    }

    private void injectFields(Class<?> testClass, Object testInstance, Predicate<Field> predicate) {
        AnnotationSupport.findAnnotatedFields(testClass, RandomBeerPojo.class, predicate)
                .forEach(field -> {
                    if (BeerRequestPojo.class.isAssignableFrom(field.getType())) {
                        try {
                            field.setAccessible(true);
                            field.set(testInstance, BeerObjectGenerator.generateRandomBeerPojo());
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(STR."Failed to inject random BeerRequestPojo into field: \{field.getName()}", ex);
                        }
                    }
                });
    }
}