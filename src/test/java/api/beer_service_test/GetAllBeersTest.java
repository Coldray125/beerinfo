package api.beer_service_test;

import api.conversion.BeerConverter;
import api.db_query.BeerQuery;
import api.extensions.LoggingExtension;
import api.extensions.resolver.GenericHttpRequestResolver;
import api.extensions.resolver.GenericQueryResolver;
import api.pojo.response.beer.GetBeerResponse;
import api.request.BeerRequest;
import api.test_utils.ResponseValidator;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.data.dto.api.beer.GetBeerResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static api.test_utils.SchemaPaths.BEER_ARRAY;
import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer_API")
@Tag("Beer_API")
@ExtendWith({LoggingExtension.class})
@ExtendWith({GenericQueryResolver.class, GenericHttpRequestResolver.class})
public class GetAllBeersTest {

    private final BeerQuery beerQuery;
    private final BeerRequest beerRequest;

    public GetAllBeersTest(BeerQuery beerQuery, BeerRequest beerRequest) {
        this.beerQuery = beerQuery;
        this.beerRequest = beerRequest;
    }

    @DisplayName("Verify GET /beers response contains record added to Postgres")
    @Test
    void checkGetAllBeersContainsAddedRecord() {
        GetBeerResponseDTO entityDTO = beerQuery.addRandomBeerReturnDTO();
        List<GetBeerResponse> responseList = beerRequest.getBeerRequestReturnList();

        Optional<GetBeerResponse> matchingResponse = responseList.stream()
                .filter(response -> response.beerId() == entityDTO.beerId())
                .findFirst();

        Assertions.assertTrue(matchingResponse.isPresent());

        GetBeerResponseDTO responseDTO = BeerConverter.MAPPER.convertToGetBeerResponseDTO(matchingResponse.get());
        Assertions.assertEquals(entityDTO, responseDTO, "Response contains the record added to Postgres");
    }

    @DisplayName("Verify GET /beers Response JSON Structure")
    @Test
    void checkGetAllBeersResponseStructure() {
        Response response = beerRequest.getBeerRequestReturnResponse();
        ResponseValidator.assertResponseMatchesSchema(response, BEER_ARRAY.getPath());
    }

    @DisplayName("Ensure GET /beers Response Code")
    @Test
    void checkGetAllBeersStatusCode() {
        Response response = beerRequest.getBeerRequestReturnResponse();
        Assertions.assertEquals(SC_OK, response.getStatusCode());
    }
}