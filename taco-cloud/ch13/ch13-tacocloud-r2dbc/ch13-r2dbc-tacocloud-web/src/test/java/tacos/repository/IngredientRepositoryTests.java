package tacos.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.IngredientRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@DirtiesContext
public class IngredientRepositoryTests {

  @Autowired
  IngredientRepository ingredientRepo;

  @Test
  public void findById() {
    // Тест для существующего ингредиента
    Ingredient ingredient = new Ingredient("FLTO", "Flour Tortilla", Type.WRAP);
    ingredientRepo.save(ingredient)
            .then(ingredientRepo.findBySlug("FLTO"))
            .as(StepVerifier::create)
            .assertNext(i -> assertThat(i.getName()).isEqualTo("Flour Tortilla"))
            .verifyComplete();
  }
  
}
