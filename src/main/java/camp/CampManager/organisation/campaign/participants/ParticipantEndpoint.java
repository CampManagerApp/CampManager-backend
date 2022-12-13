package camp.CampManager.organisation.campaign.participants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/organisation/") // /organisation/{id}/campaign/{id}
public class ParticipantEndpoint {

    @Autowired
    private ParticipantService participantService;

    @GetMapping("/{orgId}/campaign/{campId}/participant")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN') or hasAuthority(#orgId.toString() + 'USER')")
    @ResponseBody
    public ResponseEntity<List<Participant>> getCampaignParticipants(@PathVariable("orgId") Long orgId,
                                                                     @PathVariable("campId") Long campId){
        return participantService.getAllParticipantsOfCampaign(orgId, campId);
    }

    @GetMapping("/{orgId}/campaign/{campId}/participant/info")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN') or hasAuthority(#orgId.toString() + 'USER')")
    @ResponseBody
    public ResponseEntity<Participant> getCampaignParticipantInfo(@PathVariable("orgId") Long orgId,
                                                                  @PathVariable("campId") Long campId,
                                                                  @RequestParam("name") String name) {
        return participantService.getInfoOfCampaignParticipant(orgId, campId, name);
    }

    @PostMapping("/{orgId}/campaign/{campId}/participant")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
    @ResponseBody
    public ResponseEntity<String> addNewParticipantToCampaign(@PathVariable("orgId") Long orgId,
                                                              @PathVariable("campId") Long campId,
                                                              @RequestBody Map<String, String> input) throws URISyntaxException, ParseException {
        return participantService.addNewParticipantToCampaign(orgId, campId, input);
    }

    @DeleteMapping("/{orgId}/campaign/{campId}/participant")
    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority(#orgId.toString() + 'ADMIN')")
    @ResponseBody
    public ResponseEntity<String> deleteParticipantFromCampaign(@PathVariable("orgId") Long orgId,
                                                                @PathVariable("campId") Long campId,
                                                                @RequestBody Map<String, String> input){
        return participantService.deleteParticipantFromCampaign(orgId, campId, input);
    }
}
