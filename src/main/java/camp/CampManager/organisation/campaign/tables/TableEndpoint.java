package camp.CampManager.organisation.campaign.tables;

import camp.CampManager.organisation.campaign.CampaignRepository;
import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.counsellors.CounsellorRepository;
import camp.CampManager.organisation.campaign.tables.restrictions.NoFirstYearOnlyRestriction;
import camp.CampManager.organisation.campaign.tables.restrictions.Restriction;
import camp.CampManager.organisation.campaign.tables.restrictions.RestrictionRepository;
import camp.CampManager.organisation.campaign.tables.restrictions.SortByFavouriteRestriction;
import camp.CampManager.users.MembershipRepository;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

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
    @Autowired
    private MembershipRepository membershipRepository;

    @GetMapping("/{orgId}/campaign/{campId}/tables/all")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN') or hasAuthority(#orgId.toString() + 'USER')")
    @ResponseBody
    public ResponseEntity<List<CampTable>> getAllTablesOfCampaign(@PathVariable("orgId") Long orgId,
                                                                  @PathVariable("campId") Long campId) {
        return tableService.getAllTablesOfCampaign(orgId, campId);
    }

    @PostMapping("/{orgId}/campaign/{campId}/tables")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
    @ResponseBody
    public ResponseEntity<CampTable> createNewTableInCampaign(@PathVariable("orgId") Long orgId,
                                                              @PathVariable("campId") Long campId,
                                                              @RequestBody Map<String, String> input,
                                                              HttpServletResponse response) throws URISyntaxException {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var buildingTable = CampTable.builder();
        if (input.containsKey("tableName")) {
            buildingTable.tableName(input.get("tableName"));
        } else {
            response.setHeader("error", "Table name missing");
            return ResponseEntity.badRequest().build();
        }
        if (input.containsKey("days")) {
            buildingTable.days(List.of(input.get("days").split(";")));
        } else {
            response.setHeader("error", "Table days missing");
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
                response.setHeader("error", "Tasks list has wrong format: " + e.getMessage());
                return ResponseEntity.badRequest().build();
            }
        } else {
            response.setHeader("error", "List of tasks missing");
            return ResponseEntity.badRequest().build();
        }
        // Restrictions as list of type,par1,par2[,par3];...
        if (input.containsKey("restrictions")) {
            var restrictions = tableService.parseRestrictions(input.get("restrictions"));
            if (restrictions == null) {
                response.setHeader("error", "Some error parsing restrictions");
                return ResponseEntity.badRequest().build();
            }
            restrictions.add(new NoFirstYearOnlyRestriction());
            restrictions.add(new SortByFavouriteRestriction());
            int i = 0;
            for (Restriction restriction : restrictions) {
                restriction.setName("Restriction number " + i);
                i++;
            }
            buildingTable.restrictions(restrictions);
        } else {
            var restrictions = new LinkedList<Restriction>();
            restrictions.add(new NoFirstYearOnlyRestriction());
            restrictions.add(new SortByFavouriteRestriction());
            buildingTable.restrictions(restrictions);
        }
        if (input.containsKey("counsellors")) {
            var counsellors = tableService.parseCounsellors(input.get("counsellors"), camp_o.get());
            if (counsellors == null) {
                response.setHeader("error", "Some error parsing counsellors");
                return ResponseEntity.badRequest().build();
            }
            buildingTable.counsellors(counsellors);
        } else {
            var counsellorIds = camp_o.get().getCounsellor_ids();
            var counsellors = counsellorRepository.findAllById(counsellorIds);
            List<Counsellor> listCounsellor = new LinkedList<>();
            counsellors.forEach(listCounsellor::add);
            buildingTable.counsellors(listCounsellor);
        }
        return tableService.createNewTableInCampaign(orgId, campId, buildingTable.build(), response);
    }

    @GetMapping("/{orgId}/campaign/{campId}/tables")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN') or hasAuthority(#orgId.toString() + 'USER')")
    @ResponseBody
    public ResponseEntity<CampTable> getTableOfCampaign(@PathVariable("orgId") Long orgId,
                                                        @PathVariable("campId") Long campId,
                                                        @RequestParam("tableName") String tableName) {
        return tableService.getTableByName(orgId, campId, tableName);
    }

    @PutMapping("/{orgId}/campaign/{campId}/tables")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
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
            restrictions.add(new NoFirstYearOnlyRestriction());
            restrictions.add(new SortByFavouriteRestriction());
            table.setRestrictions(restrictions);
        }
        if (input.containsKey("counsellors")) {
            var counsellors = tableService.parseCounsellors(input.get("counsellors"), camp_o.get());
            if (counsellors == null) {
                return ResponseEntity.badRequest().build();
            }
            table.setCounsellors(counsellors);
        }
        return tableService.updateTable(table);
    }

    @DeleteMapping("/{orgId}/campaign/{campId}/tables")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
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
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
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

    @GetMapping("/{orgId}/campaign/{campId}/tables/export")
    @ResponseBody
    public ResponseEntity<String> exportTable(@PathVariable("orgId") Long orgId,
                                              @PathVariable("campId") Long campId,
                                              @RequestParam("tableName") String tableName,
                                              HttpServletResponse response) throws IOException {
        var table = tableService.getTableByName(orgId, campId, tableName);
        if (table.getStatusCode() != HttpStatus.OK || table.getBody() == null) {
            return ResponseEntity.badRequest().build();
        }
        CampTable tableObject = table.getBody();
        tableService.populateTable(tableObject);

        var myWriter = response.getWriter();

        for (String day : tableObject.getDays()) {
            myWriter.write("," + day);
        }

        myWriter.write("\n");
        for (Task task : tableObject.getTasks()) {
            for (int i = 0; i < task.maxPlaces; i++) {
                myWriter.write(task.name);
                for (String day : tableObject.getDays()) {
                    Set<String> counsellors = tableObject.getGrid().get(day + ":" + task.name);
                    if (counsellors == null || i >= counsellors.size()) {
                        myWriter.write(",");
                    } else {
                        myWriter.write(",");
                        myWriter.write(counsellors.toArray()[i].toString());
                    }
                }
                myWriter.write("\n");
            }
        }

        response.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + tableObject.getTableName() + "_" + new Date().getTime() + ".csv";
        response.setHeader(headerKey, headerValue);
        return ResponseEntity.ok().build();
    }

    public String fix(String st) {
        return String.format("%-15s", st);
    }
}
