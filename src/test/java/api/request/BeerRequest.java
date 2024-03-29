package api.request;

import api.api_specifications.ApiRequestSpecification;
import api.pojo.request.BeerRequestPojo;
import api.pojo.response.beer.AddBeerResponse;
import api.pojo.response.beer.GetBeerResponse;
import api.pojo.response.beer.UpdateBeerResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BeerRequest {
    @Step("Request to get all beer records GET /beers")
    public List<GetBeerResponse> getBeerRequestReturnList() {
        Response response = given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .basePath("/beers")
                .when()
                .get()
                .thenReturn();
        return response.jsonPath().getList("", GetBeerResponse.class);
    }

    @Step("Request to get all beer records GET /beers")
    public Response getBeerRequestReturnResponse() {
        return given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .basePath("/beers")
                .when()
                .get()
                .thenReturn();
    }

    @Step("Request to get beer record by id GET /beer/(beerId)")
    public GetBeerResponse getBeerByIdRequest(String beerId) {
        Response response = given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .queryParam("beerId", beerId)
                .basePath("/beer")
                .when()
                .get()
                .thenReturn();

        return response.as(GetBeerResponse.class);
    }

    @Step("Request to get beer record by id GET /beer/(beerId)")
    public Response getBeerByIdRequestReturnResponse(String beerId) {
        return given()
                .spec(ApiRequestSpecification.getRequestSpecification())
                .queryParam("beerId", beerId)
                .basePath("/beer")
                .when()
                .get()
                .thenReturn();
    }

    @Step("Request to add a new beer record POST /beer")
    public Response addBeerRequestReturnResponse(BeerRequestPojo beerObject) {
        return given()
                .spec(ApiRequestSpecification.postRequestSpecification())
                .body(beerObject)
                .basePath("/beer")
                .when()
                .post()
                .thenReturn();
    }

    @Step("Request to add a new beer record POST /beer")
    public AddBeerResponse addBeerRequest(BeerRequestPojo beerObject) {
        Response response = given()
                .spec(ApiRequestSpecification.postRequestSpecification())
                .body(beerObject)
                .basePath("/beer")
                .when()
                .post()
                .thenReturn();

        return response.as(AddBeerResponse.class);
    }

    @Step("Request to update beer record PUT /beer/(beerId)")
    public UpdateBeerResponse updateBeerRequest(BeerRequestPojo beerObject, String idNumber) {
        Response response = given()
                .spec(ApiRequestSpecification.putRequestSpecification())
                .queryParams("beerId", idNumber)
                .body(beerObject)
                .basePath("/beer")
                .when()
                .put()
                .thenReturn();
        return response.as(UpdateBeerResponse.class);
    }

    @Step("Request to delete beer record DELETE /beer/(beerId)")
    public Response deleteBeerRequestReturnResponse(String beerId) {
        return given()
                .spec(ApiRequestSpecification.deleteRequestSpecification())
                .queryParam("beerId", beerId)
                .basePath("/beer")
                .when()
                .delete()
                .thenReturn();
    }
}