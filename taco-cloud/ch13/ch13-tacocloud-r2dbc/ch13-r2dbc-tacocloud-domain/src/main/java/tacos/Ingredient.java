package tacos;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(exclude = "id")
public class Ingredient {

  @Id
  private Long id;

  private @NonNull String slug;
  private @NonNull String name;
  private @NonNull Type type;
  
  public enum Type {
    WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
  }

  public Ingredient(@NonNull String slug, @NonNull String name, @NonNull Type type) {
    this.slug = slug;
    this.name = name;
    this.type = type;
  }
}
