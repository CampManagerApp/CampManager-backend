package camp.CampManager.organisation.campaign.participants;

import org.springframework.data.repository.CrudRepository;

public interface ParticipantRepository extends CrudRepository<Participant, Long> {
    boolean existsByFullName(String name);
}
