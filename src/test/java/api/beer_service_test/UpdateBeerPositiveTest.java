package api.beer_service_test;

import api.db_query.BeerQuery;
import api.extensions.LoggingExtension;
import api.extensions.annotation.beer.RandomBeerPojo;
import api.extensions.resolver.GenericHttpRequestResolver;
import api.extensions.resolver.GenericQueryResolver;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.UpdateBeerResponse;
import api.request.BeerRequest;
import io.qameta.allure.Story;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.qameta.allure.Allure.step;

@Story("Beer_API")
@Tag("Beer_API")
@ExtendWith({LoggingExtension.class})
@ExtendWith({GenericQueryResolver.class, GenericHttpRequestResolver.class})
public class UpdateBeerPositiveTest {
    private String beerId;
    private final BeerQuery beerQuery;
    private final BeerRequest beerRequest;

    @RandomBeerPojo
    private BeerRequestPojo request;

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
    void checkUpdateBeerResponseText() {
        UpdateBeerResponse fullResponse = beerRequest.updateBeerRequest(request, beerId);
        String expectedText = String.format("Beer with id: %s was updated.", beerId);
        String responseText = fullResponse.message();
        Assertions.assertEquals(expectedText, responseText);
    }

    @DisplayName("Verify Data in PUT /beer/{id} Response and Request")
    @Test
    void checkValuesAddBeerResponse() {
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
    void checkAddBeerWriteInDatabase() {
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