package api.request;

import api.api_specifications.ApiRequestSpecification;
import api.pojo.request.BreweryRequestPojo;
import api.pojo.response.brewery.UpdateBreweryResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BreweryRequest {
    @Step("Request to get all brewery records GET /breweries")
    public List<?> getBreweriesRequest() {
        Response response = given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .basePath("/breweries")
                .when()
                .get()
                .thenReturn();

        return response.jsonPath().getList("");
    }

    @Step("Request get all brewery records GET /breweries")
    public Response getBreweriesRequestReturnResponse() {
        return given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .basePath("/breweries")
                .when()
                .get()
                .thenReturn();
    }

    @Step("Request update brewery record PUT /brewery/{breweryId}")
    public UpdateBreweryResponse updateBreweryRequest(BreweryRequestPojo breweryObject, String breweryId) {
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
    public Response updateBreweryReturnResponse(BreweryRequestPojo breweryObject, String breweryId) {
        return given()
                .spec(ApiRequestSpecification.putRequestSpecification())
                .queryParam("breweryId", breweryId)
                .body(breweryObject)
                .basePath("/brewery")
                .when()
                .put("").thenReturn();
    }
}