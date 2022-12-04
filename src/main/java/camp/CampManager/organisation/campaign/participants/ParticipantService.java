package camp.CampManager.organisation.campaign.participants;

import camp.CampManager.organisation.campaign.CampaignRepository;
import camp.CampManager.organisation.campaign.CampaignService;
import camp.CampManager.users.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

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

    public ResponseEntity<String> addNewParticipantToCampaign(Long orgId, Long campId, Map<String, String> input) throws URISyntaxException, ParseException {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var participant_builder = Participant.builder();
        if (input.containsKey("fullName")) {
            if (participantRepository.existsByFullName(input.get("fullName"))) {
                return ResponseEntity.badRequest().body("Participant already exists");
            }
            participant_builder.fullName(input.get("fullName"));
        } else {
            return ResponseEntity.badRequest().body("Name of participant missing");
        }
        if (input.containsKey("name")) {
            participant_builder.name(input.get("name"));
        }
        if (input.containsKey("surnames")) {
            participant_builder.surnames(input.get("surnames"));
        }
        if (input.containsKey("gender")) {
            participant_builder.gender(Gender.valueOf(input.get("gender")));
        }
        if (input.containsKey("birthday")) {
            participant_builder.birthday(formatter.parse(input.get("birthday")));
        }
        if (input.containsKey("school_year")) {
            participant_builder.school_year(input.get("school_year"));
        }
        if (input.containsKey("parentOneFullName")) {
            participant_builder.parentOneFullName(input.get("parentOneFullName"));
        }
        if (input.containsKey("parentTwoFullName")) {
            participant_builder.parentTwoFullName(input.get("parentTwoFullName"));
        }
        if (input.containsKey("contactEmailOne")) {
            participant_builder.contactEmailOne(input.get("contactEmailOne"));
        }
        if (input.containsKey("contactEmailTwo")) {
            participant_builder.contactEmailTwo(input.get("contactEmailTwo"));
        }
        if (input.containsKey("phoneNumberOne")) {
            participant_builder.phoneNumberOne(Integer.parseInt(input.get("phoneNumberOne")));
        }
        if (input.containsKey("phoneNumberTwo")) {
            participant_builder.phoneNumberTwo(Integer.parseInt(input.get("phoneNumberTwo")));
        }
        if (input.containsKey("phoneNumberFix")) {
            participant_builder.phoneNumberFix(Integer.parseInt(input.get("phoneNumberFix")));
        }
        if (input.containsKey("bankIBAN")) {
            participant_builder.bankIBAN(input.get("bankIBAN"));
        }
        if (input.containsKey("houseAddress")) {
            participant_builder.houseAddress(input.get("houseAddress"));
        }
        if (input.containsKey("postalCode")) {
            participant_builder.postalCode(Integer.parseInt(input.get("postalCode")));
        }
        if (input.containsKey("townName")) {
            participant_builder.townName(input.get("townName"));
        }
        if (input.containsKey("county")) {
            participant_builder.county(input.get("county"));
        }
        if (input.containsKey("healthCardCIP")) {
            participant_builder.healthCardCIP(input.get("healthCardCIP"));
        }
        if (input.containsKey("insuranceName")) {
            participant_builder.insuranceName(input.get("insuranceName"));
        }
        if (input.containsKey("foodAffection")) {
            participant_builder.foodAffection(input.get("foodAffection"));
        }
        if (input.containsKey("nonFoodAffection")) {
            participant_builder.nonFoodAffection(input.get("nonFoodAffection"));
        }
        if (input.containsKey("medicalObservations")) {
            participant_builder.medicalObservations(input.get("medicalObservations"));
        }
        if (input.containsKey("ibuprofen")) {
            participant_builder.ibuprofen(Boolean.parseBoolean(input.get("ibuprofen")));
        }
        if (input.containsKey("paracetamol")) {
            participant_builder.paracetamol(Boolean.parseBoolean(input.get("paracetamol")));
        }
        if (input.containsKey("specialMedication")) {
            participant_builder.specialMedication(input.get("specialMedication"));
        }
        if (input.containsKey("medicationGuide")) {
            participant_builder.medicationGuide(input.get("medicationGuide"));
        }
        if (input.containsKey("additionalInformation")) {
            participant_builder.additionalInformation(input.get("additionalInformation"));
        }
        var participant_instance = participant_builder.build();
        participantRepository.save(participant_instance);
        campaignService.addParticipantToCampaign(camp_o.get(), participant_instance);
        return ResponseEntity.created(new URI("/organisation/id/campaign/id/participant")).build();
    }

    public void addParticipantObjectToCampaign(Long orgId, Long campId, Participant participant) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return;
        }
        participantRepository.save(participant);
        campaignService.addParticipantToCampaign(camp_o.get(), participant);
    }

    public ResponseEntity<String> deleteParticipantFromCampaign(Long orgId, Long campId, Map<String, String> input) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String name;
        if (input.containsKey("fullName")) {
            name = input.get("fullName");
        } else {
            return ResponseEntity.badRequest().body("Full name of participant missing");
        }
        var campaign = camp_o.get();
        var new_ids = campaign.getParticipant_ids();
        Participant to_delete = null;
        for (Long part_id : campaign.getParticipant_ids()) {
            var p_o = participantRepository.findById(part_id);
            if (p_o.isPresent()) {
                if (p_o.get().getFullName().equals(name)) {
                    to_delete = p_o.get();
                }
            }
        }
        if (to_delete == null) {
            return ResponseEntity.notFound().build();
        }
        campaignService.deleteParticipantFromCampaign(campaign, to_delete);
        participantRepository.deleteById(to_delete.getId());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Participant> getInfoOfCampaignParticipant(Long orgId, Long campId, Map<String, String> input) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String name;
        if (input.containsKey("fullName")) {
            name = input.get("fullName");
        } else {
            return ResponseEntity.badRequest().build();
        }
        var campaign = camp_o.get();
        for (Long part_id : camp_o.get().getParticipant_ids()) {
            var p_o = participantRepository.findById(part_id);
            if (p_o.isPresent()) {
                if (p_o.get().getFullName().equals(name)) {
                    return ResponseEntity.ok(p_o.get());
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
}
