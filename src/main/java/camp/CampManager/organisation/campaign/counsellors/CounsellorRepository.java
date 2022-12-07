package camp.CampManager.organisation.campaign.counsellors;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CounsellorRepository extends CrudRepository<Counsellor, Long> {
    Optional<Counsellor> findByFullNameEquals(String fullName);
}
