package api.db_query;

import api.test_utils.data_generators.BreweryObjectGenerator;
import io.qameta.allure.Step;
import jakarta.persistence.PersistenceException;
import org.beerinfo.data.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.data.entity.BreweryEntity;
import org.beerinfo.db.GenericHibernateQuery;
import org.beerinfo.db.PostgresSessionProvider;
import org.beerinfo.mapper.BreweryMapper;
import org.hibernate.SessionFactory;

import java.util.Map;
import java.util.Optional;

public class BreweryQuery {
    private static final SessionFactory sessionFactory = PostgresSessionProvider.getBeerInfoSessionFactory();


    @Step("Get Brewery from Postgres with ID: {id}")
    public static GetBreweryResponseDTO getBreweryById(long id) {
        Optional<BreweryEntity> breweryEntity = GenericHibernateQuery.getEntityByFieldValue(
                sessionFactory, BreweryEntity.class, Map.of("breweryId", id));
        if (breweryEntity.isPresent()) {
            return BreweryMapper.MAPPER.mapToGetBreweryResponseDTO(breweryEntity.get());
        }
        throw new ExpectedEntityNotFoundException("BeerEntity not found for ID: " + id);
    }

    @Step("Add Brewery record to Postgres")
    public static long addRandomBreweryReturnId() {
        var breweryEntity = BreweryObjectGenerator.generateRandomBreweryEntity();
        Optional<BreweryEntity> beerEntity = GenericHibernateQuery.addEntityReturnEntity(sessionFactory, breweryEntity);
        if (beerEntity.isPresent()) {
            return beerEntity.get().getBreweryId();
        }
        throw new PersistenceException("Failed to add BeerEntity: \n" + breweryEntity);
    }
}