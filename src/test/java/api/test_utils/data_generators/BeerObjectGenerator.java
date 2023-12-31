package api.test_utils.data_generators;

import api.pojo.request.BeerRequestPojo;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.beerinfo.entity.BeerEntity;
import org.junit.jupiter.api.Test;

public class BeerObjectGenerator {
    static Faker faker = new Faker();
    @Step("Generate Beer object with random properties")
    public static BeerEntity generateRandomBeerEntity() {
        return BeerEntity.builder()
                .abv(faker.number().digits(2))
                .ibuNumber(faker.number().digits(2))
                .name(faker.beer().name())
                .style(faker.beer().style())
                .breweryId(faker.number().numberBetween(1, 300))
                .ounces(faker.number().digits(2))
                .build();
    }

    @Step("Generate Beer object with random properties")
    public static BeerRequestPojo generateRandomBeerPojo() {
        return BeerRequestPojo.builder()
                .abv(faker.number().digits(2))
                .ibuNumber(faker.number().digits(2))
                .name(faker.beer().name())
                .style(faker.beer().style())
                .breweryId(faker.number().numberBetween(1, 300))
                .ounces(faker.number().digits(2))
                .build();
    }
}