package tacos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
  private Long id;
  private  String username;
  private  String password;
  private  String fullname;
  private  String street;
  private  String city;
  private  String state;
  private  String zip;
  private  String phoneNumber;

}
