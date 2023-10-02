package api.extensions.annotation.beer;

import api.pojo.request.BeerRequestPojo;
import api.test_utils.data_generators.BeerObjectGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

class RandomBeerRequestPojoExtension implements ParameterResolver {

    private final BeerRequestPojo random = BeerObjectGenerator.generateRandomBeerPojo();

    @Override
    public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) {
        return pc.isAnnotated(RandomBeerPojo.class);
    }

    @Override
    public BeerRequestPojo resolveParameter(ParameterContext pc, ExtensionContext ec) {
        return random;
    }
}