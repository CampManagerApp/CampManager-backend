package camp.CampManager.organisation.campaign.tables.restrictions;

import camp.CampManager.organisation.campaign.CampaignRepository;
import camp.CampManager.organisation.campaign.tables.CampTable;
import camp.CampManager.organisation.campaign.tables.TableRepository;
import camp.CampManager.organisation.campaign.tables.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RestrictionService {

    @Autowired
    private RestrictionRepository restrictionRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private TableService tableService;

    public ResponseEntity<List<Restriction>> findAllRestrictionsOfTable(Long orgId, Long campId, String tableName) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Optional<CampTable> tableOptional = tableRepository.findByCampaignIdAndTableNameEquals(campId, tableName);
        if (tableOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CampTable table = tableOptional.get();
        tableService.populateTable(table);
        return ResponseEntity.ok(table.getRestrictions());
    }

    public ResponseEntity<Restriction> findRestrictionOfTable(Long orgId, Long campId, String tableName, String restrictionName) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Optional<CampTable> tableOptional = tableRepository.findByCampaignIdAndTableNameEquals(campId, tableName);
        if (tableOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CampTable table = tableOptional.get();
        tableService.populateTable(table);
        for (Restriction restriction : table.getRestrictions()) {
            if (restriction.getName().equals(restrictionName)) {
                return ResponseEntity.ok(restriction);
            }
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Restriction> createRestrictionInTable(Long orgId, Long campId, String tableName, Restriction restriction) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Optional<CampTable> tableOptional = tableRepository.findByCampaignIdAndTableNameEquals(campId, tableName);
        if (tableOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CampTable table = tableOptional.get();
        tableService.populateTable(table);
        var restrictions = table.getRestrictions();
        Restriction sortByFav = restrictions.get(restrictions.size() - 1);
        restrictions.remove(restrictions.size() - 1);
        Restriction noFirstYearOnly = restrictions.get(restrictions.size() - 1);
        restrictions.remove(restrictions.size() - 1);
        restrictionRepository.save(restriction);
        restrictions.add(restriction);
        restrictions.add(noFirstYearOnly);
        restrictions.add(sortByFav);
        table.setRestrictions(restrictions);
        var newRestrictionsIds = new LinkedList<Long>();
        for (Restriction tableRestriction : table.getRestrictions()) {
            newRestrictionsIds.add(tableRestriction.getId());
        }
        table.setRestrictions_ids(newRestrictionsIds);
        tableRepository.save(table);
        return ResponseEntity.ok(restriction);
    }

    public ResponseEntity<String> deleteRestrictionOfTable(Long orgId, Long campId, String tableName, String restrictionName) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Optional<CampTable> tableOptional = tableRepository.findByCampaignIdAndTableNameEquals(campId, tableName);
        if (tableOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CampTable table = tableOptional.get();
        tableService.populateTable(table);
        var newRestrictionsIds = new LinkedList<Long>();
        Long restrictionToDelete = -1L;
        for (Restriction restriction : table.getRestrictions()) {
            if (!restriction.getName().equals(restrictionName)) {
                newRestrictionsIds.add(restriction.getId());
            } else {
                restrictionToDelete = restriction.getId();
            }
        }
        if (restrictionToDelete.equals(-1L)) {
            return ResponseEntity.notFound().build();
        } else {
            restrictionRepository.deleteById(restrictionToDelete);
            table.setRestrictions_ids(newRestrictionsIds);
            return ResponseEntity.ok().build();
        }
    }
}
