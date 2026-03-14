package api.breweries_service_test;

import api.request.BreweryRequest;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static api.test_utils.SchemaPaths.BREWERY_ARRAY;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

@Story("Brewery_API")
@Tag("Brewery_API")
class GetAllBreweriesTest {

    @DisplayName("Verify response JSON Structure for GET /breweries")
    @Test
    void checkGetAllBreweriesResponseStructure() {
        var response = BreweryRequest.getBreweriesRequestReturnResponse();

        Allure.step("Validate response JSON structure", () ->
                ResponseValidator.assertResponseMatchesSchema(response, BREWERY_ARRAY.getPath()));
    }

    @DisplayName("Verify response returns successful status for GET /breweries")
    @Test
    void checkGetAllBreweriesStatusCode() {
        var response = BreweryRequest.getBreweriesRequestReturnResponse();

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_OK));
    }
}