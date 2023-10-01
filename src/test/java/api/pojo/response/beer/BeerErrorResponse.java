package api.pojo.response.beer;

import java.util.List;

public record BeerErrorResponse(List<String> error) {
}