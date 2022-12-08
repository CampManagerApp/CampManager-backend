package camp.CampManager.organisation.campaign.tables;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TableRepository extends CrudRepository<CampTable, Long> {
    List<CampTable> findByCampaignIdEquals(Long campaignId);

    Optional<CampTable> findByCampaignIdAndTableNameEquals(Long campaignId, String tableName);
}
