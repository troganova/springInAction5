package tacos.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor(force=true)
@RequiredArgsConstructor
public class PaymentMethod {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private long id;
  
  @ManyToOne
  private final User user;
  private final String ccNumber;
  private final String ccCVV;
  private final String ccExpiration;
  
}
