package api.breweries_service;

import api.db_query.BreweryQuery;
import api.extensions.BreweryQueryParameterResolver;
import api.extensions.BreweryRequestParameterResolver;
import api.extensions.annotation.brewery.RandomBreweryPojo;
import api.pojo.request.BreweryRequestPojo;
import api.pojo.response.brewery.UpdateBreweryResponse;
import api.request.BreweryRequest;
import api.test_utils.data_generators.BreweryObjectGenerator;
import io.qameta.allure.Story;
import org.beerinfo.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.enums.SupportedCountry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@Story("Beer API")
@ExtendWith(value = BreweryRequestParameterResolver.class)
@ExtendWith(value = BreweryQueryParameterResolver.class)
public class UpdateBreweryPositiveTest {
    BreweryQuery breweryQuery;
    BreweryRequest breweryRequest;
    long breweryId;

    public UpdateBreweryPositiveTest(BreweryQuery breweryQuery, BreweryRequest breweryRequest) {
        this.breweryQuery = breweryQuery;
        this.breweryRequest = breweryRequest;
    }

    @BeforeEach
    void createBeerEntityInDB() {
        breweryId = breweryQuery.addRandomBreweryReturnId();
    }

    @DisplayName("Verify Data in PUT /brewery/{id} Response and Request")
    @Test
    void checkUpdateBreweryResponseData(@RandomBreweryPojo BreweryRequestPojo request) {
        UpdateBreweryResponse fullResponse = breweryRequest.updateBreweryRequest(request, String.valueOf(breweryId));
        UpdateBreweryResponse.BreweryDetails responseObject = fullResponse.brewery();
        Assertions.assertAll(
                () -> Assertions.assertEquals(request.getName(), responseObject.name()),
                () -> Assertions.assertEquals(request.getCity(), responseObject.city()),
                () -> Assertions.assertEquals(request.getState(), responseObject.state()),
                () -> Assertions.assertEquals(request.getCountry(), responseObject.country()));
    }

    @DisplayName("Ensure POST /brewery Response Message")
    @Test
    void checkUpdateBreweryResponseText(@RandomBreweryPojo BreweryRequestPojo request) {
        UpdateBreweryResponse fullResponse = breweryRequest.updateBreweryRequest(request, String.valueOf(breweryId));
        String expectedMessage = String.format("Brewery with id: %s was updated.", breweryId);
        Assertions.assertEquals(expectedMessage, fullResponse.message());
    }

    @DisplayName("Verify Data Update in Database with valid Country POST /brewery")
    @ParameterizedTest
    @EnumSource(SupportedCountry.class)
    void checkUpdateBreweryWithValidCountry(SupportedCountry country, @RandomBreweryPojo BreweryRequestPojo request) {
        request.setCountry(country.getCountryName());
        UpdateBreweryResponse fullResponse = breweryRequest.updateBreweryRequest(request, String.valueOf(breweryId));
        UpdateBreweryResponse.BreweryDetails updateResponse = fullResponse.brewery();
        GetBreweryResponseDTO entity = breweryQuery.getBreweryById(breweryId);
        Assertions.assertAll(
                () -> Assertions.assertEquals(updateResponse.name(), entity.getName()),
                () -> Assertions.assertEquals(updateResponse.city(), entity.getCity()),
                () -> Assertions.assertEquals(updateResponse.state(), entity.getState()),
                () -> Assertions.assertEquals(updateResponse.country(), entity.getCountry()));
    }
}