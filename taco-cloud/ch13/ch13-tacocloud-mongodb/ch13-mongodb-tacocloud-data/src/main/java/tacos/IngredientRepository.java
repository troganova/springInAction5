package tacos;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, String> {
}
