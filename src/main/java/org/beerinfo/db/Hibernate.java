package org.beerinfo.db;

import lombok.Getter;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.entity.BreweryEntity;
import org.beerinfo.entity.JoinedBeerBreweryEntity;
import org.beerinfo.entity.JoinedBreweryBeerEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import static org.hibernate.cfg.AvailableSettings.*;

@Getter
public class Hibernate {

    private static SessionFactory sessionFactory;

    private Hibernate() {
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
        configuration.setProperty(JAKARTA_JDBC_URL, "jdbc:postgresql://192.168.1.10:5432/mydatabase");
        configuration.setProperty(JAKARTA_JDBC_USER, "postgres");
        configuration.setProperty(JAKARTA_JDBC_PASSWORD, "password");
        configuration.setProperty(JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
        configuration.setProperty(JDBC_TIME_ZONE, "UTC");
        configuration.setProperty(SHOW_SQL, "true");
        configuration.setProperty(FORMAT_SQL, "true");
        configuration.setProperty(HIGHLIGHT_SQL, "true");
        return configuration;
    }
}