package tacos;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface OrderRepository extends ReactiveCrudRepository<TacoOrder, UUID> {
}
