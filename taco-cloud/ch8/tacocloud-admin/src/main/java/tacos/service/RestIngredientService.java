package tacos.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import tacos.entity.Ingredient;

import java.util.Arrays;
import java.util.Collections;

public class RestIngredientService implements IngredientService {

  private final RestTemplate restTemplate;
  private final String baseUrl;

  public RestIngredientService(String accessToken) {
    this(accessToken, "http://localhost:8080/api");
  }

  public RestIngredientService(String accessToken, String baseUrl) {
    this.baseUrl = baseUrl;
    this.restTemplate = new RestTemplate();

    if (accessToken != null) {
      this.restTemplate.setInterceptors(
              Collections.singletonList(getBearerTokenInterceptor(accessToken))
      );
    }

  }

  @Override
  public Iterable<Ingredient> findAll() {
    return Arrays.asList(restTemplate.getForObject(
            baseUrl + "/ingredients",
            Ingredient[].class));
  }

  @Override
  public Ingredient addIngredient(Ingredient ingredient) {
    return restTemplate.postForObject(
        baseUrl + "/ingredients",
        ingredient,
        Ingredient.class);
  }

  private ClientHttpRequestInterceptor getBearerTokenInterceptor(String accessToken) {
    return (request, body, execution) -> {
      request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
      return execution.execute(request, body);
    };
  }

}
