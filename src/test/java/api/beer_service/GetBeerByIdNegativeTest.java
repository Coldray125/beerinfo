package api.beer_service;

import api.db_query.BeerQuery;
import api.extensions.LoggingExtension;
import api.extensions.resolver.BeerQueryParameterResolver;
import api.extensions.resolver.BeerRequestParameterResolver;
import api.request.BeerRequest;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;

@Story("Beer API")
@Tag("Beer API")
@ExtendWith({LoggingExtension.class})
@ExtendWith({BeerQueryParameterResolver.class, BeerRequestParameterResolver.class})
public class GetBeerByIdNegativeTest {
    BeerQuery beerQuery;
    BeerRequest beerRequest;

    public GetBeerByIdNegativeTest(BeerQuery beerQuery, BeerRequest beerRequest) {
        this.beerQuery = beerQuery;
        this.beerRequest = beerRequest;
    }

    @DisplayName("Error: Retrieve Nonexistent Beer by ID")
    @Test
    void checkBeerByIdWrongIdResponseMessage() {
        long lastBeerId = beerQuery.getLastBeerId();
        Response response = beerRequest.getBeerByIdRequestReturnResponse(String.valueOf(lastBeerId + 1));

        Assertions.assertEquals(SC_NOT_FOUND, response.getStatusCode());

        String actualResponse = response.body().jsonPath().get("error");
        String expectedResponse = STR."Beer with id: \{lastBeerId + 1} not found";

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Error: Retrieve Beer with Invalid ID Format")
    @Test
    void checkBeerByIdWrongFormatIdResponseMessage() {
        String beerId = RandomStringUtils.randomAlphabetic(1);
        Response response = beerRequest.getBeerByIdRequestReturnResponse(beerId);

        Assertions.assertEquals(SC_NOT_FOUND, response.getStatusCode());

        String actualResponse = response.body().jsonPath().get("error");
        String expectedResponse = STR."Beer with id: \{beerId} not found";

        Assertions.assertEquals(expectedResponse, actualResponse);
    }
}