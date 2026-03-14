package api.db_query;

import api.test_utils.data_generators.BeerObjectGenerator;
import io.qameta.allure.Step;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.data.entity.BeerEntity;
import org.beerinfo.db.GenericHibernateQuery;
import org.beerinfo.db.PostgresSessionProvider;
import org.beerinfo.mapper.BeerMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Map;
import java.util.Optional;

public class BeerQuery {

    private static final SessionFactory sessionFactory = PostgresSessionProvider.getBeerInfoSessionFactory();

    @Step("Get Beer from Postgres with ID: {id}")
    public static BeerEntity getBeerById(long id) {
        Optional<BeerEntity> beerEntity = GenericHibernateQuery.getEntityByFieldValue(
                sessionFactory, BeerEntity.class, Map.of("beerId", id));
        if (beerEntity.isPresent()) {
            return beerEntity.get();
        }
        throw new ExpectedEntityNotFoundException("BeerEntity not found for ID: " + id);
    }

    @Step("Add Beer record to Postgres")
    public static GetBeerResponseDTO addRandomBeerReturnDTO() {
        BeerEntity randomEntity = BeerObjectGenerator.generateRandomBeerEntity();
        Optional<BeerEntity> beerEntity = GenericHibernateQuery.addEntityReturnEntity(sessionFactory, randomEntity);
        if (beerEntity.isPresent()) {
            return BeerMapper.MAPPER.mapToGetBeerResponseDTO(beerEntity.get());
        }
        throw new PersistenceException("Failed to add BeerEntity: \n" + randomEntity);
    }

    @Step("Update Beer record in Postgres with ID: {beer} and Beer: {id}")
    public static boolean updateBeerById(BeerEntity beer, long id) {
        beer.setBeerId(id);
        return GenericHibernateQuery.updateEntityById(sessionFactory, BeerEntity.class, id, beer);
    }

    @Step("Delete Beer record in Postgres with ID: {id}")
    public static boolean deleteBeerById(long id) {
        return GenericHibernateQuery.deleteEntityById(sessionFactory, BeerEntity.class, id);
    }

    @Step("Find last Beer record Id in Postgres")
    public static long getLastBeerId() {
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