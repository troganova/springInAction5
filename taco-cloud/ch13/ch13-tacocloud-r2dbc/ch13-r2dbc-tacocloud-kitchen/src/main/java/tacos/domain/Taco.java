package tacos.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Taco {

  private String name;
  
  private Date createdAt;

  private List<Ingredient> ingredients;

}
