package tacos.ingredients;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tacos.ingredients.Ingredient.Type;

@Configuration
public class DevelopmentConfig {

  @Bean
  public CommandLineRunner dataLoader(IngredientRepository repo) {
    return args -> {
      repo.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP)).subscribe();
      repo.save(new Ingredient("COTO", "Corn Tortilla", Type.WRAP)).subscribe();
      repo.save(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN)).subscribe();
      repo.save(new Ingredient("CARN", "Carnitas", Type.PROTEIN)).subscribe();
      repo.save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES)).subscribe();
      repo.save(new Ingredient("LETC", "Lettuce", Type.VEGGIES)).subscribe();
      repo.save(new Ingredient("CHED", "Cheddar", Type.CHEESE)).subscribe();
      repo.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE)).subscribe();
      repo.save(new Ingredient("SLSA", "Salsa", Type.SAUCE)).subscribe();
      repo.save(new Ingredient("SRCR", "Sour Cream", Type.SAUCE)).subscribe();
    };
  }
  
}
