package api.beer_service_test;

import api.db_query.BeerQuery;
import api.api.BeerApiRequests;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

@Story("Beer_API")
@Tag("Beer_API")
class DeleteBeerPositiveTest {
    private String beerId;

    @BeforeEach
    void createBeerEntityInDB() {
        GetBeerResponseDTO beerEntity = BeerQuery.addRandomBeerReturnDTO();
        beerId = String.valueOf(beerEntity.beerId());
    }

    @DisplayName("Check response message for DELETE /beer/{beerId}")
    @Test
    void checkDeleteBeerResponseText() {
        var response = BeerApiRequests.deleteBeerRequestReturnResponse(beerId);

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_OK));

        Allure.step("Check response message indicates deletion", () -> {
            String expectedText = "Beer with id: " + beerId + " was deleted";
            String responseText = response.body().path("message");
            assertThat(responseText).isEqualTo(expectedText);
        });
    }
}