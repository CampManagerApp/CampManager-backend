package camp.CampManager.organisation;

import camp.CampManager.organisation.campaign.CampaignService;
import camp.CampManager.organisation.campaign.counsellors.CounsellorRepository;
import camp.CampManager.security.HashCreator;
import camp.CampManager.users.Membership;
import camp.CampManager.users.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrganisationService {

    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    private CampaignService campaignService;

    @Autowired
    private HashCreator hashCreator;
    @Autowired
    private CounsellorRepository counsellorRepository;
    @Autowired
    private MembershipRepository membershipRepository;

    public List<Organisation> getAllOrganisations() {
        return (List<Organisation>) organisationRepository.findAll();
    }

    public Optional<Organisation> getOrganisationById(Long id) {
        return organisationRepository.findById(id);
    }

    public Optional<Organisation> getOrganisationByName(String name) {
        return organisationRepository.findByName(name);
    }

    public void deleteOrganisation(Organisation organisation) {
        for (Long campaignId : organisation.getCampaign_ids()) {
            var campaign_o = campaignService.findCampaignByIdAndOrganisationId(campaignId, organisation.getId());
            if (campaign_o.isEmpty()) {
                continue;
            }
            campaignService.deleteCampaign(campaign_o.get());
        }
        for (Membership membership : membershipRepository.findByOrganisationIdEquals(organisation.getId())) {
            membershipRepository.deleteById(membership.getId());
        }
        organisationRepository.deleteById(organisation.getId());
    }

    public void createOrganisation(Organisation organisation) throws Exception {
        organisation.setCampaign_ids(new LinkedList<>());
        Optional<Organisation> doesItExist = organisationRepository.findByName(organisation.getName());
        if (doesItExist.isPresent()) {
            throw new Exception();
        }
        organisationRepository.save(organisation);
    }

    public boolean updateOrganisation(Long id, Organisation organisation) {
        Optional<Organisation> opt = getOrganisationById(id);
        if (opt.isPresent()) {
            opt.get().setName(organisation.getName());
            opt.get().setManager(organisation.getManager());
            opt.get().setDescription(organisation.getDescription());
            organisationRepository.save(opt.get());
            return true;
        } else {
            return false;
        }
    }

    public Optional<Organisation> findOrganisationByName(String orgname) {
        return organisationRepository.findByName(orgname);
    }

    public Optional<Organisation> findOrganisationById(Long organisationId) {
        return organisationRepository.findById(organisationId);
    }
}
