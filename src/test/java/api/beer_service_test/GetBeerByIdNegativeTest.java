package api.beer_service_test;

import api.db_query.BeerQuery;
import api.request.BeerRequest;
import api.test_utils.RandomValueUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

@Story("Beer_API")
@Tag("Beer_API")
class GetBeerByIdNegativeTest {

    @DisplayName("Error: Retrieve Nonexistent Beer by ID in GET /beer/(beerId)")
    @Test
    void checkBeerByIdWrongIdResponseMessage() {
        long lastBeerId = BeerQuery.getLastBeerId() + 1000;
        var response = BeerRequest.getBeerByIdRequestReturnResponse(String.valueOf(lastBeerId));

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_NOT_FOUND));

        Allure.step("Check error message for nonexistent ID", () -> {
            String actualResponse = response.body().jsonPath().get("error");
            String expectedResponse = "Beer with id: " + (lastBeerId) + " not found";
            assertThat(actualResponse).isEqualTo(expectedResponse);
        });
    }

    static private Stream<Arguments> nonValidIdProvider() {
        return Stream.of(
                Arguments.of("", "Missing 'beerId' query parameter"),
                Arguments.of("null", "Invalid Beer ID format. Only numeric values are allowed."),
                Arguments.of(RandomValueUtils.randomBeerName(), "Invalid Beer ID format. Only numeric values are allowed."));
    }

    @DisplayName("Error: Retrieve Beer with Invalid ID Format in GET /beer/(beerId)")
    @ParameterizedTest
    @MethodSource("nonValidIdProvider")
    void checkBeerByIdWrongFormatIdResponseMessage(String beerId, String expectedResponse) {
        Response response = BeerRequest.getBeerByIdRequestReturnResponse(beerId);

        Allure.step("Check response status code", () ->
                assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST));

        Allure.step("Check error message for invalid ID format", () -> {
            String actualResponse = response.body().jsonPath().get("error");
            assertThat(actualResponse).isEqualTo(expectedResponse);
        });
    }
}