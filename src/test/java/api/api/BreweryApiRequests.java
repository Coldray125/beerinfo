package api.api;

import api.api_specifications.ApiRequestSpecification;
import api.test_data.request.BreweryRequest;
import api.test_data.response.brewery.UpdateBreweryResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BreweryApiRequests {
    @Step("Request to get all brewery records GET /breweries")
    public static List<?> getBreweriesRequest() {
        Response response = given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .basePath("/breweries")
                .when()
                .get()
                .thenReturn();

        return response.jsonPath().getList("");
    }

    @Step("Request get all brewery records GET /breweries")
    public static Response getBreweriesRequestReturnResponse() {
        return given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .basePath("/breweries")
                .when()
                .get()
                .thenReturn();
    }

    @Step("Request update brewery record PUT /brewery/{breweryId}")
    public static UpdateBreweryResponse updateBreweryRequest(BreweryRequest breweryObject, String breweryId) {
        Response response = given()
                .spec(ApiRequestSpecification.putRequestSpecification())
                .queryParams("breweryId", breweryId)
                .body(breweryObject)
                .basePath("/brewery")
                .when()
                .put()
                .thenReturn();
        return response.as(UpdateBreweryResponse.class);
    }

    @Step("Request update brewery record PUT /brewery/{breweryId}")
    public static Response updateBreweryReturnResponse(BreweryRequest breweryObject, String breweryId) {
        return given()
                .spec(ApiRequestSpecification.putRequestSpecification())
                .queryParam("breweryId", breweryId)
                .body(breweryObject)
                .basePath("/brewery")
                .when()
                .put("").thenReturn();
    }
}