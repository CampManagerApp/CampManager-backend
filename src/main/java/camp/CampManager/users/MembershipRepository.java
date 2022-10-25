package camp.CampManager.users;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MembershipRepository extends CrudRepository<Membership, Long> {
    public List<Membership> findByUserIdEquals(Long userId);
}