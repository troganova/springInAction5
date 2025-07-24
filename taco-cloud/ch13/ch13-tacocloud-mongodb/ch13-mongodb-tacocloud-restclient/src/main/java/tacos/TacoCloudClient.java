package tacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class TacoCloudClient {

  @Autowired
  public WebClient webClient;

  //
  // GET examples
  //

  /*
   * Specify parameter as varargs argument
   */
  public Mono<Ingredient> getIngredientBySlug(String ingredientId) {
    return webClient.get().uri("/ingredients/slug/{id}", ingredientId).retrieve().bodyToMono(Ingredient.class);
  }

  public Flux<Ingredient> getAllIngredients() {
    return webClient.get()
            .uri("/ingredients")
            .retrieve()
            .bodyToFlux(Ingredient.class);
  }

  //
  // PUT examples
  //

  public Mono<Ingredient> updateIngredient(Ingredient ingredient) {
    return webClient
            .put()
            .uri("/ingredients")
            .bodyValue(ingredient)
            .retrieve()
            .toBodilessEntity()
            .thenReturn(ingredient);
  }

  //
  // POST examples
  //
  public Mono<Ingredient> createIngredient(Mono<Ingredient> ingredient) {
    return webClient.post()
            .uri("/ingredients")
            .body(ingredient, Ingredient.class)
            .retrieve()
            .bodyToMono(Ingredient.class);
  }

  //
  // DELETE examples
  //

  public Mono<Void> deleteIngredient(Mono<Ingredient> ingredientMono) {
    return ingredientMono
            .flatMap(ingredient ->
                    webClient.delete()
                            .uri("/ingredients/{id}", ingredient.getId())
                            .retrieve()
                            .onStatus(httpStatusCode -> httpStatusCode.is5xxServerError(), response ->
                                    response.bodyToMono(String.class)
                                            .flatMap(body -> Mono.error(new RuntimeException(
                                                    "Failed to delete ingredient: " + body))))
                            .bodyToMono(Void.class)
            )
            .doOnSubscribe(sub -> log.debug("Starting ingredient deletion"))
            .doOnSuccess(v -> log.info("Ingredient deleted successfully"))
            .doOnError(e -> log.error("Failed to delete ingredient", e))
            .timeout(Duration.ofSeconds(5));
  }

}
