package camp.CampManager.organisation.campaign.participants;

import camp.CampManager.organisation.OrganisationService;
import camp.CampManager.organisation.campaign.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/organisation/") // /organisation/{id}/campaign/{id}
public class ParticipantEndpoint {

    @Autowired
    private ParticipantService participantService;

    @GetMapping("/{orgId}/campaign/{campId}/participant")
    @ResponseBody
    public ResponseEntity<List<Participant>> getCampaignParticipants(@PathVariable("orgId") Long orgId,
                                                                     @PathVariable("campId") Long campId){
        return participantService.getAllParticipantsOfCampaign(orgId, campId);
    }

    @GetMapping("/{orgId}/campaign/{campId}/participant/info")
    @ResponseBody
    public ResponseEntity<Participant> getCampaignParticipants(@PathVariable("orgId") Long orgId,
                                                               @PathVariable("campId") Long campId,
                                                               @RequestBody Map<String, String> input){
        return participantService.getInfoOfCampaignParticipant(orgId, campId, input);
    }

    @PostMapping("/{orgId}/campaign/{campId}/participant")
    @ResponseBody
    public ResponseEntity<String> addNewParticipantToCampaign(@PathVariable("orgId") Long orgId,
                                                              @PathVariable("campId") Long campId,
                                                              @RequestBody Map<String, String> input) throws URISyntaxException {
        return participantService.addNewParticipantToCampaign(orgId, campId, input);
    }

    @DeleteMapping("/{orgId}/campaign/{campId}/participant")
    @ResponseBody
    public ResponseEntity<String> deleteParticipantFromCampaign(@PathVariable("orgId") Long orgId,
                                                                @PathVariable("campId") Long campId,
                                                                @RequestBody Map<String, String> input){
        return participantService.deleteParticipantFromCampaign(orgId, campId, input);
    }
}
