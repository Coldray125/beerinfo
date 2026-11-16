package api.test_utils.data_generators;

import api.test_data.request.BeerRequest;
import api.test_utils.RandomValueUtils;
import io.qameta.allure.Step;
import org.beerinfo.data.entity.BeerEntity;

import java.math.RoundingMode;

import static api.test_utils.RandomValueUtils.*;

public class BeerObjectGenerator {

    @Step("Generate Beer object with random properties")
    public static BeerEntity generateRandomBeerEntity() {
        return BeerEntity.builder()
                .abv(RandomValueUtils.randomPositiveBigDecimal(30, 150)
                        .movePointLeft(3)
                        .setScale(3, RoundingMode.HALF_UP))
                .ibuNumber(randomPositiveInt(0, 120))
                .name(randomBeerName())
                .style(randomBeerStyle())
                .breweryId(randomPositiveLong(1, 300))
                .ounces(RandomValueUtils.randomPositiveBigDecimal(8, 32))
                .build();
    }

    @Step("Generate Beer object with random properties")
    public static BeerRequest generateRandomBeerRequest() {
        return BeerRequest.builder()
                .abv(RandomValueUtils.randomPositiveBigDecimal(30, 150)
                        .movePointLeft(3)
                        .setScale(3, RoundingMode.HALF_UP))
                .ibuNumber(randomPositiveInt(0, 120))
                .name(randomBeerName())
                .style(randomBeerStyle())
                .breweryId(randomPositiveLong(1, 300))
                .ounces(RandomValueUtils.randomPositiveBigDecimal(8, 32))
                .build();
    }
}