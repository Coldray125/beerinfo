package api.beer_service;

import api.db_query.BeerQuery;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.AddBeerResponse;
import api.request.BeerRequest;
import api.test_utils.ResponseValidator;
import api.test_utils.data_generators.BeerObjectGenerator;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static api.test_utils.SchemaPaths.ADD_BEER_RESPONSE;
import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer API")
public class AddBeerPositiveTest {

    BeerRequest beerRequest = new BeerRequest();
    BeerRequestPojo beerRequestPojo;
    HibernateUtil hibernateUtil = new HibernateUtil();
    SessionFactory sessionFactory = hibernateUtil.buildSessionFactory();
    BeerQuery beerQuery = new BeerQuery(sessionFactory);

    @BeforeEach
    void createBeerEntityInDB() {
        beerRequestPojo = BeerObjectGenerator.generateRandomBeerPojo();
    }

    @DisplayName("Verify Data in POST /beer Response and Database")
    @Test
    void checkAddBeerWriteInDatabase() {
        AddBeerResponse fullResponse = beerRequest.addBeerRequest(beerRequestPojo);
        AddBeerResponse.BeerDetails responseObject = fullResponse.getBeer();
        GetBeerResponseDTO beerEntity = beerQuery.getBeerById(responseObject.getBeerId());

        step("Validate response JSON against database values");
        Assertions.assertAll(
                () -> Assertions.assertEquals(responseObject.getBeerId(), beerEntity.getBeerId()),
                () -> Assertions.assertEquals(responseObject.getAbv(), beerEntity.getAbv()),
                () -> Assertions.assertEquals(responseObject.getName(), beerEntity.getName()),
                () -> Assertions.assertEquals(responseObject.getIbuNumber(), beerEntity.getIbuNumber()),
                () -> Assertions.assertEquals(responseObject.getStyle(), beerEntity.getStyle()),
                () -> Assertions.assertEquals(responseObject.getBreweryId(), beerEntity.getBreweryId()),
                () -> Assertions.assertEquals(responseObject.getOunces(), beerEntity.getOunces())
        );
    }

    @DisplayName("Ensure POST /beer Response Message")
    @Test
    void checkAddBeerResponseText() {
        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);

        Assertions.assertEquals(SC_OK, response.getStatusCode());

        String responseText = response.body().path("message");
        Assertions.assertEquals("Beer added successfully.", responseText);
    }

    @DisplayName("Validate POST /beer Response JSON Structure")
    @Test
    void checkAddBeerResponseStructure() {
        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);
        ResponseValidator.assertResponseMatchesSchema(response, ADD_BEER_RESPONSE.getPath());
    }

    @DisplayName("Verify Data in POST /beer Response and Request")
    @Test
    void checkValuesAddBeerResponse() {
        AddBeerResponse fullResponse = beerRequest.addBeerRequest(beerRequestPojo);
        AddBeerResponse.BeerDetails responseObject = fullResponse.getBeer();
        Assertions.assertAll(
                () -> Assertions.assertEquals(beerRequestPojo.getAbv(), responseObject.getAbv()),
                () -> Assertions.assertEquals(beerRequestPojo.getName(), responseObject.getName()),
                () -> Assertions.assertEquals(beerRequestPojo.getIbuNumber(), responseObject.getIbuNumber()),
                () -> Assertions.assertEquals(beerRequestPojo.getStyle(), responseObject.getStyle()),
                () -> Assertions.assertEquals(beerRequestPojo.getBreweryId(), responseObject.getBreweryId()),
                () -> Assertions.assertEquals(beerRequestPojo.getOunces(), responseObject.getOunces()));
    }
}