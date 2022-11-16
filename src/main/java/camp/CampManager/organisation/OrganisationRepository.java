package camp.CampManager.organisation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrganisationRepository extends CrudRepository<Organisation, Long> {
    Optional<Organisation> findByName(String name);
}
