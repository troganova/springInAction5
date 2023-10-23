package tacos.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tacos.domain.Ingredient;

@RepositoryRestResource
public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
