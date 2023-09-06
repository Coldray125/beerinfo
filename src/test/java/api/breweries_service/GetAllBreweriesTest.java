package api.breweries_service;

import api.request.BreweryRequest;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static api.test_utils.SchemaPaths.BEER_ARRAY;
import static org.apache.http.HttpStatus.SC_OK;

@Story("Brewery API")
public class GetAllBreweriesTest {

    BreweryRequest breweryRequest = new BreweryRequest();

    @DisplayName("Verify GET /beers Response JSON Structure")
    @Test
    void checkGetAllBeersResponseStructure() {
        Response response = breweryRequest.getBreweriesRequestReturnResponse();
        ResponseValidator.assertResponseMatchesSchema(response, BEER_ARRAY.getPath());
    }

    @DisplayName("Ensure GET /beers Response Code")
    @Test
    void checkGetAllBeersStatusCode() {
        Response response = breweryRequest.getBreweriesRequestReturnResponse();
        Assertions.assertEquals(SC_OK, response.getStatusCode());
    }
}