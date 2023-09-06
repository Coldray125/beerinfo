package api.test_utils;

import lombok.Getter;

@Getter
public enum SchemaPaths {
    BEER_OBJECT("schemas/beer_service/beerObjectSchema.json"),
    BEER_ARRAY("schemas/beer_service/beerArrayOfObjectsSchema.json"),
    ADD_BEER_RESPONSE("schemas/beer_service/addBeerResponseObject.json");

    private final String path;

    SchemaPaths(String path) {
        this.path = path;
    }
}