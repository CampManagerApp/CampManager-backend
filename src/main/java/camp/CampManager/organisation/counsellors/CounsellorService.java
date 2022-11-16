package camp.CampManager.organisation.counsellors;

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
public class CounsellorService {
    @Autowired
    private CounsellorRepository counsellorRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignService campaignService;

    public ResponseEntity<List<Counsellor>> getAllCounsellorsOfCampaign(Long orgId, Long campId) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var campaign = camp_o.get();
        var participants = new LinkedList<Counsellor>();
        for (Long part_id : campaign.getCounsellor_ids()) {
            if (counsellorRepository.findById(part_id).isPresent()) {
                participants.add(counsellorRepository.findById(part_id).get());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(participants);
    }

    public ResponseEntity<String> addNewCounsellorToCampaign(Long orgId, Long campId, Map<String, String> input) throws URISyntaxException {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var participant_builder = Counsellor.builder();
        if (input.containsKey("name")) {
            participant_builder.name(input.get("name"));
        } else {
            return ResponseEntity.badRequest().body("Name of participant missing");
        }
        if (input.containsKey("parents_contact")) {
            participant_builder.parents_contact(input.get("parents_contact"));
        }
        var counsellor = participant_builder.build();
        counsellorRepository.save(counsellor);
        campaignService.addCounsellorToCampaign(camp_o.get(), counsellor);
        return ResponseEntity.created(new URI("/organisation/id/campaign/id/participant")).build();
    }

    public ResponseEntity<String> deleteCounsellorFromCampaign(Long orgId, Long campId, Map<String, String> input) {
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
        var new_ids = campaign.getCounsellor_ids();
        Counsellor to_delete = null;
        for (Long part_id : campaign.getCounsellor_ids()) {
            var p_o = counsellorRepository.findById(part_id);
            if (p_o.isPresent()) {
                if (p_o.get().getName().equals(name)) {
                    to_delete = p_o.get();
                }
            }
        }
        if (to_delete == null) {
            return ResponseEntity.notFound().build();
        }
        campaignService.deleteCounsellorFromCampaign(campaign, to_delete);
        counsellorRepository.deleteById(to_delete.getId());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Counsellor> getInfoOfCampaignCounsellor(Long orgId, Long campId, Map<String, String> input) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String name;
        if (input.containsKey("name")) {
            name = input.get("name");
        } else {
            return ResponseEntity.badRequest().build();
        }
        var campaign = camp_o.get();
        for (Long part_id : camp_o.get().getCounsellor_ids()) {
            var p_o = counsellorRepository.findById(part_id);
            if (p_o.isPresent()) {
                if (p_o.get().getName().equals(name)) {
                    return ResponseEntity.ok(p_o.get());
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
}
