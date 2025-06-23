package tacos.repository;

import java.util.Optional;

import tacos.entity.Ingredient;

public interface IngredientRepository {

  Iterable<Ingredient> findAll();
  
  Optional<Ingredient> findById(String id);
  
  Ingredient save(Ingredient ingredient);
  
}
