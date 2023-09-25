package api.beer_service;

import api.db_query.BeerQuery;
import api.request.BeerRequest;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.beerinfo.utils.HibernateUtil.getSessionFactory;

@Story("Beer API")
public class GetBeerByIdNegativeTest {
    BeerRequest beerRequest = new BeerRequest();
    BeerQuery beerQuery = new BeerQuery(getSessionFactory());

    @DisplayName("Error: Retrieve Nonexistent Beer by ID")
    @Test
    void checkBeerByIdWrongIdResponseMessage() {
        long lastBeerId = beerQuery.getLastBeerId();
        Response response = beerRequest.getBeerByIdRequestReturnResponse(String.valueOf(lastBeerId + 1));

        Assertions.assertEquals(SC_NOT_FOUND, response.getStatusCode());

        String actualResponse = response.body().jsonPath().get("error");
        String expectedResponse = String.format("Beer with id: %s not found.", lastBeerId + 1);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Error: Retrieve Beer with Invalid ID Format")
    @Test
    void checkBeerByIdWrongFormatIdResponseMessage() {
        Response response = beerRequest.getBeerByIdRequestReturnResponse("a");

        Assertions.assertEquals(SC_BAD_REQUEST, response.getStatusCode());

        String actualResponse = response.body().jsonPath().get("error");
        String expectedResponse = "Invalid Beer ID format. Only numeric values are allowed.";

        Assertions.assertEquals(expectedResponse, actualResponse);
    }
}