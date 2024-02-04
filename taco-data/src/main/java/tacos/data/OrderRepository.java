package tacos.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tacos.domain.Order;

@RepositoryRestResource
public interface OrderRepository extends CrudRepository<Order, Long> {
}
