package tacos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
public class PaymentMethod {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private User user;
  private String ccNumber;
  private String ccCVV;
  private String ccExpiration;

  public PaymentMethod(User user, String ccNumber, String ccCVV, String ccExpiration) {
    this.user = user;
    this.ccNumber = ccNumber;
    this.ccCVV = ccCVV;
    this.ccExpiration = ccExpiration;
  }
}
