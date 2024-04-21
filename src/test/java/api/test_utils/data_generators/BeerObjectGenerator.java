package api.test_utils.data_generators;

import api.pojo.request.BeerRequestPojo;
import io.qameta.allure.Step;
import net.datafaker.Faker;
import org.beerinfo.entity.BeerEntity;

public class BeerObjectGenerator {
    static Faker faker = new Faker();

    @Step("Generate Beer object with random properties")
    public static BeerEntity generateRandomBeerEntity() {
        return BeerEntity.builder()
                .abv(faker.number().digits(2))
                .ibuNumber(faker.number().digits(2))
                .name(faker.beer().name())
                .style(faker.beer().style())
                .breweryId(faker.number().numberBetween(1L, 300L))
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
                .breweryId(faker.number().numberBetween(1L, 300L))
                .ounces(faker.number().digits(2))
                .build();
    }
}