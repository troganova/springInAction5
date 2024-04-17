package tacos.data;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import reactor.core.publisher.Mono;
import tacos.domain.User;

@RepositoryRestResource
public interface UserRepository extends ReactiveMongoRepository<User, String> {

  Mono<User> findByUsername(String username);

  Mono<User> findByEmail(String email);
  
}
