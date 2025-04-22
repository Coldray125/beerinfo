package api.db_query;

import api.test_utils.data_generators.BeerObjectGenerator;
import io.qameta.allure.Step;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.data.entity.BeerEntity;
import org.beerinfo.mapper.BeerMapper;
import org.beerinfo.utils.HibernateQueryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Map;
import java.util.Optional;

public class BeerQuery {
    private final SessionFactory sessionFactory;

    public BeerQuery(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Step("Get Beer from Postgres with ID: {id}")
    public GetBeerResponseDTO getBeerById(long id) {
        Optional<BeerEntity> beerEntity = HibernateQueryUtil.getEntityByFieldValue(sessionFactory, BeerEntity.class, Map.of("beerId", id));
        if (beerEntity.isPresent()) {
            return BeerMapper.MAPPER.mapToGetBeerResponseDTO(beerEntity.get());
        }
        throw new EntityNotFoundException("BeerEntity not found for ID: " + id);
    }

    @Step("Add Beer record to Postgres")
    public GetBeerResponseDTO addRandomBeerReturnDTO() {
        BeerEntity randomEntity = BeerObjectGenerator.generateRandomBeerEntity();
        Optional<BeerEntity> beerEntity = HibernateQueryUtil.addEntityReturnEntity(sessionFactory, randomEntity);
        if (beerEntity.isPresent()) {
            return BeerMapper.MAPPER.mapToGetBeerResponseDTO(beerEntity.get());
        }
        throw new PersistenceException("Failed to add BeerEntity: \n" + randomEntity);
    }

    @Step("Update Beer record in Postgres with ID: {beer} and Beer: {id}")
    public boolean updateBeerById(BeerEntity beer, long id) {
        beer.setBeerId(id);
        return HibernateQueryUtil.updateEntityById(sessionFactory, BeerEntity.class, id, beer);
    }

    @Step("Delete Beer record in Postgres with ID: {id}")
    public boolean deleteBeerById(long id) {
        return HibernateQueryUtil.deleteEntityById(sessionFactory, BeerEntity.class, id);
    }

    @Step("Find last Beer record Id in Postgres")
    public long getLastBeerId() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<BeerEntity> root = criteriaQuery.from(BeerEntity.class);

            criteriaQuery.select(criteriaBuilder.max(root.get("beerId")));

            Optional<Long> beerId = session.createQuery(criteriaQuery).uniqueResultOptional();
            if (beerId.isPresent()) {
                return beerId.get();
            }
            throw new NoResultException("No beer records found");
        }
    }
}