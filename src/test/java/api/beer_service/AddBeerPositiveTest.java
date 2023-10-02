package api.beer_service;

import api.db_query.BeerQuery;
import api.extensions.BeerQueryParameterResolver;
import api.extensions.BeerRequestParameterResolver;
import api.extensions.annotation.beer.RandomBeerPojo;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.AddBeerResponse;
import api.request.BeerRequest;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static api.test_utils.SchemaPaths.ADD_BEER_RESPONSE;
import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer API")
@ExtendWith({BeerQueryParameterResolver.class, BeerRequestParameterResolver.class})
public class AddBeerPositiveTest {
    BeerQuery beerQuery;
    BeerRequest beerRequest;

    public AddBeerPositiveTest(BeerQuery beerQuery, BeerRequest beerRequest) {
        this.beerQuery = beerQuery;
        this.beerRequest = beerRequest;
    }

    @DisplayName("Verify Data in POST /beer Response and Database")
    @Test
    void checkAddBeerWriteInDatabase(@RandomBeerPojo BeerRequestPojo request) {
        AddBeerResponse fullResponse = beerRequest.addBeerRequest(request);
        AddBeerResponse.BeerDetails response = fullResponse.beer();
        GetBeerResponseDTO beerEntity = beerQuery.getBeerById(response.beerId());

        step("Validate response JSON against database values");
        Assertions.assertAll(
                () -> Assertions.assertEquals(response.beerId(), beerEntity.beerId()),
                () -> Assertions.assertEquals(response.abv(), beerEntity.abv()),
                () -> Assertions.assertEquals(response.name(), beerEntity.name()),
                () -> Assertions.assertEquals(response.ibuNumber(), beerEntity.ibuNumber()),
                () -> Assertions.assertEquals(response.style(), beerEntity.style()),
                () -> Assertions.assertEquals(response.breweryId(), beerEntity.breweryId()),
                () -> Assertions.assertEquals(response.ounces(), beerEntity.ounces())
        );
    }

    @DisplayName("Ensure POST /beer Response Message")
    @Test
    void checkAddBeerResponseText(@RandomBeerPojo BeerRequestPojo beerRequestPojo) {
        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);

        Assertions.assertEquals(SC_OK, response.getStatusCode());

        String responseText = response.body().path("message");
        Assertions.assertEquals("Beer added successfully.", responseText);
    }

    @DisplayName("Validate POST /beer Response JSON Structure")
    @Test
    void checkAddBeerResponseStructure(@RandomBeerPojo BeerRequestPojo beerRequestPojo) {
        Response response = beerRequest.addBeerRequestReturnResponse(beerRequestPojo);
        ResponseValidator.assertResponseMatchesSchema(response, ADD_BEER_RESPONSE.getPath());
    }

    @DisplayName("Verify Data in POST /beer Response and Request")
    @Test
    void checkValuesAddBeerResponse(@RandomBeerPojo BeerRequestPojo request) {
        AddBeerResponse fullResponse = beerRequest.addBeerRequest(request);
        AddBeerResponse.BeerDetails responseObject = fullResponse.beer();
        Assertions.assertAll(
                () -> Assertions.assertEquals(request.getAbv(), responseObject.abv()),
                () -> Assertions.assertEquals(request.getName(), responseObject.name()),
                () -> Assertions.assertEquals(request.getIbuNumber(), responseObject.ibuNumber()),
                () -> Assertions.assertEquals(request.getStyle(), responseObject.style()),
                () -> Assertions.assertEquals(request.getBreweryId(), responseObject.breweryId()),
                () -> Assertions.assertEquals(request.getOunces(), responseObject.ounces()));
    }
}