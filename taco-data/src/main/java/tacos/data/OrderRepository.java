package tacos.data;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tacos.domain.Order;

@RepositoryRestResource
public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
}
