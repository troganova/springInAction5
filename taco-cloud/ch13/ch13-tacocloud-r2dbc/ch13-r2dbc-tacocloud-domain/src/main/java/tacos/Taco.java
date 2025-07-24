package tacos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Taco {

  @Id
  private Long id;

  private @NonNull String name;

  @Transient
  private transient List<Ingredient> ingredients = new ArrayList<>();

  private Set<Long> ingredientIds = new HashSet<>();

  public void addIngredient(Ingredient ingredient) {
    ingredients.add(ingredient);
    ingredientIds.add(ingredient.getId());
  }

  public Taco(@NonNull String name, Set<Long> ingredientIds) {
    this.name = name;
    this.ingredientIds = ingredientIds;
  }
}
