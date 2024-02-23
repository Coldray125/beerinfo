package org.beerinfo.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.CriteriaDefinition;
import org.hibernate.query.criteria.JpaRoot;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HibernateQueryUtil {
    public static <T> Optional<List<T>> getAllEntities(SessionFactory sessionFactory, Class<T> entityClass) {
        var criteria = new CriteriaDefinition<>(sessionFactory, entityClass) {
            {
                JpaRoot<T> root = from(entityClass);
                select(root);
            }
        };
        List<T> result = sessionFactory
                .fromTransaction(_ -> sessionFactory.openSession().createSelectionQuery(criteria).getResultList());

        return Optional.of(result);
    }

    public static <T> Optional<T> getEntityByFieldValue(SessionFactory sessionFactory, Class<T> entityClass, Map<String, Object> criteriaParameters) {
        var criteria = new CriteriaDefinition<>(sessionFactory, entityClass) {
            {
                JpaRoot<T> root = from(entityClass);
                select(root);
                where(createPredicates(getCriteriaBuilder(), root, criteriaParameters));
            }
        };
        return sessionFactory
                .fromTransaction(_ -> sessionFactory.openSession().createSelectionQuery(criteria).uniqueResultOptional());
    }

    public static <T> Optional<T> addEntityReturnEntity(SessionFactory sessionFactory, T entity) {
        return sessionFactory.fromTransaction(_ ->
        {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return Optional.of(entity);
        });
    }

    public static boolean updateEntityById(SessionFactory sessionFactory, Class<?> entityClass, long id, Object updatedEntity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                Object entity = session.get(entityClass, id);
                if (entity != null) {
                    session.merge(updatedEntity);
                    transaction.commit();
                    return true;
                } else {
                    transaction.rollback();
                    return false;
                }
            } catch (Exception e) {
                transaction.rollback();
                return false;
            }
        }
    }

    public static boolean deleteEntityById(SessionFactory sessionFactory, Class<?> entityClass, long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Object entity = session.get(entityClass, id);

            if (entity != null) {
                session.remove(entity);
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        }
    }

    private static <T> Predicate createPredicates(CriteriaBuilder criteria, Root<T> root, Map<String, Object> parameters) {
        Predicate[] predicates = parameters.entrySet().stream()
                .map(entry -> criteria.equal(root.get(entry.getKey()), entry.getValue()))
                .toArray(Predicate[]::new);

        return criteria.and(predicates);
    }
}