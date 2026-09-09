package api.beer_service_test;

import api.db_query.BeerQuery;
import api.test_data.response.beer.GetBeerResponse;
import api.api.BeerApiRequests;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static api.test_utils.SchemaPaths.BEER_ARRAY;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Story("Beer_API")
@Tag("Beer_API")
class GetAllBeersTest {

    @DisplayName("Verify response contains record added to Postgres in GET /beers")
    @Test
    void checkGetAllBeersContainsAddedRecord() {
        GetBeerResponseDTO entityDTO = BeerQuery.addRandomBeerReturnDTO();
        List<GetBeerResponse> responseList = BeerApiRequests.getBeerRequestReturnList();

        Allure.step("Verify response record matches database", () ->
                assertThat(responseList)
                        .filteredOn(filter -> filter.beerId() == entityDTO.beerId())
                        .singleElement()
                        .satisfies(response -> assertSoftly(softly -> {
                            softly.assertThat(response.beerId()).as("beer_id").isEqualTo(entityDTO.beerId());
                            softly.assertThat(response.abv()).as("abv").isEqualTo(entityDTO.abv());
                            softly.assertThat(response.name()).as("name").isEqualTo(entityDTO.name());
                            softly.assertThat(response.ibuNumber()).as("ibu_number").isEqualTo(entityDTO.ibuNumber());
                            softly.assertThat(response.style()).as("style").isEqualTo(entityDTO.style());
                            softly.assertThat(response.breweryId()).as("brewery_id").isEqualTo(entityDTO.breweryId());
                            softly.assertThat(response.ounces()).as("ounces").isEqualTo(entityDTO.ounces());
                        })));
    }

    @DisplayName("Verify response JSON structure in GET /beers ")
    @Test
    void checkGetAllBeersResponseStructure() {
        var response = BeerApiRequests.getBeerRequestReturnResponse();

        Allure.step("Validate response JSON structure", () ->
                ResponseValidator.assertResponseMatchesSchema(response, BEER_ARRAY.getPath()));
    }

    @DisplayName("Verify response code for GET /beers")
    @Test
    void checkGetAllBeersStatusCode() {
        var response = BeerApiRequests.getBeerRequestReturnResponse();

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_OK));
    }
}