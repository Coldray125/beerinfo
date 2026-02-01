package org.beerinfo.db;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.CriteriaDefinition;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaRoot;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GenericHibernateQuery {
    public static <Type> Optional<List<Type>> getAllEntities(SessionFactory sessionFactory, Class<Type> entityClass) {
        var criteria = new CriteriaDefinition<>(sessionFactory, entityClass) {
            {
                JpaRoot<Type> root = from(entityClass);
                select(root);
            }
        };
        List<Type> result = sessionFactory
                .fromTransaction(session -> session.createSelectionQuery(criteria).getResultList());

        return Optional.of(result);
    }

    public static <Type> Optional<Type> getEntityByFieldValue(SessionFactory sessionFactory, Class<Type> entityClass,
                                                              Map<String, Object> criteriaParameters) {
        var criteria = new CriteriaDefinition<>(sessionFactory, entityClass) {
            {
                JpaRoot<Type> root = from(entityClass);
                select(root);
                where(createPredicates(getCriteriaBuilder(), root, criteriaParameters));
            }
        };
        return sessionFactory
                .fromTransaction(session -> session.createSelectionQuery(criteria).uniqueResultOptional());
    }

    public static <Type> Optional<List<Type>> getAllEntityByFieldValue(SessionFactory sessionFactory, Class<Type> entityClass,
                                                                       Map<String, Object> criteriaParameters) {
        return sessionFactory.fromSession(session -> {
            CriteriaQuery<Type> criteriaQuery = createCriteriaQueryWithPredicate(session, entityClass, criteriaParameters);
            List<Type> result = session.createSelectionQuery(criteriaQuery).getResultList();

            return Optional.ofNullable(result);
        });
    }

    public static <Type> Optional<Type> addEntityReturnEntity(SessionFactory sessionFactory, Type entity) {
        return sessionFactory.fromTransaction(session ->
        {
            session.persist(entity);
            return Optional.of(entity);
        });
    }

    //todo .persist (as update) instead of .merge ?
    public static <Type> boolean updateEntityById(SessionFactory sessionFactory, Class<Type> entityClass, long id, Type updatedEntity) {
        return sessionFactory.fromTransaction(session ->
        {
            Type entity = session.find(entityClass, id);
            if (entity != null) {
                session.merge(updatedEntity);
                return true;
            }
            return false;
        });
    }

    public static <Type> boolean deleteEntityById(SessionFactory sessionFactory, Class<Type> entityClass, long id) {
        return sessionFactory.fromTransaction(session ->
        {
            Type entity = session.find(entityClass, id);
            if (entity != null) {
                session.remove(entity);
                return true;
            }
            return false;
        });
    }

    private static <Type> CriteriaQuery<Type> createCriteriaQueryWithPredicate(Session session, Class<Type> entityClass,
                                                                               Map<String, Object> criteriaParameters) {
        HibernateCriteriaBuilder criteria = session.getCriteriaBuilder();
        CriteriaQuery<Type> criteriaQuery = criteria.createQuery(entityClass);

        Root<Type> root = criteriaQuery.from(entityClass);

        Predicate predicate = createPredicates(criteria, root, criteriaParameters);

        criteriaQuery.where(predicate);

        return criteriaQuery;
    }

    private static <Type> Predicate createPredicates(CriteriaBuilder criteria, Root<Type> root, Map<String, Object> parameters) {
        Predicate[] predicates = parameters.entrySet().stream()
                .map(entry -> criteria.equal(root.get(entry.getKey()), entry.getValue()))
                .toArray(Predicate[]::new);

        return criteria.and(predicates);
    }
}