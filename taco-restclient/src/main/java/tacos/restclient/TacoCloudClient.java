package tacos.restclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tacos.domain.Ingredient;
import tacos.domain.Taco;

import java.util.Collection;


@Service
@Slf4j
public class TacoCloudClient {

  private RestTemplate rest;
  private Traverson traverson;

  private String applicationUrl;

  public TacoCloudClient(RestTemplate rest, Traverson traverson) {
    this.rest = rest;
    this.traverson = traverson;
    this.applicationUrl = "http://localhost:8080/api";
  }

  //
  // GET examples
  //

  /*
   * Specify parameter as varargs argument
   */
  public Ingredient getIngredientById(String ingredientId) {
    try {
      return rest.getForObject(applicationUrl + "/ingredients/{id}",
              Ingredient.class, ingredientId);
    } catch (HttpClientErrorException.NotFound ex) {
      return null;
    }
  }

  /*
   * Alternate implementations...
   * The next three methods are alternative implementations of
   * getIngredientById() as shown in chapter 6. If you'd like to try
   * any of them out, comment out the previous method and uncomment
   * the variant you want to use.
   */

  /*
   * Specify parameters with a map
   */
  // public Ingredient getIngredientById(String ingredientId) {
  //   Map<String, String> urlVariables = new HashMap<>();
  //   urlVariables.put("id", ingredientId);
  //   return rest.getForObject(applicationUrl + "/ingredients/{id}",
  //       Ingredient.class, urlVariables);
  // }

  /*
   * Request with URI instead of String
   */
  // public Ingredient getIngredientById(String ingredientId) {
  //   Map<String, String> urlVariables = new HashMap<>();
  //   urlVariables.put("id", ingredientId);
  //   URI url = UriComponentsBuilder
  //             .fromHttpUrl(applicationUrl + "/ingredients/{id}")
  //             .build(urlVariables);
  //   return rest.getForObject(url, Ingredient.class);
  // }

  /*
   * Use getForEntity() instead of getForObject()
   */
  // public Ingredient getIngredientById(String ingredientId) {
  //   ResponseEntity<Ingredient> responseEntity =
  //       rest.getForEntity(applicationUrl + "/ingredients/{id}",
  //           Ingredient.class, ingredientId);
  //   log.info("Fetched time: " +
  //           responseEntity.getHeaders().getDate());
  //   return responseEntity.getBody();
  // }

  public CollectionModel<Ingredient> getAllIngredients() {
    return rest.exchange(applicationUrl + "/ingredients",
            HttpMethod.GET, null, new ParameterizedTypeReference<CollectionModel<Ingredient>>() {})
        .getBody();
  }

  //
  // PUT examples
  //

  public void updateIngredient(Ingredient ingredient) {
    rest.put(applicationUrl + "/ingredients/{id}",
          ingredient, ingredient.getId());
  }

  //
  // POST examples
  //
  public Ingredient createIngredient(Ingredient ingredient) {
    return rest.postForObject(applicationUrl + "/ingredients",
        ingredient, Ingredient.class);
  }

  /*
   * Alternate implementations...
   * The next two methods are alternative implementations of
   * createIngredient() as shown in chapter 6. If you'd like to try
   * any of them out, comment out the previous method and uncomment
   * the variant you want to use.
   */

  // public URI createIngredient(Ingredient ingredient) {
  //   return rest.postForLocation(applicationUrl + "/ingredients",
  //       ingredient, Ingredient.class);
  // }

  // public Ingredient createIngredient(Ingredient ingredient) {
  //   ResponseEntity<Ingredient> responseEntity =
  //          rest.postForEntity(applicationUrl + "/ingredients",
  //                             ingredient,
  //                             Ingredient.class);
  //   log.info("New resource created at " +
  //            responseEntity.getHeaders().getLocation());
  //   return responseEntity.getBody();
  // }

  //
  // DELETE examples
  //

  public void deleteIngredient(Ingredient ingredient) {
    rest.delete(applicationUrl + "/ingredients/{id}",
        ingredient.getId());
  }

  //
  // Traverson with RestTemplate examples
  //

  public Iterable<Ingredient> getAllIngredientsWithTraverson() {
    ParameterizedTypeReference<CollectionModel<Ingredient>> ingredientType =
        new ParameterizedTypeReference<CollectionModel<Ingredient>>() {};

    CollectionModel<Ingredient> ingredientRes =
        traverson
          .follow("ingredients")
          .toObject(ingredientType);
    
    Collection<Ingredient> ingredients = ingredientRes.getContent();
          
    return ingredients;
  }

  public Ingredient addIngredient(Ingredient ingredient) {
    String ingredientsUrl = traverson
        .follow("ingredients")
        .asLink()
        .getHref();
    return rest.postForObject(ingredientsUrl,
                              ingredient,
                              Ingredient.class);
  }

  public Collection<Taco> getRecentTacosWithTraverson() {
    ParameterizedTypeReference<CollectionModel<Taco>> tacoType =
        new ParameterizedTypeReference<CollectionModel<Taco>>() {};

    CollectionModel<Taco> tacoRes =
        traverson
          .follow("tacos")
          .follow("recents")
          .toObject(tacoType);

      // Alternatively, list the two paths in the same call to follow()
//    CollectionModel<Taco> tacoRes =
//        traverson
//          .follow("tacos", "recents")
//          .toObject(tacoType);

    return tacoRes.getContent();
  }

}
