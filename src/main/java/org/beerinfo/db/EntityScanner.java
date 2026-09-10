package org.beerinfo.db;

import io.github.classgraph.ClassGraph;
import jakarta.persistence.Entity;

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
                .enableAnnotationInfo()
                .acceptPackages(packageName)
                .scan()) {

            var entityClasses = scanResult
                    .getClassesWithAnnotation(Entity.class.getName())
                    .loadClasses();

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