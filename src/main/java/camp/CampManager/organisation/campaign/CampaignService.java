package camp.CampManager.organisation.campaign;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationRepository;
import camp.CampManager.organisation.campaign.activities.ActivitiesRepository;
import camp.CampManager.organisation.campaign.activities.Activity;
import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.counsellors.CounsellorRepository;
import camp.CampManager.organisation.campaign.participants.Participant;
import camp.CampManager.organisation.campaign.participants.ParticipantRepository;
import camp.CampManager.organisation.campaign.tables.CampTable;
import camp.CampManager.organisation.campaign.tables.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private CounsellorRepository counsellorRepository;

    @Autowired
    private TableService tableService;
    @Autowired
    private ActivitiesRepository activitiesRepository;

    public boolean saveCampaign(Campaign campaign) {
        if (campaignRepository.findByCampaignNameEqualsAndOrganisationIdEquals(campaign.getCampaignName(), campaign.getOrganisationId()).isPresent()) {
            return false;
        } else {
            var organisation_o = organisationRepository.findById(campaign.getOrganisationId());
            if (organisation_o.isEmpty()) {
                return false;
            }
            campaignRepository.save(campaign);
            var organisation = organisation_o.get();
            var ids = organisation.getCampaign_ids();
            ids.add(campaign.getId());
            organisation.setCampaign_ids(ids);
            organisationRepository.save(organisation);
            return true;
        }
    }

    public Optional<Campaign> findCampaignByName(String campaign_name) {
        return campaignRepository.findByCampaignNameEquals(campaign_name);
    }

    public boolean saveModifiedCampaign(Campaign campaign) {
        if (campaignRepository.findByCampaignNameEqualsAndOrganisationIdEquals(campaign.getCampaignName(), campaign.getOrganisationId()).isPresent()) {
            campaignRepository.save(campaign);
            return true;
        } else {
            return false;
        }
    }

    public Optional<Campaign> findCampaignByNameAndOrganisationId(String campaign_name, Long id) {
        return campaignRepository.findByCampaignNameEqualsAndOrganisationIdEquals(campaign_name, id);
    }

    public void deleteCampaign(Campaign campaign) {
        for (Long partId : campaign.getParticipant_ids()) {
            participantRepository.deleteById(partId);
        }
        for (Long cId : campaign.getCounsellor_ids()) {
            counsellorRepository.deleteById(cId);
        }
        Optional<Organisation> organisation_o = organisationRepository.findById(campaign.getOrganisationId());
        if (organisation_o.isPresent()) {
            if (organisation_o.get().getCampaign_ids().contains(campaign.getId())) {
                var ids = organisation_o.get().getCampaign_ids();
                for (int i = 0; i < ids.size(); i++) {
                    if (Objects.equals(ids.get(i), campaign.getId())) {
                        ids.remove(i);
                        break;
                    }
                }
                organisation_o.get().setCampaign_ids(ids);
                organisationRepository.save(organisation_o.get());
            }
        }
        for (Activity activity : activitiesRepository.findByCampaignIdEquals(campaign.getId())) {
            activitiesRepository.delete(activity);
        }
        for (CampTable campTable : Objects.requireNonNull(tableService.getAllTablesOfCampaign(campaign.getOrganisationId(), campaign.getId()).getBody())) {
            tableService.deleteTable(campTable);
        }
        campaignRepository.deleteById(campaign.getId());
    }

    public List<Campaign> findCampaignsByOrganisationId(Long organisation_id) {
        return campaignRepository.findByOrganisationIdEquals(organisation_id);
    }

    public void addParticipantToCampaign(Campaign campaign, Participant participant) {
        var ids = campaign.getParticipant_ids();
        if (!ids.contains(participant.getId())) {
            ids.add(participant.getId());
            campaign.setParticipant_ids(ids);
            campaignRepository.save(campaign);
        }
    }

    public void deleteParticipantFromCampaign(Campaign campaign, Participant participant) {
        campaign.getParticipant_ids().remove(participant.getId());
        campaignRepository.save(campaign);
    }

    public void addCounsellorToCampaign(Campaign campaign, Counsellor counsellor) {
        var ids = campaign.getCounsellor_ids();
        if (!ids.contains(counsellor.getId())) {
            ids.add(counsellor.getId());
            campaign.setCounsellor_ids(ids);
            campaignRepository.save(campaign);
        }
    }

    public void deleteCounsellorFromCampaign(Campaign campaign, Counsellor counsellor) {
        campaign.getCounsellor_ids().remove(counsellor.getId());
        campaignRepository.save(campaign);
    }

    public Optional<Campaign> findCampaignByIdAndOrganisationId(Long id, Long orgId) {
        return campaignRepository.findByIdEqualsAndOrganisationIdEquals(id, orgId);
    }
}
