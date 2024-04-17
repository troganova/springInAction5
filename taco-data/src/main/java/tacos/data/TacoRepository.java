package tacos.data;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tacos.domain.Taco;

@RepositoryRestResource
public interface TacoRepository extends ReactiveMongoRepository<Taco, String> {

}
