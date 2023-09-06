package org.beerinfo.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class HibernateQueryUtil {

    public static <T> Optional<List<T>> getAllEntities(SessionFactory sessionFactory, Class<T> entityClass) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<T> root = criteriaQuery.from(entityClass);
            criteriaQuery.select(root);

            Query<T> query = session.createQuery(criteriaQuery);
            List<T> result = query.getResultList();

            return Optional.of(result);
        }
    }

    public static <T> Optional<T> addEntityReturnEntity(SessionFactory sessionFactory, T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();

            return Optional.of(entity);
        } catch (Exception e) {
            return Optional.empty();
        }
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

    public static <T, V> Optional<T> getEntityByFieldValue(SessionFactory sessionFactory, Class<T> entityClass, String fieldName, V value) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<T> root = criteriaQuery.from(entityClass);

            criteriaQuery.select(root);
            criteriaQuery.where(criteriaBuilder.equal(root.get(fieldName), value));

            return session.createQuery(criteriaQuery).uniqueResultOptional();
        }
    }
}
