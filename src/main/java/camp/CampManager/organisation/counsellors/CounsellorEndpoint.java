package camp.CampManager.organisation.counsellors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public ResponseEntity<List<Counsellor>> getCampaignCounsellors(@PathVariable("orgId") Long orgId,
                                                                    @PathVariable("campId") Long campId){
        return counsellorService.getAllCounsellorsOfCampaign(orgId, campId);
    }

    @GetMapping("/{orgId}/campaign/{campId}/counsellor/info")
    @ResponseBody
    public ResponseEntity<Counsellor> getCampaignCounsellors(@PathVariable("orgId") Long orgId,
                                                              @PathVariable("campId") Long campId,
                                                              @RequestBody Map<String, String> input){
        return counsellorService.getInfoOfCampaignCounsellor(orgId, campId, input);
    }

    @PostMapping("/{orgId}/campaign/{campId}/counsellor")
    @ResponseBody
    public ResponseEntity<String> addNewCounsellorToCampaign(@PathVariable("orgId") Long orgId,
                                                              @PathVariable("campId") Long campId,
                                                              @RequestBody Map<String, String> input) throws URISyntaxException, ParseException {
        return counsellorService.addNewCounsellorToCampaign(orgId, campId, input);
    }

    @DeleteMapping("/{orgId}/campaign/{campId}/counsellor")
    @ResponseBody
    public ResponseEntity<String> deleteCounsellorFromCampaign(@PathVariable("orgId") Long orgId,
                                                                @PathVariable("campId") Long campId,
                                                                @RequestBody Map<String, String> input){
        return counsellorService.deleteCounsellorFromCampaign(orgId, campId, input);
    }
}
