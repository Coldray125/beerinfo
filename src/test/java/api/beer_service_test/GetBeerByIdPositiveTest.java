package api.beer_service_test;

import api.db_query.BeerQuery;
import api.test_data.response.beer.GetBeerResponse;
import api.api.BeerApiRequests;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static api.test_utils.SchemaPaths.BEER_OBJECT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Story("Beer_API")
@Tag("Beer_API")
class GetBeerByIdPositiveTest {

    private String beerId;
    private GetBeerResponseDTO beerEntity;

    @BeforeEach
    void createBeerEntityInDB() {
        beerEntity = BeerQuery.addRandomBeerReturnDTO();
        beerId = String.valueOf(beerEntity.beerId());
    }

    @DisplayName("Verify response code is 200 for GET /beer/{id}")
    @Test
    void checkBeerByIdResponseCode() {
        Response response = BeerApiRequests.getBeerByIdRequestReturnResponse(beerId);

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_OK));
    }

    @DisplayName("Validate response JSON structure for GET /beer/{id}")
    @Test
    void checkBeerByIdResponseStructure() {
        Response response = BeerApiRequests.getBeerByIdRequestReturnResponse(beerId);

        Allure.step("Validate response JSON structure", () ->
                ResponseValidator.assertResponseMatchesSchema(response, BEER_OBJECT.getPath()));
    }

    @DisplayName("Verify response data against database for GET /beer/{id} ")
    @Test
    void checkValuesInBeerByIdResponse() {
        GetBeerResponse response = BeerApiRequests.getBeerByIdRequest(beerId);

        Allure.step("Verify response fields against database values", () -> assertSoftly(softly -> {
            softly.assertThat(response.abv()).as("abv").isEqualTo(beerEntity.abv());
            softly.assertThat(response.name()).as("name").isEqualTo(beerEntity.name());
            softly.assertThat(response.ibuNumber()).as("ibu_number").isEqualTo(beerEntity.ibuNumber());
            softly.assertThat(response.style()).as("style").isEqualTo(beerEntity.style());
            softly.assertThat(response.breweryId()).as("brewery_id").isEqualTo(beerEntity.breweryId());
            softly.assertThat(response.ounces()).as("ounces").isEqualTo(beerEntity.ounces());
        }));
    }
}