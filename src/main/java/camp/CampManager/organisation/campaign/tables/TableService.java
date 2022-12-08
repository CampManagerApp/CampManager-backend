package camp.CampManager.organisation.campaign.tables;

import camp.CampManager.organisation.campaign.Campaign;
import camp.CampManager.organisation.campaign.CampaignRepository;
import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.counsellors.CounsellorRepository;
import camp.CampManager.organisation.campaign.tables.restrictions.*;
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
        // Counsellors ja estan guardats, tasks i restrictions no
        List<Long> tIds = new LinkedList<>();
        for (Task t : table.getTasks()) {
            taskRepository.save(t);
            tIds.add(t.getId());
        }
        table.setTask_ids(tIds);
        List<Long> rIds = new LinkedList<>();
        for (Restriction r : table.getRestrictions()) {
            restrictionRepository.save(r);
            rIds.add(r.getId());
        }
        table.setRestrictions_ids(rIds);
        List<Long> cIds = new LinkedList<>();
        for (Counsellor c : table.getCounsellors()) {
            cIds.add(c.getId());
        }
        table.setCounsellor_ids(cIds);
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
        if (restrictions == null || restrictions.length() == 0) {
            return new LinkedList<>();
        }
        List<Restriction> restrictionList = new LinkedList<>();
        try {
            for (String s : restrictions.split(";")) {
                String[] restrictionSplit = s.split(",");
                String type = restrictionSplit[0];
                switch (type) {
                    case "TASK_TASK" -> {
                        String taskName1 = restrictionSplit[1];
                        String taskName2 = restrictionSplit[2];
                        restrictionList.add(new TaskTaskRestriction(taskName1, taskName2));
                    }
                    case "COUNSELLOR_DAY" -> {
                        String counsellorFullName = restrictionSplit[1];
                        String dayString = restrictionSplit[2];
                        restrictionList.add(new CounsellorDayRestriction(counsellorFullName, dayString));
                    }
                    case "COUNSELLOR_TASK_DAY" -> {
                        String counsellorName = restrictionSplit[1];
                        String taskName = restrictionSplit[2];
                        String day = restrictionSplit[3];
                        restrictionList.add(new CounsellorTaskDayRestriction(counsellorName, taskName, day));
                    }
                    case "COUNSELLOR_TASK" -> {
                        String counsellorNameFull = restrictionSplit[1];
                        String task = restrictionSplit[2];
                        restrictionList.add(new CounsellorTaskRestriction(counsellorNameFull, task));
                    }
                    case "SPECIAL" -> {
                        String tName = restrictionSplit[1];
                        String attributeName = restrictionSplit[2];
                        String attributeDesiredValue = restrictionSplit[3];
                        restrictionList.add(new SpecialRestriction(tName, attributeName, attributeDesiredValue));
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return restrictionList;
    }

    public List<Counsellor> parseCounsellors(String counsellors, Campaign campaign) {
        if (counsellors == null || counsellors.length() == 0) {
            return new LinkedList<>();
        }
        List<Counsellor> counsellorList = new LinkedList<>();
        List<Long> idsInCampaign = campaign.getCounsellor_ids();
        Iterable<Counsellor> counsellorsInCampaign = counsellorRepository.findAllById(idsInCampaign);
        for (String s : counsellors.split(";")) {
            boolean exists = false;
            for (Counsellor c : counsellorsInCampaign) {
                if (c.getFullName().equals(s)) {
                    exists = true;
                    counsellorList.add(c);
                    break;
                }
            }
            if (!exists) {
                return null;
            }
        }
        return counsellorList;
    }

    public ResponseEntity<CampTable> updateTable(CampTable table) {
        List<Long> tIds = new LinkedList<>();
        for (Task t : table.getTasks()) {
            taskRepository.save(t);
            tIds.add(t.getId());
        }
        table.setTask_ids(tIds);
        List<Long> rIds = new LinkedList<>();
        for (Restriction r : table.getRestrictions()) {
            restrictionRepository.save(r);
            rIds.add(r.getId());
        }
        table.setRestrictions_ids(rIds);
        List<Long> cIds = new LinkedList<>();
        for (Counsellor c : table.getCounsellors()) {
            cIds.add(c.getId());
        }
        table.setCounsellor_ids(cIds);
        tableRepository.save(table);
        return ResponseEntity.ok(table);
    }
}
