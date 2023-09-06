package org.beerinfo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum SupportedCountry {
    GERMANY("Germany"),
    SPAIN("Spain"),
    UNITED_KINGDOM("United Kingdom"),
    UNITED_STATES("United States of America"),
    BELGIUM("Belgium"),
    NETHERLANDS("Netherlands"),
    FRANCE("France"),
    CZECH_REPUBLIC("Czech Republic"),
    POLAND("Poland");

    private final String countryName;

    public static List<String> getAllCountryNames() {
        List<String> countryNames = new ArrayList<>();
        for (SupportedCountry supportedCountry : values()) {
            countryNames.add(supportedCountry.getCountryName());
        }
        return countryNames;
    }

    public static boolean isValidCountry(String country) {
        for (SupportedCountry supportedCountry : values()) {
            if (supportedCountry.getCountryName().equalsIgnoreCase(country)) {
                return true;
            }
        }
        return false;
    }
}