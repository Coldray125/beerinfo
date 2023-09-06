package api.breweries_service;

import api.db_query.BreweryQuery;
import api.pojo.request.BreweryRequestPojo;
import api.pojo.response.brewery.UpdateBreweryResponse;
import api.request.BreweryRequest;
import api.test_utils.data_generators.BeerObjectGenerator;
import api.test_utils.data_generators.BreweryObjectGenerator;
import io.qameta.allure.Story;
import org.beerinfo.dto.api.brewery.GetBreweryResponseDTO;
import org.beerinfo.entity.BreweryEntity;
import org.beerinfo.enums.SupportedCountry;
import org.beerinfo.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;

@Story("Beer API")
public class UpdateBreweryPositiveTest {

    BreweryRequest breweryRequest = new BreweryRequest();
    BreweryRequestPojo breweryRequestPojo;
    BreweryEntity breweryEntity;
    HibernateUtil hibernateUtil = new HibernateUtil();
    SessionFactory sessionFactory = hibernateUtil.buildSessionFactory();
    BreweryQuery breweryQuery = new BreweryQuery(sessionFactory);

    long breweryId;

    @BeforeEach
    void createBeerEntityInDB() {
        breweryRequestPojo = BreweryObjectGenerator.generateRandomBreweryPojo();
        breweryEntity = BreweryObjectGenerator.generateRandomBreweryEntity();
        breweryId = breweryQuery.addBreweryReturnId(breweryEntity);
    }

    @DisplayName("Verify Data in PUT /brewery/{id} Response and Request")
    @Test
    void checkUpdateBreweryResponseData() {
        UpdateBreweryResponse fullResponse = breweryRequest.updateBreweryRequest(breweryRequestPojo, String.valueOf(breweryId));
        UpdateBreweryResponse.BreweryDetails responseObject = fullResponse.getBrewery();
        Assertions.assertAll(
                () -> Assertions.assertEquals(breweryRequestPojo.getName(), responseObject.getName()),
                () -> Assertions.assertEquals(breweryRequestPojo.getCity(), responseObject.getCity()),
                () -> Assertions.assertEquals(breweryRequestPojo.getState(), responseObject.getState()),
                () -> Assertions.assertEquals(breweryRequestPojo.getCountry(), responseObject.getCountry()));
    }

    @DisplayName("Ensure POST /beer Response Message")
    @Test
    void checkUpdateBreweryResponseText() {
        UpdateBreweryResponse fullResponse = breweryRequest.updateBreweryRequest(breweryRequestPojo, String.valueOf(breweryId));
        String expectedMessage = String.format("Brewery with id: %s was updated.", breweryId);
        Assertions.assertEquals(expectedMessage, fullResponse.getMessage());
    }

    @DisplayName("Verify Data Update in Database with valid Country POST /beer")
    @ParameterizedTest
    @EnumSource(SupportedCountry.class)
    void checkUpdateBreweryWithValidCountry(SupportedCountry country) {
        breweryRequestPojo.setCountry(country.getCountryName());
        UpdateBreweryResponse fullResponse = breweryRequest.updateBreweryRequest(breweryRequestPojo, String.valueOf(breweryId));
        UpdateBreweryResponse.BreweryDetails updateResponse = fullResponse.getBrewery();
        GetBreweryResponseDTO entity = breweryQuery.getBreweryById(breweryId);
        Assertions.assertAll(
                () -> Assertions.assertEquals(updateResponse.getName(), entity.getName()),
                () -> Assertions.assertEquals(updateResponse.getCity(), entity.getCity()),
                () -> Assertions.assertEquals(updateResponse.getState(), entity.getState()),
                () -> Assertions.assertEquals(updateResponse.getCountry(), entity.getCountry()));
    }
}