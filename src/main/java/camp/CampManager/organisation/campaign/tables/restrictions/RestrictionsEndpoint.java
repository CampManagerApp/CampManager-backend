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
        return null;
    }

    @PutMapping("/{orgId}/campaign/{campId}/tables/restrictions")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
    @ResponseBody
    public ResponseEntity<Restriction> updateARestrictionInATable(@PathVariable("orgId") Long orgId,
                                                                  @PathVariable("campId") Long campId,
                                                                  @RequestBody Map<String, String> input) {
        return null;
    }

    @DeleteMapping("/{orgId}/campaign/{campId}/tables/restrictions")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
    @ResponseBody
    public ResponseEntity<String> deleteARestrictionInATable(@PathVariable("orgId") Long orgId,
                                                             @PathVariable("campId") Long campId,
                                                             @RequestBody Map<String, String> input) {
        return null;
    }
}
