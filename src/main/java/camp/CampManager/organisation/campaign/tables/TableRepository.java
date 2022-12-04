package camp.CampManager.organisation.campaign.tables;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TableRepository extends CrudRepository<CampTable, Long> {
    List<CampTable> findByCampaignIdEquals(Long campaignId);
}
