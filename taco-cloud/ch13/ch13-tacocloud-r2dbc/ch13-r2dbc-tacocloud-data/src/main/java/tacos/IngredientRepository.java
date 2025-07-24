package tacos;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, Long> {
    Mono<Ingredient> findBySlug(String slug);
    Mono<Boolean> existsBySlug(String slug);
}
