package api.request;

import api.api_specifications.ApiRequestSpecification;
import api.pojo.request.BeerRequestPojo;
import api.pojo.request.BreweryRequestPojo;
import api.pojo.response.brewery.UpdateBreweryResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.beerinfo.dto.api.beer.PutBeerResponseDTO;

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

    @Step("Request update brewery record PUT /brewery/{id}")
    public UpdateBreweryResponse updateBreweryRequest(BreweryRequestPojo breweryObject, String idNumber) {
        Response response = given()
                .spec(ApiRequestSpecification.putRequestSpecification())
                .pathParams("id", idNumber)
                .body(breweryObject)
                .basePath("/brewery")
                .when()
                .put("/{id}").thenReturn();
        return response.as(UpdateBreweryResponse.class);
    }

    @Step("Request update brewery record PUT /brewery/{id}")
    public Response updateBreweryReturnResponse(BreweryRequestPojo breweryObject, String idNumber) {
        return given()
                .spec(ApiRequestSpecification.putRequestSpecification())
                .pathParams("id", idNumber)
                .body(breweryObject)
                .basePath("/brewery")
                .when()
                .put("/{id}").thenReturn();
    }
}