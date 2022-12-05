package camp.CampManager.organisation.campaign.tables;

import camp.CampManager.organisation.campaign.CampaignRepository;
import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.counsellors.CounsellorRepository;
import camp.CampManager.organisation.campaign.tables.restrictions.Restriction;
import camp.CampManager.organisation.campaign.tables.restrictions.RestrictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class TableService {

    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CounsellorRepository counsellorRepository;
    @Autowired
    private RestrictionRepository restrictionRepository;

    public ResponseEntity<List<CampTable>> getAllTablesOfCampaign(Long orgId, Long campId) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tableRepository.findByCampaignIdEquals(campId));
    }

    public ResponseEntity<String> createNewTableInCampaign(Long orgId, Long campId, CampTable table) throws URISyntaxException {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (tableRepository.findByCampaignIdAndTableNameEquals(campId, table.getTableName()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        table.setCampaignId(campId);
        table.setStatus("CREATED");
        table.setGrid(new HashMap<>());
        tableRepository.save(table);
        return ResponseEntity.created(new URI("/organisation/id/campaign/id/tables")).build();
    }

    public ResponseEntity<CampTable> getTableByName(Long orgId, Long campId, String tableName) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var table_o = tableRepository.findByCampaignIdAndTableNameEquals(campId, tableName);
        return table_o.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<CampTable> updateTableByName(Long orgId, Long campId, String tableName) {
        // TODO
        return null;
    }

    public ResponseEntity<String> deleteTableByName(Long orgId, Long campId, String tableName) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var table_o = tableRepository.findByCampaignIdAndTableNameEquals(campId, tableName);
        if (table_o.isPresent()) {
            tableRepository.deleteById(table_o.get().getId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    public void populateTable(CampTable tableObject) {
        var tasks = new LinkedList<Task>();
        taskRepository.findAllById(tableObject.getTask_ids()).forEach(tasks::add);
        tableObject.setTasks(tasks);
        var counsellors = new LinkedList<Counsellor>();
        counsellorRepository.findAllById(tableObject.getCounsellor_ids()).forEach(counsellors::add);
        tableObject.setCounsellors(counsellors);
        var restrictions = new LinkedList<Restriction>();
        restrictionRepository.findAllById(tableObject.getRestrictions_ids()).forEach(restrictions::add);
        tableObject.setRestrictions(restrictions);
    }

    public List<Restriction> parseRestrictions(String restrictions) {
        // TODO
    }

    public List<Counsellor> parseCounsellors(String counsellors) {
        // TODO
    }
}
