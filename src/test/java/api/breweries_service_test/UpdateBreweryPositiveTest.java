package api.breweries_service_test;

import api.db_query.BreweryQuery;
import api.extensions.annotation.brewery.RandomBreweryPojo;
import api.pojo.request.BreweryRequestPojo;
import api.pojo.response.brewery.UpdateBreweryResponse;
import api.request.BreweryRequest;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.beerinfo.data.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.enums.SupportedCountry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Story("Brewery_API")
@Tag("Brewery_API")
class UpdateBreweryPositiveTest {

    private long breweryId;

    @RandomBreweryPojo
    private BreweryRequestPojo request;

    @BeforeEach
    void createBeerEntityInDB() {
        breweryId = BreweryQuery.addRandomBreweryReturnId();
    }

    @DisplayName("Verify response data matches request for PUT /brewery/{breweryId}")
    @Test
    void checkUpdateBreweryResponseData() {
        UpdateBreweryResponse fullResponse = BreweryRequest.updateBreweryRequest(request, String.valueOf(breweryId));
        UpdateBreweryResponse.BreweryDetails responseObject = fullResponse.brewery();

        Allure.step("Check response fields match request data", () -> assertSoftly(softly -> {
            softly.assertThat(responseObject.name()).as("name").isEqualTo(request.getName());
            softly.assertThat(responseObject.city()).as("city").isEqualTo(request.getCity());
            softly.assertThat(responseObject.state()).as("state").isEqualTo(request.getState());
            softly.assertThat(responseObject.country()).as("country").isEqualTo(request.getCountry());
        }));
    }

    @DisplayName("Verify response message for successful PUT /brewery/{breweryId}")
    @Test
    void checkUpdateBreweryResponseText() {
        UpdateBreweryResponse fullResponse = BreweryRequest.updateBreweryRequest(request, String.valueOf(breweryId));
        String expectedMessage = String.format("Brewery with id: %s was updated.", breweryId);

        Allure.step("Check response message indicates successful update", () ->
                assertThat(fullResponse.message()).isEqualTo(expectedMessage));
    }

    @DisplayName("Verify database data update with valid country for PUT /brewery/{breweryId}")
    @ParameterizedTest
    @EnumSource(SupportedCountry.class)
    void checkUpdateBreweryWithValidCountry(SupportedCountry country) {
        request.setCountry(country.getCountryName());
        UpdateBreweryResponse fullResponse = BreweryRequest.updateBreweryRequest(request, String.valueOf(breweryId));
        UpdateBreweryResponse.BreweryDetails updateResponse = fullResponse.brewery();

        GetBreweryResponseDTO entity = BreweryQuery.getBreweryById(breweryId);

        Allure.step("Check response fields match database values", () -> assertSoftly(softly -> {
            softly.assertThat(updateResponse.name()).as("name").isEqualTo(entity.name());
            softly.assertThat(updateResponse.city()).as("city").isEqualTo(entity.city());
            softly.assertThat(updateResponse.state()).as("state").isEqualTo(entity.state());
            softly.assertThat(updateResponse.country()).as("country").isEqualTo(entity.country());
        }));
    }
}