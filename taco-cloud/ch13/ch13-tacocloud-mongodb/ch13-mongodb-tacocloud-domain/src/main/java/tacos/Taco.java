package tacos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Taco {

  private String id;

  private Date createdAt = new Date();

  @NotNull
  @Size(min=5, message="Name must be at least 5 characters long")
  private String name;

  @NotNull
  @Size(min=1, message="You must choose at least 1 ingredient")
  private List<Ingredient> ingredients = new ArrayList<>();

  public void addIngredient(Ingredient taco) {
    this.ingredients.add(taco);
  }

  public Taco(String name, List<Ingredient> ingredients) {
    this.name = name;
    this.ingredients = ingredients;
  }
}
