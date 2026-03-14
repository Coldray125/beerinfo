package api.beer_service_test;

import api.db_query.BeerQuery;
import api.pojo.response.beer.GetBeerResponse;
import api.request.BeerRequest;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.*;

import static api.test_utils.SchemaPaths.BEER_OBJECT;
import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer_API")
@Tag("Beer_API")
public class GetBeerByIdPositiveTest {

    private String beerId;
    private GetBeerResponseDTO beerEntity;

    @BeforeEach
    void createBeerEntityInDB() {
        beerEntity = BeerQuery.addRandomBeerReturnDTO();
        beerId = String.valueOf(beerEntity.beerId());
    }

    @DisplayName("Verify response code is 200 for GET /beer/{id}")
    @Test
    void checkBeerByIdResponseCode() {
        Response response = BeerRequest.getBeerByIdRequestReturnResponse(beerId);

        Allure.step("Check response status code", () ->
                Assertions.assertEquals(SC_OK, response.getStatusCode()));
    }

    @DisplayName("Validate response JSON structure for GET /beer/{id}")
    @Test
    void checkBeerByIdResponseStructure() {
        Response response = BeerRequest.getBeerByIdRequestReturnResponse(beerId);

        Allure.step("Validate response JSON structure", () ->
                ResponseValidator.assertResponseMatchesSchema(response, BEER_OBJECT.getPath()));
    }

    @DisplayName("Verify response data against database for GET /beer/{id} ")
    @Test
    void checkValuesInBeerByIdResponse() {
        GetBeerResponse responseObject = BeerRequest.getBeerByIdRequest(beerId);

        Allure.step("Verify response fields against database values", () -> Assertions.assertAll(
                () -> Assertions.assertEquals(beerEntity.abv(), responseObject.abv()),
                () -> Assertions.assertEquals(beerEntity.name(), responseObject.name()),
                () -> Assertions.assertEquals(beerEntity.ibuNumber(), responseObject.ibuNumber()),
                () -> Assertions.assertEquals(beerEntity.name(), responseObject.name()),
                () -> Assertions.assertEquals(beerEntity.style(), responseObject.style()),
                () -> Assertions.assertEquals(beerEntity.breweryId(), responseObject.breweryId()),
                () -> Assertions.assertEquals(beerEntity.ounces(), responseObject.ounces())
        ));
    }
}