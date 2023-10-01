package api.beer_service;

import api.db_query.BeerQuery;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.UpdateBeerResponse;
import api.request.BeerRequest;
import api.test_utils.data_generators.BeerObjectGenerator;
import io.qameta.allure.Story;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.beerinfo.utils.HibernateUtil.getSessionFactory;

@Story("Beer API")
public class UpdateBeerPositiveTest {

    BeerRequest beerRequest = new BeerRequest();
    BeerRequestPojo beerRequestPojo;
    BeerQuery beerQuery = new BeerQuery(getSessionFactory());

    String beerId;

    @BeforeEach
    void createBeerEntityInDB() {
        beerRequestPojo = BeerObjectGenerator.generateRandomBeerPojo();
        GetBeerResponseDTO entity = beerQuery.addRandomBeerReturnDTO();
        beerId = String.valueOf(entity.beerId());
    }

    @DisplayName("Verify Response Text for PUT /beer/{id}")
    @Test
    void checkUpdateBeerResponseText() {
        UpdateBeerResponse fullResponse = beerRequest.updateBeerRequest(beerRequestPojo, beerId);
        String expectedText = String.format("Beer with id: %s was updated.", beerId);
        String responseText = fullResponse.message();
        Assertions.assertEquals(expectedText, responseText);
    }

    @DisplayName("Verify Data in PUT /beer/{id} Response and Request")
    @Test
    void checkValuesAddBeerResponse() {
        UpdateBeerResponse fullResponse = beerRequest.updateBeerRequest(beerRequestPojo, beerId);
        UpdateBeerResponse.BeerDetails responseObject = fullResponse.beer();
        Assertions.assertAll(
                () -> Assertions.assertEquals(beerRequestPojo.getAbv(), responseObject.abv()),
                () -> Assertions.assertEquals(beerRequestPojo.getName(), responseObject.name()),
                () -> Assertions.assertEquals(beerRequestPojo.getIbuNumber(), responseObject.ibuNumber()),
                () -> Assertions.assertEquals(beerRequestPojo.getStyle(), responseObject.style()),
                () -> Assertions.assertEquals(beerRequestPojo.getBreweryId(), responseObject.breweryId()),
                () -> Assertions.assertEquals(beerRequestPojo.getOunces(), responseObject.ounces()));
    }

    @DisplayName("Verify Data in PUT /beer/{id} Response and Database")
    @Test
    void checkAddBeerWriteInDatabase() {
        UpdateBeerResponse fullResponse = beerRequest.updateBeerRequest(beerRequestPojo, beerId);
        UpdateBeerResponse.BeerDetails responseObject = fullResponse.beer();
        GetBeerResponseDTO beerEntity = beerQuery.getBeerById(Long.parseLong(beerId));

        step("Validate response JSON against database values");
        Assertions.assertAll(
                () -> Assertions.assertEquals(responseObject.abv(), beerEntity.abv()),
                () -> Assertions.assertEquals(responseObject.name(), beerEntity.name()),
                () -> Assertions.assertEquals(responseObject.ibuNumber(), beerEntity.ibuNumber()),
                () -> Assertions.assertEquals(responseObject.style(), beerEntity.style()),
                () -> Assertions.assertEquals(responseObject.breweryId(), beerEntity.breweryId()),
                () -> Assertions.assertEquals(responseObject.ounces(), beerEntity.ounces())
        );
    }
}