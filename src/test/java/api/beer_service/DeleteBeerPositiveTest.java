package api.beer_service;

import api.db_query.BeerQuery;
import api.request.BeerRequest;
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

import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer API")
public class DeleteBeerPositiveTest {

    BeerRequest beerRequest = new BeerRequest();
    BeerEntity beerEntity;
    HibernateUtil hibernateUtil = new HibernateUtil();
    SessionFactory sessionFactory = hibernateUtil.buildSessionFactory();
    BeerQuery beerQuery = new BeerQuery(sessionFactory);

    long beerId;

    @BeforeEach
    void createBeerEntityInDB() {
        beerEntity = BeerObjectGenerator.generateRandomBeerEntity();
        beerId = beerQuery.addBeerReturnId(beerEntity);
    }

    @DisplayName("Check DELETE /beer/{id} response message")
    @Test
    void checkDeleteBeerResponseText() {
        Response response = beerRequest.deleteBeerRequestReturnResponse(String.valueOf(beerId));

        Assertions.assertEquals(SC_OK, response.getStatusCode());

        String expectedText = String.format("Beer with id: %s was deleted.", beerId);
        String responseText = response.body().path("message");
        Assertions.assertEquals(expectedText, responseText);
    }
}