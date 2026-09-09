package api.beer_service_test;

import api.extensions.annotation.beer.RandomBeerData;
import api.test_data.request.BeerRequest;
import api.test_data.response.beer.BeerErrorResponse;
import api.api.BeerApiRequests;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static api.test_utils.RandomValueUtils.randomNegativeLong;
import static api.test_utils.RandomValueUtils.randomPositiveLong;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Story("Beer_API")
@Tag("Beer_API")
class AddBeerNegativeTest {

    @RandomBeerData
    private BeerRequest request;

    @DisplayName("Error: Blank Name in POST /beer")
    @Test
    void checkAddBeerErrorEmptyName() {
        request.setName("");
        var response = BeerApiRequests.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST));

        Allure.step("Check error message for blank name", () -> {
            BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
            assertThat(errorObject.error()).as("response errors array").isNotEmpty();
            assertThat(errorObject.error().getFirst()).isEqualTo("Name cannot be blank");
        });
    }

    @DisplayName("Error: Blank Style in POST /beer")
    @Test
    void checkAddBeerErrorEmptyStyle() {
        request.setStyle("");
        var response = BeerApiRequests.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST));

        Allure.step("Check error message for blank style", () -> {
            BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
            assertThat(errorObject.error()).as("response errors array").isNotEmpty();
            assertThat(errorObject.error().getFirst()).isEqualTo("Style cannot be blank");
        });
    }

    @DisplayName("Error: Missing BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorEmptyBreweryId() {
        request.setBreweryId(null);
        var response = BeerApiRequests.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST));

        Allure.step("Check error message for missing breweryId", () -> {
            BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
            assertThat(errorObject.error()).as("response errors array").isNotEmpty();
            assertThat(errorObject.error().getFirst()).isEqualTo("BreweryId cannot be null");
        });
    }

    @DisplayName("Error: Negative BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorNegativeNumbersBreweryId() {
        request.setBreweryId(randomNegativeLong(111111L, 999999L));
        var response = BeerApiRequests.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST));

        Allure.step("Check error message for negative breweryId", () -> {
            BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
            assertThat(errorObject.error()).as("response errors array").isNotEmpty();
            assertThat(errorObject.error().getFirst()).isEqualTo("breweryId must be a positive number and must be at least 1");
        });
    }

    @DisplayName("Error: Excessive Digits in BreweryId in POST /beer")
    @Test
    void checkAddBeerErrorAmountOfDigitsBreweryId() {
        request.setBreweryId(randomPositiveLong(111111L, 999999L));
        var response = BeerApiRequests.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST));

        Allure.step("Check error message for excessive digits in breweryId", () -> {
            BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);
            assertThat(errorObject.error()).as("response errors array").isNotEmpty();
            assertThat(errorObject.error().getFirst()).isEqualTo("breweryId must be at most 99999");
        });
    }

    @DisplayName("Verify multiple validation errors in POST /beer")
    @Test
    void checkAddBeerMultipleErrors() {
        request.setName("");
        request.setStyle("");
        request.setBreweryId(null);

        var response = BeerApiRequests.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST));

        BeerErrorResponse errorObject = response.body().as(BeerErrorResponse.class);

        Allure.step("Check multiple validation error messages", () -> assertSoftly(softly -> {
            softly.assertThat(errorObject.error().contains("Name cannot be blank")).isTrue();
            softly.assertThat(errorObject.error().contains("Style cannot be blank")).isTrue();
            softly.assertThat(errorObject.error().contains("BreweryId cannot be null")).isTrue();
        }));
    }
}