package api.beer_service;

import api.db_query.BeerQuery;
import api.extensions.LoggingExtension;
import api.extensions.annotation.beer.RandomBeerPojo;
import api.extensions.annotation.beer.RandomBeerRequestPojoExtension;
import api.extensions.resolver.BeerQueryParameterResolver;
import api.extensions.resolver.BeerRequestParameterResolver;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.AddBeerResponse;
import api.request.BeerRequest;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static api.test_utils.SchemaPaths.ADD_BEER_RESPONSE;
import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer_API")
@Tag("Beer_API")
@ExtendWith({LoggingExtension.class})
@ExtendWith({BeerQueryParameterResolver.class, BeerRequestParameterResolver.class, RandomBeerRequestPojoExtension.class})
public class AddBeerPositiveTest {
    BeerQuery beerQuery;
    BeerRequest beerRequest;

    public AddBeerPositiveTest(BeerQuery beerQuery, BeerRequest beerRequest) {
        this.beerQuery = beerQuery;
        this.beerRequest = beerRequest;
    }

    @RandomBeerPojo
    BeerRequestPojo request;

    @DisplayName("Verify Data in POST /beer Response and Database")
    @Test
    void checkAddBeerWriteInDatabase() {
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
    void checkAddBeerResponseText() {
        Response response = beerRequest.addBeerRequestReturnResponse(request);

        Assertions.assertEquals(SC_OK, response.getStatusCode());

        String responseText = response.body().path("message");
        Assertions.assertEquals("Beer added successfully.", responseText);
    }

    @DisplayName("Validate POST /beer Response JSON Structure")
    @Test
    void checkAddBeerResponseStructure() {
        Response response = beerRequest.addBeerRequestReturnResponse(request);
        ResponseValidator.assertResponseMatchesSchema(response, ADD_BEER_RESPONSE.getPath());
    }

    @DisplayName("Verify Data in POST /beer Response and Request")
    @Test
    void checkValuesAddBeerResponse() {
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