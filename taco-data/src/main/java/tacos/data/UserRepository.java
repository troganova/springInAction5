package tacos.data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tacos.domain.User;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {

  User findByUsername(String username);

  User findByEmail(String email);
  
}
