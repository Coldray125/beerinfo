package api.api;

import api.api_specifications.ApiRequestSpecification;
import api.test_data.request.BeerRequest;
import api.test_data.response.beer.AddBeerResponse;
import api.test_data.response.beer.GetBeerResponse;
import api.test_data.response.beer.UpdateBeerResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public final class BeerApiRequests {
    @Step("Request to get all beer records GET /beers")
    public static List<GetBeerResponse> getBeerRequestReturnList() {
        var response = given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .basePath("/beers")
                .when()
                .get()
                .thenReturn();
        return response.jsonPath().getList("", GetBeerResponse.class);
    }

    @Step("Request to get all beer records GET /beers")
    public static Response getBeerRequestReturnResponse() {
        return given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .basePath("/beers")
                .when()
                .get()
                .thenReturn();
    }

    @Step("Request to get beer record by id GET /beer/(beerId)")
    public static GetBeerResponse getBeerByIdRequest(String beerId) {
        return given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .queryParam("beerId", beerId)
                .basePath("/beer")
                .when()
                .get()
                .thenReturn()
                .as(GetBeerResponse.class);
    }

    @Step("Request to get beer record by id GET /beer/(beerId)")
    public static Response getBeerByIdRequestReturnResponse(String beerId) {
        return given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .queryParam("beerId", beerId)
                .basePath("/beer")
                .when()
                .get()
                .thenReturn();
    }

    @Step("Request to add a new beer record POST /beer")
    public static Response addBeerRequestReturnResponse(BeerRequest beerObject) {
        return given()
                .spec(ApiRequestSpecification.postRequestSpecification())
                .body(beerObject)
                .basePath("/beer")
                .when()
                .post()
                .thenReturn();
    }

    @Step("Request to add a new beer record POST /beer")
    public static AddBeerResponse addBeerRequest(BeerRequest beerObject) {
        return given()
                .spec(ApiRequestSpecification.postRequestSpecification())
                .body(beerObject)
                .basePath("/beer")
                .when()
                .post()
                .thenReturn()
                .as(AddBeerResponse.class);
    }

    @Step("Request to update beer record PUT /beer/(beerId)")
    public static UpdateBeerResponse updateBeerRequest(BeerRequest beerObject, String idNumber) {
        return given()
                .spec(ApiRequestSpecification.putRequestSpecification())
                .queryParams("beerId", idNumber)
                .body(beerObject)
                .basePath("/beer")
                .when()
                .put()
                .thenReturn()
                .as(UpdateBeerResponse.class);
    }

    @Step("Request to delete beer record DELETE /beer/(beerId)")
    public static Response deleteBeerRequestReturnResponse(String beerId) {
        return given()
                .spec(ApiRequestSpecification.deleteRequestSpecification())
                .queryParam("beerId", beerId)
                .basePath("/beer")
                .when()
                .delete()
                .thenReturn();
    }
}