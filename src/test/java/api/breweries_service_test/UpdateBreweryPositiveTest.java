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
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@Story("Brewery_API")
@Tag("Brewery_API")
public class UpdateBreweryPositiveTest {

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

        Allure.step("Check response fields match request data", () -> Assertions.assertAll(
                () -> Assertions.assertEquals(request.getName(), responseObject.name()),
                () -> Assertions.assertEquals(request.getCity(), responseObject.city()),
                () -> Assertions.assertEquals(request.getState(), responseObject.state()),
                () -> Assertions.assertEquals(request.getCountry(), responseObject.country())
        ));
    }

    @DisplayName("Verify response message for successful PUT /brewery/{breweryId}")
    @Test
    void checkUpdateBreweryResponseText() {
        UpdateBreweryResponse fullResponse = BreweryRequest.updateBreweryRequest(request, String.valueOf(breweryId));
        String expectedMessage = String.format("Brewery with id: %s was updated.", breweryId);

        Allure.step("Check response message indicates successful update", () ->
                Assertions.assertEquals(expectedMessage, fullResponse.message()));
    }

    @DisplayName("Verify database data update with valid country for PUT /brewery/{breweryId}")
    @ParameterizedTest
    @EnumSource(SupportedCountry.class)
    void checkUpdateBreweryWithValidCountry(SupportedCountry country) {
        request.setCountry(country.getCountryName());
        UpdateBreweryResponse fullResponse = BreweryRequest.updateBreweryRequest(request, String.valueOf(breweryId));
        UpdateBreweryResponse.BreweryDetails updateResponse = fullResponse.brewery();

        GetBreweryResponseDTO entity = BreweryQuery.getBreweryById(breweryId);

        Allure.step("Check response fields match database values", () -> Assertions.assertAll(
                () -> Assertions.assertEquals(updateResponse.name(), entity.name()),
                () -> Assertions.assertEquals(updateResponse.city(), entity.city()),
                () -> Assertions.assertEquals(updateResponse.state(), entity.state()),
                () -> Assertions.assertEquals(updateResponse.country(), entity.country())
        ));
    }
}