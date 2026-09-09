package api.test_utils.data_generators;

import api.test_data.request.BreweryRequest;
import io.qameta.allure.Step;
import org.beerinfo.data.entity.BreweryEntity;

import static api.test_utils.RandomValueUtils.*;

public class BreweryObjectGenerator {

    @Step("Generate Beer object with random properties")
    public static BreweryEntity generateRandomBreweryEntity() {
        return BreweryEntity.builder()
                .name(randomBeerName())
                .city(randomCity())
                .state(randomState())
                .country(randomValidCountry())
                .build();
    }

    @Step("Generate Brewery object with random properties")
    public static BreweryRequest generateRandomBreweryRequest() {
        return BreweryRequest.builder()
                .name(randomBeerName())
                .city(randomCity())
                .state(randomState())
                .country(randomValidCountry())
                .build();
    }
}