package tacos.ingredients;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins="*")
public interface IngredientRepository
         extends ReactiveMongoRepository<Ingredient, String> {

}