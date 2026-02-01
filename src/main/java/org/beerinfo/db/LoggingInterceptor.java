package org.beerinfo.db;

import org.hibernate.Interceptor;
import org.hibernate.type.Type;

public class LoggingInterceptor implements Interceptor {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean onLoad(
            Object entity,
            Object id,
            Object[] state,
            String[] propertyNames,
            Type[] types
    ) {
        createLog("Load", entity, id, state, propertyNames, types);
        return Interceptor.super.onLoad(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onPersist(
            Object entity,
            Object id,
            Object[] state,
            String[] propertyNames,
            Type[] types
    ) {
        createLog("Persist", entity, id, state, propertyNames, types);
        return Interceptor.super.onPersist(entity, id, state, propertyNames, types);
    }

    private void createLog(
            String action,
            Object entity,
            Object id,
            Object[] state,
            String[] propertyNames,
            Type[] types
    ) {
        StringBuilder sb = new StringBuilder("\n");

        for (int i = 0; i < propertyNames.length; i++) {
            sb.append("  ")
                    .append(propertyNames[i])
                    .append(" = ")
                    .append(state[i])
                    .append(" (")
                    .append(types[i])
                    .append(")\n");
        }

        log.debug(
                "Entity on {}: {} id:{}{}",
                action,
                entity.getClass().getSimpleName(),
                id,
                sb
        );
    }
}