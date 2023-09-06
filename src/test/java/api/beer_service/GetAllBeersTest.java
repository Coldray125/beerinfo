package api.beer_service;

import api.conversion.BeerResponseConversion;
import api.db_query.BeerQuery;
import api.pojo.response.beer.GetBeerResponse;
import api.request.BeerRequest;
import api.test_utils.ResponseValidator;
import api.test_utils.data_generators.BeerObjectGenerator;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.beerinfo.dto.api.beer.GetBeerResponseDTO;
import org.beerinfo.entity.BeerEntity;
import org.beerinfo.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static api.test_utils.SchemaPaths.BEER_ARRAY;
import static org.apache.http.HttpStatus.SC_OK;

@Story("Beer API")
public class GetAllBeersTest {

    BeerRequest beerRequest = new BeerRequest();
    HibernateUtil hibernateUtil = new HibernateUtil();
    SessionFactory sessionFactory = hibernateUtil.buildSessionFactory();
    BeerQuery beerQuery = new BeerQuery(sessionFactory);

    @DisplayName("Verify GET /beers response contains record added to Postgres")
    @Test
    void checkGetAllBeersContainsAddedRecord() {
        BeerEntity randomEntity = BeerObjectGenerator.generateRandomBeerEntity();
        GetBeerResponseDTO entityDTO = beerQuery.addBeerReturnEntity(randomEntity);
        List<GetBeerResponse> responseList = beerRequest.getBeerRequestReturnList();

        Optional<GetBeerResponse> matchingResponse = responseList.stream()
                .filter(response -> response.getBeerId() == entityDTO.getBeerId())
                .findFirst();

        Assertions.assertTrue(matchingResponse.isPresent());

        GetBeerResponseDTO responseDTO = BeerResponseConversion.convertToGetBeerResponseDTO(matchingResponse.get());
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