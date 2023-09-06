package api.beer_service;

import api.db_query.BeerQuery;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.UpdateBeerResponse;
import api.request.BeerRequest;
import api.test_utils.data_generators.BeerObjectGenerator;
import io.qameta.allure.Story;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;

@Story("Beer API")
public class UpdateBeerPositiveTest {

    BeerRequest beerRequest = new BeerRequest();
    BeerRequestPojo beerRequestPojo;
    BeerEntity beerEntity;
    HibernateUtil hibernateUtil = new HibernateUtil();
    SessionFactory sessionFactory = hibernateUtil.buildSessionFactory();
    BeerQuery beerQuery = new BeerQuery(sessionFactory);

    long beerId;

    @BeforeEach
    void createBeerEntityInDB() {
        beerRequestPojo = BeerObjectGenerator.generateRandomBeerPojo();
        beerEntity = BeerObjectGenerator.generateRandomBeerEntity();
        beerId = beerQuery.addBeerReturnId(beerEntity);
    }

    @DisplayName("Verify Response Text for PUT /beer/{id}")
    @Test
    void checkUpdateBeerResponseText() {
        UpdateBeerResponse fullResponse = beerRequest.updateBeerRequest(beerRequestPojo, String.valueOf(beerId));
        String expectedText = String.format("Beer with id: %s was updated.", beerId);
        String responseText = fullResponse.getMessage();
        Assertions.assertEquals(expectedText, responseText);
    }

    @DisplayName("Verify Data in PUT /beer/{id} Response and Request")
    @Test
    void checkValuesAddBeerResponse() {
        UpdateBeerResponse fullResponse = beerRequest.updateBeerRequest(beerRequestPojo, String.valueOf(beerId));
        UpdateBeerResponse.BeerDetails responseObject = fullResponse.getBeer();
        Assertions.assertAll(
                () -> Assertions.assertEquals(beerRequestPojo.getAbv(), responseObject.getAbv()),
                () -> Assertions.assertEquals(beerRequestPojo.getName(), responseObject.getName()),
                () -> Assertions.assertEquals(beerRequestPojo.getIbuNumber(), responseObject.getIbuNumber()),
                () -> Assertions.assertEquals(beerRequestPojo.getStyle(), responseObject.getStyle()),
                () -> Assertions.assertEquals(beerRequestPojo.getBreweryId(), responseObject.getBreweryId()),
                () -> Assertions.assertEquals(beerRequestPojo.getOunces(), responseObject.getOunces()));
    }

    @DisplayName("Verify Data in PUT /beer/{id} Response and Database")
    @Test
    void checkAddBeerWriteInDatabase() {
        UpdateBeerResponse fullResponse = beerRequest.updateBeerRequest(beerRequestPojo, String.valueOf(beerId));
        UpdateBeerResponse.BeerDetails responseObject = fullResponse.getBeer();
        GetBeerResponseDTO beerEntity = beerQuery.getBeerById(beerId);

        step("Validate response JSON against database values");
        Assertions.assertAll(
                () -> Assertions.assertEquals(responseObject.getAbv(), beerEntity.getAbv()),
                () -> Assertions.assertEquals(responseObject.getName(), beerEntity.getName()),
                () -> Assertions.assertEquals(responseObject.getIbuNumber(), beerEntity.getIbuNumber()),
                () -> Assertions.assertEquals(responseObject.getStyle(), beerEntity.getStyle()),
                () -> Assertions.assertEquals(responseObject.getBreweryId(), beerEntity.getBreweryId()),
                () -> Assertions.assertEquals(responseObject.getOunces(), beerEntity.getOunces())
        );
    }
}