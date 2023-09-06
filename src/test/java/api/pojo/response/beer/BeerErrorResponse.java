package api.pojo.response.beer;

import lombok.Data;

import java.util.List;

@Data
public class BeerErrorResponse {
    private List<String> error;
}