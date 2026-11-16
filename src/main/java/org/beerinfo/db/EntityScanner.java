package org.beerinfo.db;

import io.github.classgraph.ClassGraph;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class EntityScanner {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EntityScanner.class);

    private EntityScanner() {
    }

    /// Finds all classes annotated with {@link Entity} in the specified package.
    ///
    /// @param packageName package to scan
    /// @return list of classes annotated with {@link Entity}
    public static List<Class<?>> findAnnotatedEntities(String packageName) {
        try (var scanResult = new ClassGraph()
                .enableClasspath()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(packageName)
                .scan()) {

            List<Class<?>> entityClasses = new ArrayList<>();

            for (var classInfo : scanResult.getClassesWithAnnotation(Entity.class.getName())) {
                try {
                    Class<?> entityClass = Class.forName(
                            classInfo.getName(),
                            false,
                            EntityScanner.class.getClassLoader()
                    );

                    entityClasses.add(entityClass);
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException(
                            "Failed to load entity class: " + classInfo.getName(), e
                    );
                }
            }

            logFoundEntities(packageName, entityClasses);

            return entityClasses;
        }
    }

    private static void logFoundEntities(String packageName, List<Class<?>> entityClasses) {
        log.info("""
                        \nFound {} entity classes in package '{}':
                        =================
                        {}
                        =================
                        """,
                entityClasses.size(),
                packageName,
                entityClasses.stream()
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining("\n"))
        );
    }
}