package camp.CampManager.organisation.campaign.tables.restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/organisation/") // /organisation/{id}/campaign/{id}
public class RestrictionsEndpoint {

    @Autowired
    private RestrictionService restrictionService;

    @GetMapping("/{orgId}/campaign/{campId}/tables/restrictions/all")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN') or hasAuthority(#orgId.toString() + 'USER')")
    @ResponseBody
    public ResponseEntity<List<Restriction>> getAllRestrictionsOfTable(@PathVariable("orgId") Long orgId,
                                                                       @PathVariable("campId") Long campId,
                                                                       @RequestParam("tableName") String tableName) {
        if (tableName.equals("")) {
            return ResponseEntity.badRequest().build();
        }
        return restrictionService.findAllRestrictionsOfTable(orgId, campId, tableName);
    }

    @GetMapping("/{orgId}/campaign/{campId}/tables/restrictions")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN') or hasAuthority(#orgId.toString() + 'USER')")
    @ResponseBody
    public ResponseEntity<Restriction> getARestrictionOfATableByName(@PathVariable("orgId") Long orgId,
                                                                     @PathVariable("campId") Long campId,
                                                                     @RequestBody Map<String, String> input) {
        if (!input.containsKey("tableName") || !input.containsKey("restrictionName")) {
            return ResponseEntity.badRequest().build();
        }
        return restrictionService.findRestrictionOfTable(orgId, campId, input.get("tableName"), input.get("restrictionName"));
    }

    @PostMapping("/{orgId}/campaign/{campId}/tables/restrictions")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
    @ResponseBody
    public ResponseEntity<Restriction> createARestrictionInATable(@PathVariable("orgId") Long orgId,
                                                                  @PathVariable("campId") Long campId,
                                                                  @RequestBody Map<String, String> input) {
        if (!input.containsKey("restrictionName") || !input.containsKey("restrictionType") || !input.containsKey("tableName")) {
            return ResponseEntity.badRequest().build();
        }
        Restriction restriction = null;
        switch (input.get("restrictionType")) {
            case "TASK_TASK" -> {
                String taskName1 = input.get("taskName1");
                String taskName2 = input.get("taskName2");
                if (taskName1 == null || taskName2 == null) {
                    return ResponseEntity.badRequest().build();
                }
                restriction = new TaskTaskRestriction(taskName1, taskName2);
            }
            case "COUNSELLOR_DAY" -> {
                String counsellorFullName = input.get("counsellorName");
                List<String> daysString = List.of(input.get("days").split(","));
                if (counsellorFullName == null || !input.containsKey("days")) {
                    return ResponseEntity.badRequest().build();
                }
                restriction = new CounsellorDayRestriction(counsellorFullName, daysString);
            }
            case "COUNSELLOR_TASK_DAY" -> {
                String counsellorName = input.get("counsellorName");
                String taskName = input.get("taskName");
                String day = input.get("day");
                if (counsellorName == null || taskName == null || day == null) {
                    return ResponseEntity.badRequest().build();
                }
                restriction = new CounsellorTaskDayRestriction(counsellorName, taskName, day);
            }
            case "COUNSELLOR_TASK" -> {
                String counsellorNameFull = input.get("counsellorName");
                String task = input.get("taskName");
                if (counsellorNameFull == null || task == null) {
                    return ResponseEntity.badRequest().build();
                }
                restriction = new CounsellorTaskRestriction(counsellorNameFull, task);
            }
            case "SPECIAL" -> {
                String tName = input.get("taskName");
                String attributeName = input.get("attribute");
                String attributeDesiredValue = input.get("desiredValue");
                if (tName == null || attributeName == null || attributeDesiredValue == null) {
                    return ResponseEntity.badRequest().build();
                }
                restriction = new SpecialRestriction(tName, attributeName, attributeDesiredValue);
            }
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }
        restriction.setName(input.get("restrictionName"));
        return restrictionService.createRestrictionInTable(orgId, campId, input.get("tableName"), restriction);
    }

    @DeleteMapping("/{orgId}/campaign/{campId}/tables/restrictions")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
    @ResponseBody
    public ResponseEntity<String> deleteARestrictionInATable(@PathVariable("orgId") Long orgId,
                                                             @PathVariable("campId") Long campId,
                                                             @RequestBody Map<String, String> input) {
        if (!input.containsKey("tableName") || !input.containsKey("restrictionName")) {
            return ResponseEntity.badRequest().build();
        }
        return restrictionService.deleteRestrictionOfTable(orgId, campId, input.get("tableName"), input.get("restrictionName"));
    }
}
