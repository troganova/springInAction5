package tacos.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tacos.domain.Ingredient;

@RepositoryRestResource
public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, String> {

}
