package tacos;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor(force=true, access=AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Table("payment_method")
public class PaymentMethod {

  @PrimaryKeyColumn(type= PrimaryKeyType.PARTITIONED)
  private UUID id = Uuids.timeBased();

  @Column("user")
  private final UserUDT user;
  private final String ccNumber;
  private final String ccCVV;
  private final String ccExpiration;
  
}
