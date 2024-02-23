package api.beer_service;

import api.db_query.BeerQuery;
import api.extensions.LoggingExtension;
import api.extensions.resolver.BeerQueryParameterResolver;
import api.extensions.resolver.BeerRequestParameterResolver;
import api.pojo.response.beer.GetBeerResponse;
import api.request.BeerRequest;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static api.test_utils.SchemaPaths.BEER_OBJECT;
import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer API")
@ExtendWith({LoggingExtension.class})
@ExtendWith({BeerQueryParameterResolver.class, BeerRequestParameterResolver.class})
public class GetBeerByIdPositiveTest {
    String beerId;
    GetBeerResponseDTO beerEntity;
    BeerQuery beerQuery;
    BeerRequest beerRequest;

    public GetBeerByIdPositiveTest(BeerQuery beerQuery, BeerRequest beerRequest) {
        this.beerQuery = beerQuery;
        this.beerRequest = beerRequest;
    }

    @BeforeEach
    void createBeerEntityInDB() {
        beerEntity = beerQuery.addRandomBeerReturnDTO();
        beerId = String.valueOf(beerEntity.beerId());
    }

    @DisplayName("Ensure Response Code is 200 for GET /beer/{id}")
    @Test
    void checkBeerByIdResponseCode() {
        Response response = beerRequest.getBeerByIdRequestReturnResponse(beerId);
        Assertions.assertEquals(SC_OK, response.getStatusCode());
    }

    @DisplayName("Validate Response JSON Structure for GET /beer/{id}")
    @Test
    void checkBeerByIdResponseStructure() {
        Response response = beerRequest.getBeerByIdRequestReturnResponse(beerId);
        ResponseValidator.assertResponseMatchesSchema(response, BEER_OBJECT.getPath());
    }

    @DisplayName("Check Data Matching: GET /beer/{id} Response vs Database")
    @Test
    void checkValuesInBeerByIdResponse() {
        GetBeerResponse responseObject = beerRequest.getBeerByIdRequest(beerId);
        Assertions.assertAll(
                () -> Assertions.assertEquals(beerEntity.abv(), responseObject.abv()),
                () -> Assertions.assertEquals(beerEntity.name(), responseObject.name()),
                () -> Assertions.assertEquals(beerEntity.ibuNumber(), responseObject.ibuNumber()),
                () -> Assertions.assertEquals(beerEntity.name(), responseObject.name()),
                () -> Assertions.assertEquals(beerEntity.style(), responseObject.style()),
                () -> Assertions.assertEquals(beerEntity.breweryId(), responseObject.breweryId()),
                () -> Assertions.assertEquals(beerEntity.ounces(), responseObject.ounces())
        );
    }
}