package camp.CampManager.organisation.campaign.tables;

import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/organisation/") // /organisation/{id}/campaign/{id}
public class TableEndpoint {

    @Autowired
    private JobScheduler scheduler;
    @Autowired
    private TableService tableService;
    @Autowired
    private TableRepository tableRepository;

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
        var buildingTable = CampTable.builder();
        if (input.containsKey("tableName")) {
            buildingTable.tableName(input.get("tableName"));
        } else {
            return ResponseEntity.badRequest().build();
        }
        // TODO Set days, counsellors, restrictions
        // Days as string list
        // Counsellors as string list or empty then all in camp
        // Restrictions as list of type,par1,par2[,par3];...
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
        return tableService.updateTableByName(orgId, campId, input.get("tableName"));
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
        scheduler.enqueue(() -> {
            CampTable tableObject = table.getBody();
            tableService.populateTable(tableObject);
            tableObject.solve();
        });
        return ResponseEntity.ok().build();
    }
}
