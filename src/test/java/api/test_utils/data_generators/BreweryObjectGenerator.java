package api.test_utils.data_generators;

import api.pojo.request.BreweryRequestPojo;
import io.qameta.allure.Step;
import net.datafaker.Faker;
import org.beerinfo.entity.BreweryEntity;
import org.beerinfo.enums.SupportedCountry;

import java.util.List;

public class BreweryObjectGenerator {
    static Faker faker = new Faker();

     private static final List<String> countryList = SupportedCountry.getAllCountryNames();
    @Step("Generate Beer object with random properties")
    public static BreweryEntity generateRandomBreweryEntity() {
        return BreweryEntity.builder()
                .name(faker.beer().name())
                .city(faker.address().city())
                .state(faker.address().state())
                .country(countryList.get(faker.random().nextInt(countryList.size())))
                .build();
    }

    @Step("Generate Beer object with random properties")
    public static BreweryRequestPojo generateRandomBreweryPojo() {
        return BreweryRequestPojo.builder()
                .name(faker.beer().name())
                .city(faker.address().city())
                .state(faker.address().state())
                .country(countryList.get(faker.random().nextInt(countryList.size())))
                .build();
    }
}