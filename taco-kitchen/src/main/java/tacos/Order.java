package tacos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {
  private Date placedAt;
  private String name;
  private String street;
  private String city;
  private String state;
  private String zip;

  private List<Taco> tacos = new ArrayList<>();
}
