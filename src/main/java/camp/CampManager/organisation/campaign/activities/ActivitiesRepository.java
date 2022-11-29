package camp.CampManager.organisation.campaign.activities;

import org.springframework.data.repository.CrudRepository;

import java.util.Map;
import java.util.Optional;

public interface ActivitiesRepository extends CrudRepository<Activity, Long> {
    Iterable<Activity> findByCampaignIdEquals(Long campaignId);

    boolean existsByActivityName(String name);

    Optional<Activity> findByCampaignIdEqualsAndActivityNameEquals(Long campId, String activityName);
}
