package api.test_suites;

import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("BeerInfo Suite")
@SelectPackages({"api.beer_service_test", "api.breweries_service_test"})
@IncludeClassNamePatterns(".*Test")
public class RunAllTestsSuite {
    @BeforeSuite
    static void beforeSuite() {
    }

    @AfterSuite
    static void afterSuite() {
    }
}