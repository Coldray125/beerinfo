package org.beerinfo.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.beerinfo.data.entity.BreweryEntity;
import org.beerinfo.data.entity.JoinedBreweryBeerEntity;
import org.beerinfo.db.GenericHibernateQuery;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BreweriesService {
    private final SessionFactory sessionFactory;

    public BreweriesService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<BreweryEntity> getAllBreweries() {
        return GenericHibernateQuery.getAllEntities(sessionFactory, BreweryEntity.class);
    }

    public boolean updateBreweryById(BreweryEntity brewery, long id) {
        brewery.setBreweryId(id);
        return GenericHibernateQuery.updateEntityById(sessionFactory, BreweryEntity.class, id, brewery);
    }

    public Optional<JoinedBreweryBeerEntity> getBreweryBeersById(long id) {
        return GenericHibernateQuery.getEntityByFieldValue(sessionFactory, JoinedBreweryBeerEntity.class, Map.of("breweryId", id));
    }

    /// Returns 0 if brewery table is empty.
    public long getLastBreweryId() {
       return sessionFactory.fromSession(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<BreweryEntity> root = criteriaQuery.from(BreweryEntity.class);

            criteriaQuery.select(criteriaBuilder.max(root.get("breweryId")));

           Long lastBreweryId = session.createQuery(criteriaQuery).uniqueResult();
           return lastBreweryId != null ? lastBreweryId : 0L;
        });
    }
}