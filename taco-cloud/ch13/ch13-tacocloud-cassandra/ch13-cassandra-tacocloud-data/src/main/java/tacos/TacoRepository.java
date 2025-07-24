package tacos;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface TacoRepository extends ReactiveCrudRepository<Taco, UUID> {
}
