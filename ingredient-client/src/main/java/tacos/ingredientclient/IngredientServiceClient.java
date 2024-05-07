package tacos.ingredientclient;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class IngredientServiceClient {

  private WebClient.Builder wcBuilder;
  
  public IngredientServiceClient(@LoadBalanced WebClient.Builder wcBuilder) {
    this.wcBuilder = wcBuilder;
  }
  
  public Mono<Ingredient> getIngredientById(String ingredientId) {  
    return wcBuilder.build()    
        .get()      
        .uri("http://ingredient-service/ingredients/{id}", ingredientId)    
        .retrieve().bodyToMono(Ingredient.class);
  }
  
  public Flux<Ingredient> getAllIngredients() {
    return wcBuilder.build()    
        .get()      
        .uri("http://ingredient-service/ingredients")    
        .retrieve().bodyToFlux(Ingredient.class);
  }
  
}
