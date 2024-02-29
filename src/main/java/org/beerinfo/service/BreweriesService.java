package org.beerinfo.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.beerinfo.entity.BreweryEntity;
import org.beerinfo.entity.JoinedBreweryBeerEntity;
import org.beerinfo.utils.HibernateQueryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BreweriesService {
    private final SessionFactory sessionFactory;

    public BreweriesService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<List<BreweryEntity>> getAllBreweries() {
        return HibernateQueryUtil.getAllEntities(sessionFactory, BreweryEntity.class);
    }

    public boolean updateBreweryById(BreweryEntity brewery, long id) {
        brewery.setBreweryId(id);
        return HibernateQueryUtil.updateEntityById(sessionFactory, BreweryEntity.class, id, brewery);
    }

    public Optional<JoinedBreweryBeerEntity> getBreweryBeersById(long id) {
        return HibernateQueryUtil.getEntityByFieldValue(sessionFactory, JoinedBreweryBeerEntity.class, Map.of("breweryId", id));
    }

    public long getLastBreweryId() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<BreweryEntity> root = criteriaQuery.from(BreweryEntity.class);

            criteriaQuery.select(criteriaBuilder.max(root.get("breweryId")));

            Long lastBreweryId = session.createQuery(criteriaQuery).uniqueResult();
            return lastBreweryId != null ? lastBreweryId : 0L; // Return 0 if the table is empty.
        } catch (Exception e) {
            e.printStackTrace();
            return 0L; // Return 0 in case of any errors.
        }
    }
}