package api.beer_service_test;

import api.db_query.BeerQuery;
import api.extensions.annotation.beer.RandomBeerPojo;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.UpdateBeerResponse;
import api.request.BeerRequest;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Story("Beer_API")
@Tag("Beer_API")
class UpdateBeerPositiveTest {
    private String beerId;

    @RandomBeerPojo
    private BeerRequestPojo request;

    @BeforeEach
    void createBeerEntityInDB() {
        GetBeerResponseDTO entity = BeerQuery.addRandomBeerReturnDTO();
        beerId = String.valueOf(entity.beerId());
    }

    @DisplayName("Verify response text for PUT /beer/{id}")
    @Test
    void checkUpdateBeerResponseText() {
        UpdateBeerResponse fullResponse = BeerRequest.updateBeerRequest(request, beerId);
        String expectedText = String.format("Beer with id: %s was updated.", beerId);

        Allure.step("Check response message", () -> {
            String responseText = fullResponse.message();
            assertThat(responseText).isEqualTo(expectedText);
        });
    }

    @DisplayName("Verify response data matches request for PUT /beer/{id}")
    @Test
    void checkValuesAddBeerResponse() {
        UpdateBeerResponse fullResponse = BeerRequest.updateBeerRequest(request, beerId);
        UpdateBeerResponse.BeerDetails response = fullResponse.beer();

        Allure.step("Check response fields against request values", () -> assertSoftly(softly -> {
            softly.assertThat(response.abv()).as("abv").isEqualTo(request.getAbv());
            softly.assertThat(response.name()).as("name").isEqualTo(request.getName());
            softly.assertThat(response.ibuNumber()).as("ibu_number").isEqualTo(request.getIbuNumber());
            softly.assertThat(response.style()).as("style").isEqualTo(request.getStyle());
            softly.assertThat(response.breweryId()).as("brewery_id").isEqualTo(request.getBreweryId());
            softly.assertThat(response.ounces()).as("ounces").isEqualTo(request.getOunces());
        }));
    }

    @DisplayName("Verify data in response against database for PUT /beer/{id}")
    @Test
    void checkAddBeerWriteInDatabase() {
        UpdateBeerResponse fullResponse = BeerRequest.updateBeerRequest(request, beerId);
        UpdateBeerResponse.BeerDetails response = fullResponse.beer();

        var beerEntity = BeerQuery.getBeerById(Long.parseLong(beerId));

        Allure.step("Check response fields match database values", () -> assertSoftly(softly -> {
            softly.assertThat(fullResponse.message()).as("beer_id").contains(String.valueOf(beerEntity.getBeerId()));
            softly.assertThat(response.abv()).as("abv").isEqualTo(beerEntity.getAbv());
            softly.assertThat(response.name()).as("name").isEqualTo(beerEntity.getName());
            softly.assertThat(response.ibuNumber()).as("ibu_number").isEqualTo(beerEntity.getIbuNumber());
            softly.assertThat(response.style()).as("style").isEqualTo(beerEntity.getStyle());
            softly.assertThat(response.breweryId()).as("brewery_id").isEqualTo(beerEntity.getBreweryId());
            softly.assertThat(response.ounces()).as("ounces").isEqualTo(beerEntity.getOunces());
        }));
    }
}