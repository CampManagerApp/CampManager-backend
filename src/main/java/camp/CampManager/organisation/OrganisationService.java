package camp.CampManager.organisation;

import camp.CampManager.security.HashCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrganisationService {

    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    private HashCreator hashCreator;

    public List<Organisation> getAllOrganisations() {
        return (List<Organisation>) organisationRepository.findAll();
    }

    public Optional<Organisation> getOrganisationById(Long id) {
        return organisationRepository.findById(id);
    }

    public Optional<Organisation> getOrganisationByCode(String code) {
        return organisationRepository.findByCode(code);
    }

    public Optional<Organisation> getOrganisationByName(String name) {
        return organisationRepository.findByName(name);
    }

    public void deleteOrganisationById(Long id) {
        organisationRepository.deleteById(id);
    }

    public void createOrganisation(Organisation organisation) {
        organisationRepository.save(organisation);
        try {
            organisation.setOrganisationCode(hashCreator.createMD5Hash(organisation.getId().toString()));
        } catch (NoSuchAlgorithmException ignored) {

        }
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

    public Optional<Organisation> findOrganisationByCode(String code) {
        return organisationRepository.findByCode(code);
    }

    public Optional<Organisation> findOrganisationById(Long organisationId) {
        return organisationRepository.findById(organisationId);
    }
}
