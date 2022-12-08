package camp.CampManager.organisation.campaign.activities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/organisation/")
public class ActivitiesEndpoint {

    @Autowired
    private ActivitiesService activitiesService;

    @GetMapping("/{orgId}/activities/")
    @ResponseBody
    public ResponseEntity<List<Activity>> getAllActivitiesOfOrganisation(@PathVariable("orgId") Long orgId){
        return activitiesService.findAllOfOrganisation(orgId);
    }

    @GetMapping("/{orgId}/campaign/{campId}/activities")
    @ResponseBody
    public ResponseEntity<List<Activity>> getAllActivitiesInACampaign(@PathVariable("orgId") Long orgId,
                                                                      @PathVariable("campId") Long campId){
        return activitiesService.findAllOfCampaign(orgId, campId);
    }

    @GetMapping("/{orgId}/campaign/{campId}/activities/info")
    @ResponseBody
    public ResponseEntity<Activity> getOneActivityOfACampaign(@PathVariable("orgId") Long orgId,
                                                              @PathVariable("campId") Long campId,
                                                              @RequestParam("name") String name) {
        return activitiesService.findActivityOfCampaign(orgId, campId, name);
    }

    @PostMapping("/{orgId}/campaign/{campId}/activities")
    @ResponseBody
    public ResponseEntity<String> createNewActivityInACampaign(@PathVariable("orgId") Long orgId,
                                                               @PathVariable("campId") Long campId,
                                                               @RequestBody Map<String, String> input) throws URISyntaxException {
        return activitiesService.createNewActivityInCampaign(orgId, campId, input);
    }

    @PutMapping("/{orgId}/campaign/{campId}/activities")
    @ResponseBody
    public ResponseEntity<String> modifyActivityInACampaign(@PathVariable("orgId") Long orgId,
                                                                    @PathVariable("campId") Long campId,
                                                                    @RequestBody Map<String, String> input){
        return activitiesService.modifyActivityInCampaign(orgId, campId, input);
    }

    @DeleteMapping("/{orgId}/campaign/{campId}/activities")
    @ResponseBody
    public ResponseEntity<String> deleteActivityInACampaign(@PathVariable("orgId") Long orgId,
                                                            @PathVariable("campId") Long campId,
                                                            @RequestBody Map<String, String> input){
        return activitiesService.deleteActivityInACampaign(orgId, campId, input);
    }
}
