package camp.CampManager.users;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<CampUser, Long> {
    Optional<CampUser> findByUsername(String username);
}
