package camp.CampManager.users;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends CrudRepository<Membership, Long> {
    List<Membership> findByUserIdEquals(Long userId);

    Optional<Membership> findByUserIdEqualsAndOrganisationIdEquals(Long userId, Long organisationId);

    List<Membership> findByOrganisationIdEquals(Long organisationId);

    List<Membership> findByFullnameEquals(String fullname);

    Optional<Membership> findByFullnameEqualsAndOrganisationIdEquals(String fullname, Long organisationId);
}