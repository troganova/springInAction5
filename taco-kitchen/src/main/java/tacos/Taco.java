package tacos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Taco {

  private Long id;
  private String name;
  private Date createdAt;
  private List<Ingredient> ingredients = new ArrayList<>();

}
