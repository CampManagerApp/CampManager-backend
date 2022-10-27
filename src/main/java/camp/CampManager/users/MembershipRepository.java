package camp.CampManager.users;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends CrudRepository<Membership, Long> {
    public List<Membership> findByUserIdEquals(Long userId);
    public Optional<Membership> findByUserIdEqualsAndOrganisationIdEquals(Long userId, Long organisationId);

    public List<Membership> findByOrganisationIdEquals(Long organisationId);
}