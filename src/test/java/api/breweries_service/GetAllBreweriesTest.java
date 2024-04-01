package api.breweries_service;

import api.extensions.LoggingExtension;
import api.extensions.resolver.BreweryRequestParameterResolver;
import api.request.BreweryRequest;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static api.test_utils.SchemaPaths.BREWERY_ARRAY;
import static org.apache.http.HttpStatus.SC_OK;

@Story("Brewery API")
@Tag("Brewery API")
@ExtendWith({LoggingExtension.class})
@ExtendWith(value = BreweryRequestParameterResolver.class)
public class GetAllBreweriesTest {
    BreweryRequest breweryRequest;

    public GetAllBreweriesTest(BreweryRequest breweryRequest) {
        this.breweryRequest = breweryRequest;
    }

    @DisplayName("Verify GET /breweries Response JSON Structure")
    @Test
    void checkGetAllBreweriesResponseStructure() {
        Response response = breweryRequest.getBreweriesRequestReturnResponse();
        ResponseValidator.assertResponseMatchesSchema(response, BREWERY_ARRAY.getPath());
    }

    @DisplayName("Ensure GET /breweries Response Code")
    @Test
    void checkGetAllBreweriesStatusCode() {
        Response response = breweryRequest.getBreweriesRequestReturnResponse();
        Assertions.assertEquals(SC_OK, response.getStatusCode());
    }
}