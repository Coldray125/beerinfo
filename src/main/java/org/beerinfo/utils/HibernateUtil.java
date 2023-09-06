package org.beerinfo.utils;

import lombok.Getter;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.entity.BreweryEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import static org.hibernate.cfg.AvailableSettings.*;

@Getter
public class HibernateUtil {

    public SessionFactory buildSessionFactory() {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(createConfiguration().getProperties())
                .build();

        Metadata metadata = addAnnotatedClasses(serviceRegistry);

        SessionFactoryBuilder sessionFactoryBuilder = metadata.getSessionFactoryBuilder();

        return sessionFactoryBuilder.build();
    }

    private Metadata addAnnotatedClasses(StandardServiceRegistry serviceRegistry) {
        return new MetadataSources(serviceRegistry)
                .addAnnotatedClass(BeerEntity.class)
                .addAnnotatedClass(BreweryEntity.class)
                .getMetadataBuilder()
                .build();
    }

    private Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setProperty(URL, "jdbc:postgresql://192.168.1.17:5432/mydatabase");
        configuration.setProperty(USER, "postgres");
        configuration.setProperty(PASS, "password");
        configuration.setProperty(DRIVER, "org.postgresql.Driver");
        configuration.setProperty(DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty(SHOW_SQL, "true");
        configuration.setProperty(FORMAT_SQL, "true");
        configuration.setProperty(HIGHLIGHT_SQL, "true");
        return configuration;
    }
}