package org.beerinfo.service;

import org.beerinfo.data.entity.BeerEntity;
import org.beerinfo.data.entity.JoinedBeerBreweryEntity;
import org.beerinfo.db.GenericHibernateQuery;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BeerService {
    private final SessionFactory sessionFactory;

    public BeerService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<List<BeerEntity>> getAllBeers() {
        return GenericHibernateQuery.getAllEntities(sessionFactory, BeerEntity.class);
    }

    public Optional<JoinedBeerBreweryEntity> getBeerBreweryById(long id) {
        return GenericHibernateQuery.getEntityByFieldValue(sessionFactory, JoinedBeerBreweryEntity.class, Map.of("beerId", id));
    }

    public Optional<BeerEntity> addBeer(BeerEntity beer) {
        return GenericHibernateQuery.addEntityReturnEntity(sessionFactory, beer);
    }

    public boolean updateBeerById(BeerEntity beer, long id) {
        beer.setBeerId(id);
        return GenericHibernateQuery.updateEntityById(sessionFactory, BeerEntity.class, id, beer);
    }

    public Optional<BeerEntity> getBeerById(long id) {
        return GenericHibernateQuery.getEntityByFieldValue(sessionFactory, BeerEntity.class, Map.of("beerId", id));
    }

    public boolean deleteBeerById(long id) {
        return GenericHibernateQuery.deleteEntityById(sessionFactory, BeerEntity.class, id);
    }
}