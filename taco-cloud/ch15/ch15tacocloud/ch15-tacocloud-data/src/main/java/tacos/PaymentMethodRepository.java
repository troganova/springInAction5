package tacos;

import org.springframework.data.repository.CrudRepository;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Long> {
  PaymentMethod findByUser(User user);
}
