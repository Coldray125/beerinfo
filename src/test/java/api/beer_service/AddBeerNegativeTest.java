package api.beer_service;

import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.BeerErrorResponse;
import api.request.BeerRequest;
import api.test_utils.data_generators.BeerObjectGenerator;
import com.github.javafaker.Faker;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

@Story("Beer API")
public class AddBeerNegativeTest {

    Faker faker = new Faker();
    BeerRequest beerRequest = new BeerRequest();
    BeerRequestPojo beerRequestPojo;

    @BeforeEach
    void createBeerEntityInDB() {
        beerRequestPojo = BeerObjectGenerator.generateRandomBeerPojo();
    }

    @DisplayName("Error: Blank Name in POST /beer")
    @Test
    void checkAddBeerErrorEmptyName() {
        beerRequestPojo.setName("");
        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("Name cannot be blank", errorObject.getError().get(0));
    }

    @DisplayName("Error: Blank Style in POST /beer")
    @Test
    void checkAddBeerErrorEmptyStyle() {
        beerRequestPojo.setStyle("");
        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("Style cannot be blank", errorObject.getError().get(0));
    }

    @DisplayName("Error: Missing BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorEmptyBreweryId() {
        beerRequestPojo.setBreweryId(null);
        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("BreweryId cannot be empty", errorObject.getError().get(0));
    }

    @DisplayName("Error: Negative BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorNegativeNumbersBreweryId() {
        beerRequestPojo.setBreweryId(-faker.number().numberBetween(11111, 99999));
        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("breweryId must be a positive number and must be at least 1", errorObject.getError().get(0));
    }

    @DisplayName("Error: Excessive Digits in BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorAmountOfDigitsBreweryId() {
        beerRequestPojo.setBreweryId(faker.number().numberBetween(111111, 999999));
        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("breweryId must be at most 99999", errorObject.getError().get(0));
    }

    @DisplayName("Multiple Validation Errors in POST /beer")
    @Test
    void checkAddBeerMultipleErrors() {
        beerRequestPojo.setName("");
        beerRequestPojo.setStyle("");
        beerRequestPojo.setBreweryId(null);

        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertAll(
                () -> Assertions.assertTrue(errorObject.getError().contains("Name cannot be blank")),
                () -> Assertions.assertTrue(errorObject.getError().contains("Style cannot be blank")),
                () -> Assertions.assertTrue(errorObject.getError().contains("BreweryId cannot be empty"))
        );
    }
}