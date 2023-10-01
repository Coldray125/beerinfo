package api.beer_service;

import api.db_query.BeerQuery;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.AddBeerResponse;
import api.request.BeerRequest;
import api.test_utils.ResponseValidator;
import api.test_utils.data_generators.BeerObjectGenerator;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static api.test_utils.SchemaPaths.ADD_BEER_RESPONSE;
import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.SC_OK;
import static org.beerinfo.utils.HibernateUtil.getSessionFactory;

@Story("Beer API")
public class AddBeerPositiveTest {

    BeerRequest beerRequest = new BeerRequest();
    BeerRequestPojo beerRequestPojo;
    BeerQuery beerQuery = new BeerQuery(getSessionFactory());

    @BeforeEach
    void createBeerEntityInDB() {
        beerRequestPojo = BeerObjectGenerator.generateRandomBeerPojo();
    }

    @DisplayName("Verify Data in POST /beer Response and Database")
    @Test
    void checkAddBeerWriteInDatabase() {
        AddBeerResponse fullResponse = beerRequest.addBeerRequest(beerRequestPojo);
        AddBeerResponse.BeerDetails responseObject = fullResponse.beer();
        GetBeerResponseDTO beerEntity = beerQuery.getBeerById(responseObject.beerId());

        step("Validate response JSON against database values");
        Assertions.assertAll(
                () -> Assertions.assertEquals(responseObject.beerId(), beerEntity.beerId()),
                () -> Assertions.assertEquals(responseObject.abv(), beerEntity.abv()),
                () -> Assertions.assertEquals(responseObject.name(), beerEntity.name()),
                () -> Assertions.assertEquals(responseObject.ibuNumber(), beerEntity.ibuNumber()),
                () -> Assertions.assertEquals(responseObject.style(), beerEntity.style()),
                () -> Assertions.assertEquals(responseObject.breweryId(), beerEntity.breweryId()),
                () -> Assertions.assertEquals(responseObject.ounces(), beerEntity.ounces())
        );
    }

    @DisplayName("Ensure POST /beer Response Message")
    @Test
    void checkAddBeerResponseText() {
        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);

        Assertions.assertEquals(SC_OK, response.getStatusCode());

        String responseText = response.body().path("message");
        Assertions.assertEquals("Beer added successfully.", responseText);
    }

    @DisplayName("Validate POST /beer Response JSON Structure")
    @Test
    void checkAddBeerResponseStructure() {
        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);
        ResponseValidator.assertResponseMatchesSchema(response, ADD_BEER_RESPONSE.getPath());
    }

    @DisplayName("Verify Data in POST /beer Response and Request")
    @Test
    void checkValuesAddBeerResponse() {
        AddBeerResponse fullResponse = beerRequest.addBeerRequest(beerRequestPojo);
        AddBeerResponse.BeerDetails responseObject = fullResponse.beer();
        Assertions.assertAll(
                () -> Assertions.assertEquals(beerRequestPojo.getAbv(), responseObject.abv()),
                () -> Assertions.assertEquals(beerRequestPojo.getName(), responseObject.name()),
                () -> Assertions.assertEquals(beerRequestPojo.getIbuNumber(), responseObject.ibuNumber()),
                () -> Assertions.assertEquals(beerRequestPojo.getStyle(), responseObject.style()),
                () -> Assertions.assertEquals(beerRequestPojo.getBreweryId(), responseObject.breweryId()),
                () -> Assertions.assertEquals(beerRequestPojo.getOunces(), responseObject.ounces()));
    }
}