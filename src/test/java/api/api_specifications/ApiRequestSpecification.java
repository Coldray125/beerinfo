package api.api_specifications;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static org.beerinfo.config.PropertyUtil.getProperty;

public class ApiRequestSpecification {
    private static final String BASE_URL = getProperty("base.url");

    private static final RestAssuredConfig config = RestAssuredConfig.config()
            .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())
            .httpClient(httpClientConfig()
                    .setParam("http.connection.timeout", 3000)
                    .setParam("http.socket.timeout", 3000));

    public static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setConfig(config)
                .setBaseUri(BASE_URL)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public static RequestSpecification postRequestSpecification() {
        return new RequestSpecBuilder()
                .setConfig(config)
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public static RequestSpecification putRequestSpecification() {
        return new RequestSpecBuilder()
                .setConfig(config)
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public static RequestSpecification deleteRequestSpecification() {
        return new RequestSpecBuilder()
                .setConfig(config)
                .setBaseUri(BASE_URL)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}