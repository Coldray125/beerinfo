package api.beer_service;

import api.db_query.BeerQuery;
import api.extensions.BeerQueryParameterResolver;
import api.extensions.BeerRequestParameterResolver;
import api.request.BeerRequest;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer API")
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

    @DisplayName("Check DELETE /beer/{id} response message")
    @Test
    void checkDeleteBeerResponseText() {
        Response response = beerRequest.deleteBeerRequestReturnResponse(beerId);

        Assertions.assertEquals(SC_OK, response.getStatusCode());

        String expectedText = String.format("Beer with id: %s was deleted.", beerId);
        String responseText = response.body().path("message");
        Assertions.assertEquals(expectedText, responseText);
    }
}