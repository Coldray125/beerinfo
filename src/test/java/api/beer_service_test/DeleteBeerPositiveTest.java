package api.beer_service_test;

import api.db_query.BeerQuery;
import api.extensions.LoggingExtension;
import api.extensions.resolver.BeerQueryParameterResolver;
import api.extensions.resolver.BeerRequestParameterResolver;
import api.request.BeerRequest;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer_API")
@Tag("Beer_API")
@ExtendWith({LoggingExtension.class})
@ExtendWith({BeerQueryParameterResolver.class, BeerRequestParameterResolver.class})
public class DeleteBeerPositiveTest {
    GetBeerResponseDTO beerEntity;
    String beerId;
    BeerQuery beerQuery;
    BeerRequest beerRequest;

    public DeleteBeerPositiveTest(BeerQuery beerQuery, BeerRequest beerRequest) {
        this.beerQuery = beerQuery;
        this.beerRequest = beerRequest;
    }

    @BeforeEach
    void createBeerEntityInDB() {
        beerEntity = beerQuery.addRandomBeerReturnDTO();
        beerId = String.valueOf(beerEntity.beerId());
    }

    @DisplayName("Check DELETE /beer/{beerId} response message")
    @Test
    void checkDeleteBeerResponseText() {
        Response response = beerRequest.deleteBeerRequestReturnResponse(beerId);
        Assertions.assertEquals(SC_OK, response.getStatusCode());

        String expectedText = STR."Beer with beerId: \{beerId} was deleted";
        String responseText = response.body().path("message");
        Assertions.assertEquals(expectedText, responseText);
    }
}