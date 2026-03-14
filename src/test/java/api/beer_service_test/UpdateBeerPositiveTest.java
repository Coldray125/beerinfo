package api.beer_service_test;

import api.db_query.BeerQuery;
import api.extensions.annotation.beer.RandomBeerPojo;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.UpdateBeerResponse;
import api.request.BeerRequest;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.*;

@Story("Beer_API")
@Tag("Beer_API")
public class UpdateBeerPositiveTest {
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
            Assertions.assertEquals(expectedText, responseText);
        });
    }

    @DisplayName("Verify response data matches request for PUT /beer/{id}")
    @Test
    void checkValuesAddBeerResponse() {
        UpdateBeerResponse fullResponse = BeerRequest.updateBeerRequest(request, beerId);
        UpdateBeerResponse.BeerDetails response = fullResponse.beer();

        Allure.step("Check response fields against request values", () -> Assertions.assertAll(
                () -> Assertions.assertEquals(request.getAbv(), response.abv()),
                () -> Assertions.assertEquals(request.getName(), response.name()),
                () -> Assertions.assertEquals(request.getIbuNumber(), response.ibuNumber()),
                () -> Assertions.assertEquals(request.getStyle(), response.style()),
                () -> Assertions.assertEquals(request.getBreweryId(), response.breweryId()),
                () -> Assertions.assertEquals(request.getOunces(), response.ounces())
        ));
    }

    @DisplayName("Verify data in response against database for PUT /beer/{id}")
    @Test
    void checkAddBeerWriteInDatabase() {
        UpdateBeerResponse fullResponse = BeerRequest.updateBeerRequest(request, beerId);
        UpdateBeerResponse.BeerDetails response = fullResponse.beer();

        var beerEntity = BeerQuery.getBeerById(Long.parseLong(beerId));

        Allure.step("Check response fields match database values", () -> Assertions.assertAll(
                () -> Assertions.assertTrue(fullResponse.message().contains(String.valueOf(beerEntity.getBeerId()))),
                () -> Assertions.assertEquals(response.abv(), beerEntity.getAbv()),
                () -> Assertions.assertEquals(response.name(), beerEntity.getName()),
                () -> Assertions.assertEquals(response.ibuNumber(), beerEntity.getIbuNumber()),
                () -> Assertions.assertEquals(response.style(), beerEntity.getStyle()),
                () -> Assertions.assertEquals(response.breweryId(), beerEntity.getBreweryId()),
                () -> Assertions.assertEquals(response.ounces(), beerEntity.getOunces())
        ));
    }
}