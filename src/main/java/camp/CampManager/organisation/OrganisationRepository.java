package camp.CampManager.organisation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrganisationRepository extends CrudRepository<Organisation, Long> {
    Optional<Organisation> findByName(String name);

    @Query("select o from Organisation o where o.organisationCode = ?1")
    Optional<Organisation> findByCode(String organisationCode);
}
