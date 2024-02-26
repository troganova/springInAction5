package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.domain.PaymentMethod;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Long> {
  PaymentMethod findByUserId(Long userId);
}
