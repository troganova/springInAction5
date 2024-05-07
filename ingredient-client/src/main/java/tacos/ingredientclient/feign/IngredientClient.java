package tacos.ingredientclient.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.ingredientclient.Ingredient;

@FeignClient("ingredient-service")
public interface IngredientClient {

  @GetMapping("/ingredients/{id}")
  Mono<Ingredient> getIngredient(@PathVariable("id") String id);

  @GetMapping("/ingredients")
  Flux<Ingredient> getAllIngredients();

}
