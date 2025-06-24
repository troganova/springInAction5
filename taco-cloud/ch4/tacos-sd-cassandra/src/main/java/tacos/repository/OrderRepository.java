package tacos.repository;

import org.springframework.data.repository.CrudRepository;
import tacos.entity.TacoOrder;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<TacoOrder, UUID> {
}
