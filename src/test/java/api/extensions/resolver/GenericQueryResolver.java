package api.extensions.resolver;

import api.db_query.BeerQuery;
import api.db_query.BreweryQuery;
import org.beerinfo.db.PostgresSessionProvider;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GenericQueryResolver implements ParameterResolver {

    private final Map<Class<?>, Supplier<?>> supportedQueryClasses = new HashMap<>();

    private GenericQueryResolver() {
        registerQueryClasses();
    }

    private void registerQueryClasses() {
        SessionFactory sessionFactory = PostgresSessionProvider.getBeerInfoSessionFactory();
        registerQuery(BeerQuery.class, ()-> new BeerQuery(sessionFactory));
        registerQuery(BreweryQuery.class, ()-> new BreweryQuery(sessionFactory));
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