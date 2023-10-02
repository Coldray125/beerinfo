package api.beer_service;

import api.db_query.BeerQuery;
import api.extensions.BeerQueryParameterResolver;
import api.extensions.BeerRequestParameterResolver;
import api.extensions.annotation.beer.RandomBeerPojo;
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
import org.junit.jupiter.api.extension.ExtendWith;

import static io.qameta.allure.Allure.step;

@Story("Beer API")
@ExtendWith({BeerQueryParameterResolver.class, BeerRequestParameterResolver.class})
public class UpdateBeerPositiveTest {
    String beerId;
    BeerQuery beerQuery;
    BeerRequest beerRequest;

    public UpdateBeerPositiveTest(BeerQuery beerQuery, BeerRequest beerRequest) {
        this.beerQuery = beerQuery;
        this.beerRequest = beerRequest;
    }

    @BeforeEach
    void createBeerEntityInDB() {
        GetBeerResponseDTO entity = beerQuery.addRandomBeerReturnDTO();
        beerId = String.valueOf(entity.beerId());
    }

    @DisplayName("Verify Response Text for PUT /beer/{id}")
    @Test
    void checkUpdateBeerResponseText(@RandomBeerPojo BeerRequestPojo request) {
        UpdateBeerResponse fullResponse = beerRequest.updateBeerRequest(request, beerId);
        String expectedText = String.format("Beer with id: %s was updated.", beerId);
        String responseText = fullResponse.message();
        Assertions.assertEquals(expectedText, responseText);
    }

    @DisplayName("Verify Data in PUT /beer/{id} Response and Request")
    @Test
    void checkValuesAddBeerResponse(@RandomBeerPojo BeerRequestPojo request) {
        UpdateBeerResponse fullResponse = beerRequest.updateBeerRequest(request, beerId);
        UpdateBeerResponse.BeerDetails response = fullResponse.beer();
        Assertions.assertAll(
                () -> Assertions.assertEquals(request.getAbv(), response.abv()),
                () -> Assertions.assertEquals(request.getName(), response.name()),
                () -> Assertions.assertEquals(request.getIbuNumber(), response.ibuNumber()),
                () -> Assertions.assertEquals(request.getStyle(), response.style()),
                () -> Assertions.assertEquals(request.getBreweryId(), response.breweryId()),
                () -> Assertions.assertEquals(request.getOunces(), response.ounces()));
    }

    @DisplayName("Verify Data in PUT /beer/{id} Response and Database")
    @Test
    void checkAddBeerWriteInDatabase(@RandomBeerPojo BeerRequestPojo request) {
        UpdateBeerResponse fullResponse = beerRequest.updateBeerRequest(request, beerId);
        UpdateBeerResponse.BeerDetails response = fullResponse.beer();
        GetBeerResponseDTO beerEntity = beerQuery.getBeerById(Long.parseLong(beerId));

        step("Validate response JSON against database values");
        Assertions.assertAll(
                () -> Assertions.assertEquals(response.abv(), beerEntity.abv()),
                () -> Assertions.assertEquals(response.name(), beerEntity.name()),
                () -> Assertions.assertEquals(response.ibuNumber(), beerEntity.ibuNumber()),
                () -> Assertions.assertEquals(response.style(), beerEntity.style()),
                () -> Assertions.assertEquals(response.breweryId(), beerEntity.breweryId()),
                () -> Assertions.assertEquals(response.ounces(), beerEntity.ounces())
        );
    }
}