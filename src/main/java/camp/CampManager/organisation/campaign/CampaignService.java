package camp.CampManager.organisation.campaign;

import camp.CampManager.organisation.OrganisationRepository;
import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.participants.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private OrganisationRepository organisationRepository;

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
        if (campaignRepository.findByCampaignNameEqualsAndOrganisationIdEquals(campaign.getCampaignName(), campaign.getOrganisationId()).isPresent()){
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
        campaignRepository.deleteById(campaign.getId());
    }

    public List<Campaign> findCampaignsByOrganisationId(Long organisation_id) {
        return campaignRepository.findByOrganisationIdEquals(organisation_id);
    }

    public void addParticipantToCampaign(Campaign campaign, Participant participant) {
        var ids = campaign.getParticipant_ids();
        if (!ids.contains(participant.getId())){
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
        if (!ids.contains(counsellor.getId())){
            ids.add(counsellor.getId());
            campaign.setCounsellor_ids(ids);
            campaignRepository.save(campaign);
        }
    }

    public void deleteCounsellorFromCampaign(Campaign campaign, Counsellor counsellor) {
        campaign.getCounsellor_ids().remove(counsellor.getId());
        campaignRepository.save(campaign);
    }
}
