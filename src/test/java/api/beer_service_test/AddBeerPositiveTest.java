package api.beer_service_test;

import api.db_query.BeerQuery;
import api.extensions.annotation.beer.RandomBeerData;
import api.test_data.request.BeerRequest;
import api.test_data.response.beer.AddBeerResponse;
import api.api.BeerApiRequests;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static api.test_utils.SchemaPaths.ADD_BEER_RESPONSE;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Story("Beer_API")
@Tag("Beer_API")
class AddBeerPositiveTest {

    @RandomBeerData
    private BeerRequest request;

    @DisplayName("Verify response data for POST /beer")
    @Test
    void checkAddBeerWriteInDatabase() {
        var fullResponse = BeerApiRequests.addBeerRequest(request);
        AddBeerResponse.BeerDetails responseObject = fullResponse.beer();
        var beerEntity = BeerQuery.getBeerById(responseObject.beerId());

        Allure.step("Verify fullResponse fields against database values", () -> assertSoftly(softly -> {
            softly.assertThat(beerEntity.getBeerId()).as("beer_id").isEqualTo(responseObject.beerId());
            softly.assertThat(beerEntity.getAbv()).as("abv").isEqualTo(responseObject.abv());
            softly.assertThat(beerEntity.getName()).as("name").isEqualTo(responseObject.name());
            softly.assertThat(beerEntity.getIbuNumber()).as("ibu_number").isEqualTo(responseObject.ibuNumber());
            softly.assertThat(beerEntity.getStyle()).as("style").isEqualTo(responseObject.style());
            softly.assertThat(beerEntity.getBreweryId()).as("brewery_id").isEqualTo(responseObject.breweryId());
            softly.assertThat(beerEntity.getOunces()).as("ounces").isEqualTo(responseObject.ounces());
        }));
    }

    @DisplayName("Verify response message for POST /beer")
    @Test
    void checkAddBeerResponseText() {
        var response = BeerApiRequests.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_OK));

        Allure.step("Check response message", () -> {
            String responseText = response.body().path("message");
            assertThat(responseText).isEqualTo("Beer added successfully.");
        });
    }

    @DisplayName("Validate Response JSON Structure for POST /beer")
    @Test
    void checkAddBeerResponseStructure() {
        var response = BeerApiRequests.addBeerRequestReturnResponse(request);

        Allure.step("Validate response JSON structure", () ->
                ResponseValidator.assertResponseMatchesSchema(response, ADD_BEER_RESPONSE.getPath()));
    }

    @DisplayName("Verify data in response after request POST /beer")
    @Test
    void checkValuesAddBeerResponse() {
        var fullResponse = BeerApiRequests.addBeerRequest(request);
        AddBeerResponse.BeerDetails responseObject = fullResponse.beer();

        Allure.step("Check response fields against request values", () -> assertSoftly(softly -> {
            softly.assertThat(responseObject.abv()).as("abv").isEqualTo(responseObject.abv());
            softly.assertThat(responseObject.name()).as("name").isEqualTo(responseObject.name());
            softly.assertThat(responseObject.ibuNumber()).as("ibu_number").isEqualTo(responseObject.ibuNumber());
            softly.assertThat(responseObject.style()).as("style").isEqualTo(responseObject.style());
            softly.assertThat(responseObject.breweryId()).as("brewery_id").isEqualTo(responseObject.breweryId());
            softly.assertThat(responseObject.ounces()).as("ounces").isEqualTo(responseObject.ounces());
        }));
    }
}