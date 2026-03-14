package api.beer_service_test;

import api.db_query.BeerQuery;
import api.extensions.LoggingExtension;
import api.extensions.annotation.beer.RandomBeerPojo;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.AddBeerResponse;
import api.request.BeerRequest;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static api.test_utils.SchemaPaths.ADD_BEER_RESPONSE;
import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer_API")
@Tag("Beer_API")
@ExtendWith({LoggingExtension.class})
public class AddBeerPositiveTest {

    @RandomBeerPojo
    private BeerRequestPojo request;

    @DisplayName("Verify response data for POST /beer")
    @Test
    void checkAddBeerWriteInDatabase() {
        var response = BeerRequest.addBeerRequest(request);
        AddBeerResponse.BeerDetails responseObject = response.beer();
        var beerEntity = BeerQuery.getBeerById(responseObject.beerId());

        Allure.step("Verify response fields against database values", () -> Assertions.assertAll(
                () -> Assertions.assertEquals(responseObject.beerId(), beerEntity.getBeerId()),
                () -> Assertions.assertEquals(responseObject.abv(), beerEntity.getAbv()),
                () -> Assertions.assertEquals(responseObject.name(), beerEntity.getName()),
                () -> Assertions.assertEquals(responseObject.ibuNumber(), beerEntity.getIbuNumber()),
                () -> Assertions.assertEquals(responseObject.style(), beerEntity.getStyle()),
                () -> Assertions.assertEquals(responseObject.breweryId(), beerEntity.getBreweryId()),
                () -> Assertions.assertEquals(responseObject.ounces(), beerEntity.getOunces())
        ));
    }

    @DisplayName("Verify response message for POST /beer")
    @Test
    void checkAddBeerResponseText() {
        var response = BeerRequest.addBeerRequestReturnResponse(request);

        Allure.step("Check response status code", () ->
                Assertions.assertEquals(SC_OK, response.getStatusCode()));

        Allure.step("Check response message", () -> {
            String responseText = response.body().path("message");
            Assertions.assertEquals("Beer added successfully.", responseText);
        });
    }

    @DisplayName("Validate Response JSON Structure for POST /beer")
    @Test
    void checkAddBeerResponseStructure() {
        var response = BeerRequest.addBeerRequestReturnResponse(request);

        Allure.step("Validate response JSON structure", () ->
                ResponseValidator.assertResponseMatchesSchema(response, ADD_BEER_RESPONSE.getPath()));
    }

    @DisplayName("Verify data in response after request POST /beer")
    @Test
    void checkValuesAddBeerResponse() {
        AddBeerResponse fullResponse = BeerRequest.addBeerRequest(request);
        AddBeerResponse.BeerDetails responseObject = fullResponse.beer();

        Allure.step("Check response fields against request values", () -> Assertions.assertAll(
                () -> Assertions.assertEquals(request.getAbv(), responseObject.abv()),
                () -> Assertions.assertEquals(request.getName(), responseObject.name()),
                () -> Assertions.assertEquals(request.getIbuNumber(), responseObject.ibuNumber()),
                () -> Assertions.assertEquals(request.getStyle(), responseObject.style()),
                () -> Assertions.assertEquals(request.getBreweryId(), responseObject.breweryId()),
                () -> Assertions.assertEquals(request.getOunces(), responseObject.ounces())));
    }
}