package api.db_query;

import api.test_utils.data_generators.BreweryObjectGenerator;
import io.qameta.allure.Step;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.beerinfo.data.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.data.entity.BreweryEntity;
import org.beerinfo.mapper.BreweryMapper;
import org.beerinfo.utils.HibernateQueryUtil;
import org.hibernate.SessionFactory;

import java.util.Map;
import java.util.Optional;

public class BreweryQuery {
    private final SessionFactory sessionFactory;

    public BreweryQuery(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Step("Get Brewery from Postgres with ID: {id}")
    public GetBreweryResponseDTO getBreweryById(long id) {
        Optional<BreweryEntity> breweryEntity = HibernateQueryUtil.getEntityByFieldValue(
                sessionFactory, BreweryEntity.class, Map.of("breweryId", id));
        if (breweryEntity.isPresent()) {
            return BreweryMapper.MAPPER.mapToGetBreweryResponseDTO(breweryEntity.get());
        }
        throw new EntityNotFoundException("BeerEntity not found for ID: " + id);
    }

    @Step("Add Brewery record to Postgres")
    public long addRandomBreweryReturnId() {
        var breweryEntity = BreweryObjectGenerator.generateRandomBreweryEntity();
        Optional<BreweryEntity> beerEntity = HibernateQueryUtil.addEntityReturnEntity(sessionFactory, breweryEntity);
        if (beerEntity.isPresent()) {
            return beerEntity.get().getBreweryId();
        }
        throw new PersistenceException("Failed to add BeerEntity: \n" + breweryEntity);
    }
}