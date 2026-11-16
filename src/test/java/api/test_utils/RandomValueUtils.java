package api.test_utils;

import net.datafaker.Faker;
import org.beerinfo.enums.SupportedCountry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

public final class RandomValueUtils {

    static private final Faker faker = new Faker(Locale.ENGLISH);

    public static String randomBeerName() {
        return faker.beer().name();
    }

    public static String randomBeerStyle() {
        return faker.beer().style();
    }

    public static String randomCity() {
        return faker.address().city();
    }

    public static String randomState() {
        return faker.address().state();
    }

    public static String randomValidCountry() {
        List<String> countryList = SupportedCountry.getAllCountryNames();
       return countryList.get(faker.random().nextInt(countryList.size()));
    }

    public static String randomStringDigits(int amount) {
        return faker.number().digits(amount);
    }

    public static int randomPositiveInt(int min, int max) {
        return faker.number().numberBetween(min, max);
    }

    public static long randomPositiveLong(long from, long to) {
        return faker.number().numberBetween(from, to);
    }

    public static BigDecimal randomPositiveBigDecimal(int min, int max) {
        double randomDouble = faker.number().randomDouble(2, min, max);
        BigDecimal decimal = BigDecimal.valueOf(randomDouble)
                .setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros();
        return new BigDecimal(decimal.toPlainString());
    }

    public static long randomNegativeLong(long from, long to) {
        long randomLong = faker.number().numberBetween(from, to);
        return -Math.abs(randomLong);
    }
}