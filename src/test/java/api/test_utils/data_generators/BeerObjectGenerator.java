package api.test_utils.data_generators;

import api.test_data.request.BeerRequest;
import io.qameta.allure.Step;
import org.beerinfo.data.entity.BeerEntity;

import static api.test_utils.RandomValueUtils.*;

public class BeerObjectGenerator {

    @Step("Generate Beer object with random properties")
    public static BeerEntity generateRandomBeerEntity() {
        return BeerEntity.builder()
                .abv(randomStringDigits(2))
                .ibuNumber(randomStringDigits(2))
                .name(randomBeerName())
                .style(randomBeerStyle())
                .breweryId(randomPositiveLong(1, 300))
                .ounces(randomStringDigits(2))
                .build();
    }

    @Step("Generate Beer object with random properties")
    public static BeerRequest generateRandomBeerRequest() {
        return BeerRequest.builder()
                .abv(randomStringDigits(2))
                .ibuNumber(randomStringDigits(2))
                .name(randomBeerName())
                .style(randomBeerStyle())
                .breweryId(randomPositiveLong(1, 300))
                .ounces(randomStringDigits(2))
                .build();
    }
}