package org.beerinfo.db;

import lombok.Getter;
import org.beerinfo.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import static org.beerinfo.config.PropertyUtil.getProperty;
import static org.hibernate.cfg.AvailableSettings.*;

@Getter
public class PostgresSessionProvider {

    private static SessionFactory sessionFactory;

    private PostgresSessionProvider() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }

        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(createConfiguration().getProperties())
                .build();

        Metadata metadata = addAnnotatedClasses(serviceRegistry);

        SessionFactoryBuilder sessionFactoryBuilder = metadata.getSessionFactoryBuilder();

        return sessionFactoryBuilder.build();
    }

    private static Metadata addAnnotatedClasses(StandardServiceRegistry serviceRegistry) {
        return new MetadataSources(serviceRegistry)
                .addAnnotatedClass(BeerEntity.class)
                .addAnnotatedClass(BreweryEntity.class)
                .addAnnotatedClass(JoinedBeerBreweryEntity.class)
                .addAnnotatedClass(JoinedBreweryBeerEntity.class)
                .getMetadataBuilder()
                .build();
    }

    private static Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setProperty(JAKARTA_JDBC_URL, getProperty("postgresql.dev.uri"));
        configuration.setProperty(JAKARTA_JDBC_USER, getProperty("postgresql.dev.login"));
        configuration.setProperty(JAKARTA_JDBC_PASSWORD, getProperty("postgresql.dev.password"));
        configuration.setProperty(JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
        configuration.setProperty(JDBC_TIME_ZONE, "UTC");
        configuration.setProperty(SHOW_SQL, "true");
        configuration.setProperty(FORMAT_SQL, "true");
        configuration.setProperty(HIGHLIGHT_SQL, "true");
        configuration.setProperty("hibernate.hikari.connectionTimeout", "250");
        configuration.setProperty("hibernate.hikari.maximumPoolSize", "10");

        return configuration;
    }
}