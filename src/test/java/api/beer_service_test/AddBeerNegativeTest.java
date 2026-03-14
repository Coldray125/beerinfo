package api.beer_service_test;

import api.extensions.annotation.beer.RandomBeerPojo;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.BeerErrorResponse;
import api.request.BeerRequest;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static api.test_utils.RandomValueUtils.randomNegativeLong;
import static api.test_utils.RandomValueUtils.randomPositiveLong;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

@Story("Beer_API")
@Tag("Beer_API")
public class AddBeerNegativeTest {

    @RandomBeerPojo
    private BeerRequestPojo request;

    @DisplayName("Error: Blank Name in POST /beer")
    @Test
    void checkAddBeerErrorEmptyName() {
        request.setName("");
        var response = BeerRequest.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode()));

        Allure.step("Check error message for blank name", () -> {
            BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
            Assertions.assertEquals("Name cannot be blank", errorObject.error().getFirst());
        });
    }

    @DisplayName("Error: Blank Style in POST /beer")
    @Test
    void checkAddBeerErrorEmptyStyle() {
        request.setStyle("");
        var response = BeerRequest.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode()));

        Allure.step("Check error message for blank style", () -> {
            BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
            Assertions.assertEquals("Style cannot be blank", errorObject.error().getFirst());
        });
    }

    @DisplayName("Error: Missing BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorEmptyBreweryId() {
        request.setBreweryId(null);
        var response = BeerRequest.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode()));

        Allure.step("Check error message for missing breweryId", () -> {
            BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
            Assertions.assertEquals("BreweryId cannot be null", errorObject.error().getFirst());
        });
    }

    @DisplayName("Error: Negative BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorNegativeNumbersBreweryId() {
        request.setBreweryId(randomNegativeLong(111111L, 999999L));
        var response = BeerRequest.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode()));

        Allure.step("Check error message for negative breweryId", () -> {
            BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
            Assertions.assertEquals(
                    "breweryId must be a positive number and must be at least 1",
                    errorObject.error().getFirst()
            );
        });
    }

    @DisplayName("Error: Excessive Digits in BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorAmountOfDigitsBreweryId() {
        request.setBreweryId(randomPositiveLong(111111L, 999999L));
        var response = BeerRequest.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode()));

        Allure.step("Check error message for excessive digits in breweryId", () -> {
            BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
            Assertions.assertEquals("breweryId must be at most 99999", errorObject.error().getFirst());
        });
    }

    @DisplayName("Verify multiple validation errors in POST /beer")
    @Test
    void checkAddBeerMultipleErrors() {
        request.setName("");
        request.setStyle("");
        request.setBreweryId(null);

        var response = BeerRequest.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode()));

        Allure.step("Check multiple validation error messages", () -> {
            BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
            Assertions.assertAll(
                    () -> Assertions.assertTrue(errorObject.error().contains("Name cannot be blank")),
                    () -> Assertions.assertTrue(errorObject.error().contains("Style cannot be blank")),
                    () -> Assertions.assertTrue(errorObject.error().contains("BreweryId cannot be null"))
            );
        });
    }
}