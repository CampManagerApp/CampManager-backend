package camp.CampManager.organisation.campaign.tables;

import camp.CampManager.organisation.campaign.CampaignRepository;
import camp.CampManager.organisation.campaign.counsellors.CounsellorRepository;
import camp.CampManager.organisation.campaign.tables.restrictions.RestrictionRepository;
import camp.CampManager.organisation.campaign.tables.restrictions.SortByFavouriteRestriction;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/organisation/") // /organisation/{id}/campaign/{id}
public class TableEndpoint {

    @Autowired
    private JobScheduler scheduler;
    @Autowired
    private TableSolvingService tableSolvingService;

    @Autowired
    private TableService tableService;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private RestrictionRepository restrictionRepository;
    @Autowired
    private CounsellorRepository counsellorRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/{orgId}/campaign/{campId}/tables/all")
    @ResponseBody
    public ResponseEntity<List<CampTable>> getAllTablesOfCampaign(@PathVariable("orgId") Long orgId,
                                                                  @PathVariable("campId") Long campId) {
        return tableService.getAllTablesOfCampaign(orgId, campId);
    }

    @PostMapping("/{orgId}/campaign/{campId}/tables")
    @ResponseBody
    public ResponseEntity<String> createNewTableInCampaign(@PathVariable("orgId") Long orgId,
                                                           @PathVariable("campId") Long campId,
                                                           @RequestBody Map<String, String> input) throws URISyntaxException {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var buildingTable = CampTable.builder();
        if (input.containsKey("tableName")) {
            buildingTable.tableName(input.get("tableName"));
        } else {
            return ResponseEntity.badRequest().build();
        }
        if (input.containsKey("days")) {
            buildingTable.days(List.of(input.get("days").split(";")));
        } else {
            return ResponseEntity.badRequest().build();
        }
        // Tasks as name,min,max;
        if (input.containsKey("tasks")) {
            try {
                List<Task> tasks = new LinkedList<>();
                for (String taskInString : input.get("tasks").split(";")) {
                    String taskName = taskInString.split(",")[0];
                    int minCounsellors = Integer.parseInt(taskInString.split(",")[1]);
                    int maxCounsellors = Integer.parseInt(taskInString.split(",")[2]);
                    Task task = new Task(taskName, minCounsellors, maxCounsellors);
                    tasks.add(task);
                }
                buildingTable.tasks(tasks);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
        // Restrictions as list of type,par1,par2[,par3];...
        if (input.containsKey("restrictions")) {
            var restrictions = tableService.parseRestrictions(input.get("restrictions"));
            if (restrictions == null) {
                return ResponseEntity.badRequest().build();
            }
            restrictions.add(new SortByFavouriteRestriction());
            buildingTable.restrictions(restrictions);
        } else {
            buildingTable.restrictions(new LinkedList<>());
        }
        if (input.containsKey("counsellors")) {
            var counsellors = tableService.parseCounsellors(input.get("counsellors"), camp_o.get());
            if (counsellors == null) {
                return ResponseEntity.badRequest().build();
            }
            buildingTable.counsellors(counsellors);
        } else {
            // TODO fer que agafi tots els de la campaign
            buildingTable.counsellors(new LinkedList<>());
        }
        return tableService.createNewTableInCampaign(orgId, campId, buildingTable.build());
    }

    @GetMapping("/{orgId}/campaign/{campId}/tables")
    @ResponseBody
    public ResponseEntity<CampTable> getTableOfCampaign(@PathVariable("orgId") Long orgId,
                                                        @PathVariable("campId") Long campId,
                                                        @RequestBody Map<String, String> input) {
        if (!input.containsKey("tableName")) {
            return ResponseEntity.badRequest().build();
        }
        return tableService.getTableByName(orgId, campId, input.get("tableName"));
    }

    @PutMapping("/{orgId}/campaign/{campId}/tables")
    @ResponseBody
    public ResponseEntity<CampTable> updateTableOfCampaign(@PathVariable("orgId") Long orgId,
                                                           @PathVariable("campId") Long campId,
                                                           @RequestBody Map<String, String> input) {
        if (!input.containsKey("tableName")) {
            return ResponseEntity.badRequest().build();
        }
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var tableResponse = tableService.getTableByName(orgId, campId, input.get("tableName"));
        if (tableResponse.getStatusCode() != HttpStatus.OK || tableResponse.getBody() == null) {
            return ResponseEntity.badRequest().build();
        }
        CampTable table = tableResponse.getBody();
        table.setStatus("DEPRECATED");
        if (input.containsKey("days")) {
            table.setDays(List.of(input.get("days").split(";")));
        }
        // Tasks as name,min,max;
        if (input.containsKey("tasks")) {
            try {
                List<Task> tasks = new LinkedList<>();
                for (String taskInString : input.get("tasks").split(";")) {
                    String taskName = taskInString.split(",")[0];
                    int minCounsellors = Integer.parseInt(taskInString.split(",")[1]);
                    int maxCounsellors = Integer.parseInt(taskInString.split(",")[2]);
                    Task task = new Task(taskName, minCounsellors, maxCounsellors);
                    tasks.add(task);
                }
                table.setTasks(tasks);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }
        // Restrictions as list of type,par1,par2[,par3];...
        if (input.containsKey("restrictions")) {
            var restrictions = tableService.parseRestrictions(input.get("restrictions"));
            if (restrictions == null) {
                return ResponseEntity.badRequest().build();
            }
            restrictions.add(new SortByFavouriteRestriction());
            table.setRestrictions(restrictions);
        }
        if (input.containsKey("counsellors")) {
            var counsellors = tableService.parseCounsellors(input.get("counsellors"), camp_o.get());
            if (counsellors == null) {
                return ResponseEntity.badRequest().build();
            }
            table.setCounsellors(counsellors);
        } else {
            // TODO fer que agafi tots els de la campaign
            table.setCounsellors(new LinkedList<>());
        }
        return tableService.updateTable(table);
    }

    @DeleteMapping("/{orgId}/campaign/{campId}/tables")
    @ResponseBody
    public ResponseEntity<String> deleteTableOfCampaign(@PathVariable("orgId") Long orgId,
                                                        @PathVariable("campId") Long campId,
                                                        @RequestBody Map<String, String> input) {
        if (!input.containsKey("tableName")) {
            return ResponseEntity.badRequest().build();
        }
        return tableService.deleteTableByName(orgId, campId, input.get("tableName"));
    }

    @PostMapping("/{orgId}/campaign/{campId}/tables/solve")
    @ResponseBody
    public ResponseEntity<String> createSolveTableJob(@PathVariable("orgId") Long orgId,
                                                      @PathVariable("campId") Long campId,
                                                      @RequestBody Map<String, String> input) {
        if (!input.containsKey("tableName")) {
            return ResponseEntity.badRequest().build();
        }
        var table = tableService.getTableByName(orgId, campId, input.get("tableName"));
        if (table.getStatusCode() != HttpStatus.OK || table.getBody() == null) {
            return ResponseEntity.badRequest().build();
        }
        CampTable tableObject = table.getBody();
        System.out.println("ENQUEUEING JOB");
        scheduler.enqueue(() -> tableSolvingService.solveTable(tableObject, tableObject.getTableName()));
        System.out.println("JOB ENQUEUED");
        return ResponseEntity.ok().build();
    }
}
