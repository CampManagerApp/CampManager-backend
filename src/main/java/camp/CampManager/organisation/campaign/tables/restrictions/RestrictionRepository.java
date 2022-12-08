package camp.CampManager.organisation.campaign.tables.restrictions;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestrictionRepository extends CrudRepository<Restriction, Long> {
}
