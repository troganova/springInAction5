package tacos.service;

import tacos.entity.Ingredient;

public interface IngredientService {

  Iterable<Ingredient> findAll();
  
  Ingredient addIngredient(Ingredient ingredient);
    
}
