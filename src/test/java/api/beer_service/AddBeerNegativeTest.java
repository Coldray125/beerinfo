package api.beer_service;

import api.extensions.BeerRequestParameterResolver;
import api.extensions.annotation.beer.RandomBeerPojo;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.BeerErrorResponse;
import api.request.BeerRequest;
import com.github.javafaker.Faker;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

@Story("Beer API")
@ExtendWith(value = BeerRequestParameterResolver.class)
public class AddBeerNegativeTest {
    Faker faker = new Faker();
    BeerRequest beerRequest;

    public AddBeerNegativeTest(BeerRequest beerRequest) {
        this.beerRequest = beerRequest;
    }


    @DisplayName("Error: Blank Name in POST /beer")
    @Test
    void checkAddBeerErrorEmptyName(@RandomBeerPojo BeerRequestPojo request) {
        request.setName("");
        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("Name cannot be blank", errorObject.error().get(0));
    }

    @DisplayName("Error: Blank Style in POST /beer")
    @Test
    void checkAddBeerErrorEmptyStyle(@RandomBeerPojo BeerRequestPojo request) {
        request.setStyle("");
        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("Style cannot be blank", errorObject.error().get(0));
    }

    @DisplayName("Error: Missing BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorEmptyBreweryId(@RandomBeerPojo BeerRequestPojo request) {
        request.setBreweryId(null);
        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("BreweryId cannot be empty", errorObject.error().get(0));
    }

    @DisplayName("Error: Negative BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorNegativeNumbersBreweryId(@RandomBeerPojo BeerRequestPojo request) {
        request.setBreweryId(-faker.number().numberBetween(11111, 99999));
        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("breweryId must be a positive number and must be at least 1", errorObject.error().get(0));
    }

    @DisplayName("Error: Excessive Digits in BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorAmountOfDigitsBreweryId(@RandomBeerPojo BeerRequestPojo request) {
        request.setBreweryId(faker.number().numberBetween(111111, 999999));
        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("breweryId must be at most 99999", errorObject.error().get(0));
    }

    @DisplayName("Multiple Validation Errors in POST /beer")
    @Test
    void checkAddBeerMultipleErrors(@RandomBeerPojo BeerRequestPojo request) {
        request.setName("");
        request.setStyle("");
        request.setBreweryId(null);

        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertAll(
                () -> Assertions.assertTrue(errorObject.error().contains("Name cannot be blank")),
                () -> Assertions.assertTrue(errorObject.error().contains("Style cannot be blank")),
                () -> Assertions.assertTrue(errorObject.error().contains("BreweryId cannot be empty"))
        );
    }
}