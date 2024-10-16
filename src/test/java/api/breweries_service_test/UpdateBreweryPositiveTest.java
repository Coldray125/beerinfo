package api.breweries_service_test;

import api.db_query.BreweryQuery;
import api.extensions.LoggingExtension;
import api.extensions.annotation.brewery.RandomBreweryPojo;
import api.extensions.resolver.BreweryQueryParameterResolver;
import api.extensions.resolver.BreweryRequestParameterResolver;
import api.pojo.request.BreweryRequestPojo;
import api.pojo.response.brewery.UpdateBreweryResponse;
import api.request.BreweryRequest;
import io.qameta.allure.Story;
import org.beerinfo.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.enums.SupportedCountry;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@Story("Brewery_API")
@Tag("Brewery_API")
@ExtendWith({LoggingExtension.class})
@ExtendWith({BreweryRequestParameterResolver.class, BreweryQueryParameterResolver.class})
public class UpdateBreweryPositiveTest {
    private final BreweryQuery breweryQuery;
    private final BreweryRequest breweryRequest;
    private long breweryId;
    @RandomBreweryPojo
    BreweryRequestPojo request;

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
    void checkUpdateBreweryResponseData() {
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
    void checkUpdateBreweryResponseText() {
        UpdateBreweryResponse fullResponse = breweryRequest.updateBreweryRequest(request, String.valueOf(breweryId));
        String expectedMessage = String.format("Brewery with id: %s was updated.", breweryId);
        Assertions.assertEquals(expectedMessage, fullResponse.message());
    }

    @DisplayName("Verify Data Update in Database with valid Country POST /brewery")
    @ParameterizedTest
    @EnumSource(SupportedCountry.class)
    void checkUpdateBreweryWithValidCountry(SupportedCountry country) {
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