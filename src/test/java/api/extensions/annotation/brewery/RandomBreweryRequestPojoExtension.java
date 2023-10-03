package api.extensions.annotation.brewery;

import api.pojo.request.BreweryRequestPojo;
import api.test_utils.data_generators.BreweryObjectGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

class RandomBreweryRequestPojoExtension implements ParameterResolver {

    private final BreweryRequestPojo random = BreweryObjectGenerator.generateRandomBreweryPojo();

    @Override
    public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) {
        return pc.isAnnotated(RandomBreweryPojo.class);
    }

    @Override
    public BreweryRequestPojo resolveParameter(ParameterContext pc, ExtensionContext ec) {
        return random;
    }
}