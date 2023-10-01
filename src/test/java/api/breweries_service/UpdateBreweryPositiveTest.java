package api.breweries_service;

import api.db_query.BreweryQuery;
import api.pojo.request.BreweryRequestPojo;
import api.pojo.response.brewery.UpdateBreweryResponse;
import api.request.BreweryRequest;
import api.test_utils.data_generators.BreweryObjectGenerator;
import io.qameta.allure.Story;
import org.beerinfo.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.enums.SupportedCountry;
import org.beerinfo.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@Story("Beer API")
public class UpdateBreweryPositiveTest {

    BreweryRequest breweryRequest = new BreweryRequest();
    BreweryRequestPojo breweryRequestPojo;
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    BreweryQuery breweryQuery = new BreweryQuery(sessionFactory);

    long breweryId;

    @BeforeEach
    void createBeerEntityInDB() {
        breweryRequestPojo = BreweryObjectGenerator.generateRandomBreweryPojo();
        breweryId = breweryQuery.addRandomBreweryReturnId();
    }

    @DisplayName("Verify Data in PUT /brewery/{id} Response and Request")
    @Test
    void checkUpdateBreweryResponseData() {
        UpdateBreweryResponse fullResponse = breweryRequest.updateBreweryRequest(breweryRequestPojo, String.valueOf(breweryId));
        UpdateBreweryResponse.BreweryDetails responseObject = fullResponse.brewery();
        Assertions.assertAll(
                () -> Assertions.assertEquals(breweryRequestPojo.getName(), responseObject.name()),
                () -> Assertions.assertEquals(breweryRequestPojo.getCity(), responseObject.city()),
                () -> Assertions.assertEquals(breweryRequestPojo.getState(), responseObject.state()),
                () -> Assertions.assertEquals(breweryRequestPojo.getCountry(), responseObject.country()));
    }

    @DisplayName("Ensure POST /brewery Response Message")
    @Test
    void checkUpdateBreweryResponseText() {
        UpdateBreweryResponse fullResponse = breweryRequest.updateBreweryRequest(breweryRequestPojo, String.valueOf(breweryId));
        String expectedMessage = String.format("Brewery with id: %s was updated.", breweryId);
        Assertions.assertEquals(expectedMessage, fullResponse.message());
    }

    @DisplayName("Verify Data Update in Database with valid Country POST /brewery")
    @ParameterizedTest
    @EnumSource(SupportedCountry.class)
    void checkUpdateBreweryWithValidCountry(SupportedCountry country) {
        breweryRequestPojo.setCountry(country.getCountryName());
        UpdateBreweryResponse fullResponse = breweryRequest.updateBreweryRequest(breweryRequestPojo, String.valueOf(breweryId));
        UpdateBreweryResponse.BreweryDetails updateResponse = fullResponse.brewery();
        GetBreweryResponseDTO entity = breweryQuery.getBreweryById(breweryId);
        Assertions.assertAll(
                () -> Assertions.assertEquals(updateResponse.name(), entity.getName()),
                () -> Assertions.assertEquals(updateResponse.city(), entity.getCity()),
                () -> Assertions.assertEquals(updateResponse.state(), entity.getState()),
                () -> Assertions.assertEquals(updateResponse.country(), entity.getCountry()));
    }
}