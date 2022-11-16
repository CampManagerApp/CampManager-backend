package camp.CampManager.organisation.campaign.participants;

import camp.CampManager.organisation.campaign.CampaignRepository;
import camp.CampManager.organisation.campaign.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignService campaignService;

    public ResponseEntity<List<Participant>> getAllParticipantsOfCampaign(Long orgId, Long campId) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var campaign = camp_o.get();
        var participants = new LinkedList<Participant>();
        for (Long part_id : campaign.getParticipant_ids()) {
            if (participantRepository.findById(part_id).isPresent()) {
                participants.add(participantRepository.findById(part_id).get());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(participants);
    }

    public ResponseEntity<String> addNewParticipantToCampaign(Long orgId, Long campId, Map<String, String> input) throws URISyntaxException {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var participant_builder = Participant.builder();
        if (input.containsKey("name")) {
            participant_builder.name(input.get("name"));
        } else {
            return ResponseEntity.badRequest().body("Name of participant missing");
        }
        if (input.containsKey("parents_contact")) {
            participant_builder.parents_contact(input.get("parents_contact"));
        }
        var participant_instance = participant_builder.build();
        participantRepository.save(participant_instance);
        campaignService.addParticipantToCampaign(camp_o.get(), participant_instance);
        return ResponseEntity.created(new URI("/organisation/id/campaign/id/participant")).build();
    }

    public ResponseEntity<String> deleteParticipantFromCampaign(Long orgId, Long campId, Map<String, String> input) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String name;
        if (input.containsKey("name")) {
            name = input.get("name");
        } else {
            return ResponseEntity.badRequest().body("Name of participant missing");
        }
        var campaign = camp_o.get();
        var new_ids = campaign.getParticipant_ids();
        Participant to_delete = null;
        for (Long part_id : campaign.getParticipant_ids()) {
            var p_o = participantRepository.findById(part_id);
            if (p_o.isPresent()) {
                if (p_o.get().getName().equals(name)) {
                    to_delete = p_o.get();
                }
            }
        }
        if (to_delete == null){
            return ResponseEntity.notFound().build();
        }
        campaignService.deleteParticipantFromCampaign(campaign, to_delete);
        participantRepository.deleteById(to_delete.getId());
        return ResponseEntity.ok().build();
    }
}
