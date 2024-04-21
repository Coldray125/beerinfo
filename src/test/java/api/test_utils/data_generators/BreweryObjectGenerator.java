package api.test_utils.data_generators;

import api.pojo.request.BreweryRequestPojo;
import io.qameta.allure.Step;
import org.beerinfo.entity.BreweryEntity;

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
    public static BreweryRequestPojo generateRandomBreweryPojo() {
        return BreweryRequestPojo.builder()
                .name(randomBeerName())
                .city(randomCity())
                .state(randomState())
                .country(randomValidCountry())
                .build();
    }
}