package camp.CampManager.organisation.campaign;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository extends CrudRepository<Campaign, Long> {
    Optional<Campaign> findByCampaignNameEquals(String campaign_name);

    Optional<Campaign> findByCampaignNameEqualsAndOrganisationIdEquals(String campaign_name, Long organisation_id);

    List<Campaign> findByOrganisationIdEquals(Long organisationId);



}
