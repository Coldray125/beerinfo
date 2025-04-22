package api.extensions.resolver;

import api.request.BeerRequest;
import api.request.BreweryRequest;
import org.beerinfo.db.PostgresSessionProvider;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GenericHttpRequestResolver implements ParameterResolver {

    private final Map<Class<?>, Supplier<?>> supportedQueryClasses = new HashMap<>();

    private GenericHttpRequestResolver() {
        registerQueryClasses();
    }

    private void registerQueryClasses() {
        SessionFactory sessionFactory = PostgresSessionProvider.getBeerInfoSessionFactory();
        registerQuery(BeerRequest.class, BeerRequest::new);
        registerQuery(BreweryRequest.class, BreweryRequest::new);
    }

    private <T> void registerQuery(Class<T> clazz, Supplier<T> supplier) {
        supportedQueryClasses.put(clazz, supplier);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return supportedQueryClasses.containsKey(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return supportedQueryClasses.get(parameterContext.getParameter().getType()).get();
    }
}