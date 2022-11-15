package camp.CampManager.organisation.campaign.participants;

import camp.CampManager.organisation.OrganisationService;
import camp.CampManager.organisation.campaign.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/organisation/") // /organisation/{id}/campaign/{id}
public class ParticipantEndpoint {

    @Autowired
    private ParticipantService participantService;

    @GetMapping("({orgId}/campaign/{campId}/participants")
    @ResponseBody
    public ResponseEntity<List<Participant>> getCampaignParticipants(@PathVariable("orgId") Long orgId,
                                                                     @PathVariable("orgId") Long campId){
        return participantService.getAllParticipantsOfCampaign(orgId, campId);
    }
}
