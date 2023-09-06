package api.beer_service;

import api.db_query.BeerQuery;
import api.pojo.response.beer.GetBeerResponse;
import api.request.BeerRequest;
import api.test_utils.ResponseValidator;
import api.test_utils.data_generators.BeerObjectGenerator;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static api.test_utils.SchemaPaths.BEER_OBJECT;
import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer API")
public class GetBeerByIdPositiveTest {
    BeerRequest beerRequest = new BeerRequest();
    Long beerId;
    BeerEntity beerEntity;
    HibernateUtil hibernateUtil = new HibernateUtil();
    SessionFactory sessionFactory = hibernateUtil.buildSessionFactory();
    BeerQuery beerQuery = new BeerQuery(sessionFactory);

    @BeforeEach
    void createBeerEntityInDB() {
        beerEntity = BeerObjectGenerator.generateRandomBeerEntity();
        beerId = beerQuery.addBeerReturnId(beerEntity);
    }

    @DisplayName("Ensure Response Code is 200 for GET /beer/{id}")
    @Test
    void checkBeerByIdResponseCode() {
        Response response = beerRequest.getBeerByIdRequestReturnResponse(String.valueOf(beerId));
        Assertions.assertEquals(SC_OK, response.getStatusCode());
    }

    @DisplayName("Validate Response JSON Structure for GET /beer/{id}")
    @Test
    void checkBeerByIdResponseStructure() {
        Response response = beerRequest.getBeerByIdRequestReturnResponse(String.valueOf(beerId));
        ResponseValidator.assertResponseMatchesSchema(response, BEER_OBJECT.getPath());
    }

    @DisplayName("Check Data Matching: GET /beer/{id} Response vs Database")
    @Test
    void checkValuesInBeerByIdResponse() {
        GetBeerResponse responseObject = beerRequest.getBeerByIdRequest(String.valueOf(beerId));
        Assertions.assertAll(
                () -> Assertions.assertEquals(beerEntity.getAbv(), responseObject.getAbv()),
                () -> Assertions.assertEquals(beerEntity.getName(), responseObject.getName()),
                () -> Assertions.assertEquals(beerEntity.getIbuNumber(), responseObject.getIbuNumber()),
                () -> Assertions.assertEquals(beerEntity.getName(), responseObject.getName()),
                () -> Assertions.assertEquals(beerEntity.getStyle(), responseObject.getStyle()),
                () -> Assertions.assertEquals(beerEntity.getBreweryId(), responseObject.getBreweryId()),
                () -> Assertions.assertEquals(beerEntity.getOunces(), responseObject.getOunces())
        );
    }
}