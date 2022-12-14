package camp.CampManager.organisation.campaign.counsellors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/organisation/") // /organisation/{id}/campaign/{id}
public class CounsellorEndpoint {

    @Autowired
    private CounsellorService counsellorService;

    @GetMapping("/{orgId}/campaign/{campId}/counsellor")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN') or hasAuthority(#orgId.toString() + 'USER')")
    @ResponseBody
    public ResponseEntity<List<Counsellor>> getCampaignCounsellors(@PathVariable("orgId") Long orgId,
                                                                    @PathVariable("campId") Long campId){
        return counsellorService.getAllCounsellorsOfCampaign(orgId, campId);
    }

    @GetMapping("/{orgId}/campaign/{campId}/counsellor/info")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN') or hasAuthority(#orgId.toString() + 'USER')")
    @ResponseBody
    public ResponseEntity<Counsellor> getCampaignCounsellor(@PathVariable("orgId") Long orgId,
                                                            @PathVariable("campId") Long campId,
                                                            @RequestParam("name") String name) {
        return counsellorService.getInfoOfCampaignCounsellor(orgId, campId, name);
    }

    @PostMapping("/{orgId}/campaign/{campId}/counsellor")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
    @ResponseBody
    public ResponseEntity<Counsellor> addNewCounsellorToCampaign(@PathVariable("orgId") Long orgId,
                                                                 @PathVariable("campId") Long campId,
                                                                 @RequestBody Map<String, String> input,
                                                                 HttpServletResponse response) throws URISyntaxException, ParseException {
        return counsellorService.addNewCounsellorToCampaign(orgId, campId, input, response);
    }

    @DeleteMapping("/{orgId}/campaign/{campId}/counsellor")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
    @ResponseBody
    public ResponseEntity<String> deleteCounsellorFromCampaign(@PathVariable("orgId") Long orgId,
                                                                @PathVariable("campId") Long campId,
                                                                @RequestBody Map<String, String> input){
        return counsellorService.deleteCounsellorFromCampaign(orgId, campId, input);
    }
}
