package org.beerinfo.service;

import org.beerinfo.entity.BeerEntity;
import org.beerinfo.utils.HibernateQueryUtil;
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
        return HibernateQueryUtil.getAllEntities(sessionFactory, BeerEntity.class);
    }

    public Optional<BeerEntity> addBeer(BeerEntity beer) {
        return HibernateQueryUtil.addEntityReturnEntity(sessionFactory, beer);
    }

    public boolean updateBeerById(BeerEntity beer, long id) {
        beer.setBeerId(id);
        return HibernateQueryUtil.updateEntityById(sessionFactory, BeerEntity.class, id, beer);
    }

    public Optional<BeerEntity> getBeerById(long id) {
        return HibernateQueryUtil.getEntityByFieldValue(sessionFactory, BeerEntity.class, Map.of("beerId", id));
    }

    public boolean deleteBeerById(long id) {
        return HibernateQueryUtil.deleteEntityById(sessionFactory, BeerEntity.class, id);
    }
}