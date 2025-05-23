package api.beer_service_test;

import api.extensions.LoggingExtension;
import api.extensions.annotation.beer.RandomBeerPojo;
import api.extensions.resolver.GenericHttpRequestResolver;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.BeerErrorResponse;
import api.request.BeerRequest;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static api.test_utils.RandomValueUtils.randomNegativeLong;
import static api.test_utils.RandomValueUtils.randomPositiveLong;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

@Story("Beer_API")
@Tag("Beer_API")
@ExtendWith({LoggingExtension.class})
@ExtendWith({GenericHttpRequestResolver.class})
public class AddBeerNegativeTest {

    private final BeerRequest beerRequest;

    @RandomBeerPojo
    private BeerRequestPojo request;

    public AddBeerNegativeTest(BeerRequest beerRequest) {
        this.beerRequest = beerRequest;
    }

    @DisplayName("Error: Blank Name in POST /beer")
    @Test
    void checkAddBeerErrorEmptyName() {
        request.setName("");
        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("Name cannot be blank", errorObject.error().getFirst());
    }

    @DisplayName("Error: Blank Style in POST /beer")
    @Test
    void checkAddBeerErrorEmptyStyle() {
        request.setStyle("");
        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("Style cannot be blank", errorObject.error().getFirst());
    }

    @DisplayName("Error: Missing BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorEmptyBreweryId() {
        request.setBreweryId(null);
        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("BreweryId cannot be null", errorObject.error().getFirst());
    }

    @DisplayName("Error: Negative BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorNegativeNumbersBreweryId() {
        request.setBreweryId(randomNegativeLong(111111L, 999999L));
        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("breweryId must be a positive number and must be at least 1", errorObject.error().getFirst());
    }

    @DisplayName("Error: Excessive Digits in BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorAmountOfDigitsBreweryId() {
        request.setBreweryId(randomPositiveLong(111111L, 999999L));
        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertEquals("breweryId must be at most 99999", errorObject.error().getFirst());
    }

    @DisplayName("Multiple Validation Errors in POST /beer")
    @Test
    void checkAddBeerMultipleErrors() {
        request.setName("");
        request.setStyle("");
        request.setBreweryId(null);

        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
        Assertions.assertAll(
                () -> Assertions.assertTrue(errorObject.error().contains("Name cannot be blank")),
                () -> Assertions.assertTrue(errorObject.error().contains("Style cannot be blank")),
                () -> Assertions.assertTrue(errorObject.error().contains("BreweryId cannot be null"))
        );
    }
}