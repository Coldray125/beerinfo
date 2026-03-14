package api.beer_service_test;

import api.conversion.BeerConverter;
import api.db_query.BeerQuery;
import api.pojo.response.beer.GetBeerResponse;
import api.request.BeerRequest;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static api.test_utils.SchemaPaths.BEER_ARRAY;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

@Story("Beer_API")
@Tag("Beer_API")
class GetAllBeersTest {

    @DisplayName("Verify response contains record added to Postgres in GET /beers")
    @Test
    void checkGetAllBeersContainsAddedRecord() {
        GetBeerResponseDTO entityDTO = BeerQuery.addRandomBeerReturnDTO();
        List<GetBeerResponse> responseList = BeerRequest.getBeerRequestReturnList();

        var filteredResponse = Allure.step("Check response contains the added record", () -> {
            Optional<GetBeerResponse> matchingResponse = responseList.stream()
                    .filter(response -> response.beerId() == entityDTO.beerId())
                    .findFirst();

            assertThat(matchingResponse.isPresent()).as("Record should exist in the response").isTrue();
            return matchingResponse.get();
        });

        var responseDTO = BeerConverter.MAPPER.convertToGetBeerResponseDTO(filteredResponse);

        Allure.step("Verify response record matches database", () ->
                assertThat(responseDTO).as("Response record should match database").isEqualTo(entityDTO));
    }

    @DisplayName("Verify response JSON structure in GET /beers ")
    @Test
    void checkGetAllBeersResponseStructure() {
        var response = BeerRequest.getBeerRequestReturnResponse();

        Allure.step("Validate response JSON structure", () ->
                ResponseValidator.assertResponseMatchesSchema(response, BEER_ARRAY.getPath()));
    }

    @DisplayName("Verify response code for GET /beers")
    @Test
    void checkGetAllBeersStatusCode() {
        var response = BeerRequest.getBeerRequestReturnResponse();

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_OK));
    }
}