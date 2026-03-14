package api.beer_service_test;

import api.db_query.BeerQuery;
import api.extensions.LoggingExtension;
import api.request.BeerRequest;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer_API")
@Tag("Beer_API")
@ExtendWith({LoggingExtension.class})
public class DeleteBeerPositiveTest {
    private String beerId;

    @BeforeEach
    void createBeerEntityInDB() {
        GetBeerResponseDTO beerEntity = BeerQuery.addRandomBeerReturnDTO();
        beerId = String.valueOf(beerEntity.beerId());
    }

    @DisplayName("Check response message for DELETE /beer/{beerId}")
    @Test
    void checkDeleteBeerResponseText() {
        Response response = BeerRequest.deleteBeerRequestReturnResponse(beerId);

        Allure.step("Check response status code", () ->
                Assertions.assertEquals(SC_OK, response.getStatusCode()));

        Allure.step("Check response message indicates deletion", () -> {
            String expectedText = "Beer with id: " + beerId + " was deleted";
            String responseText = response.body().path("message");
            Assertions.assertEquals(expectedText, responseText);
        });
    }
}