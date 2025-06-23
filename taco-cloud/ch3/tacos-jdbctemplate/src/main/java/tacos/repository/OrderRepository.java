package tacos.repository;

import java.util.Optional;

import tacos.entity.TacoOrder;

public interface OrderRepository {

  TacoOrder save(TacoOrder order);

  Optional<TacoOrder> findById(Long id);

}
