package org.beerinfo.db;

import org.beerinfo.data.entity.BeerEntity;
import org.beerinfo.data.entity.BreweryEntity;
import org.beerinfo.data.entity.JoinedBeerBreweryEntity;
import org.beerinfo.data.entity.JoinedBreweryBeerEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.HikariCPSettings;

import static org.beerinfo.config.PropertyUtil.getProperty;
import static org.hibernate.cfg.AvailableSettings.*;

public class PostgresSessionProvider {

    private PostgresSessionProvider() {
    }

    public static SessionFactory getBeerInfoSessionFactory() {
        return PostgresSessionProvider.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        static final SessionFactory INSTANCE = buildSessionFactory();
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
        configuration.setProperty(HikariCPSettings.HIKARI_ACQUISITION_TIMEOUT, "250");
        configuration.setProperty(HikariCPSettings.HIKARI_MAX_SIZE, "10");
        configuration.setProperty(HikariCPSettings.HIKARI_POOL_NAME, "HikariPool-BeerInfoDB");

        return configuration;
    }
}