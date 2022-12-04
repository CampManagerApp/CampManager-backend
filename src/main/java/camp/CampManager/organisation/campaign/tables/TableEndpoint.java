package camp.CampManager.organisation.campaign.tables;

import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
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
        var buildingCampaign = CampTable.builder();
        if (input.containsKey("tableName")) {
            buildingCampaign.tableName(input.get("tableName"));
        }
        return tableService.createNewTableInCampaign(orgId, campId, buildingCampaign.build());
    }
}
