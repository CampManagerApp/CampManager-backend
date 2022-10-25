package camp.CampManager.organisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service @Transactional
public class OrganisationService {

    @Autowired
    private OrganisationRepository organisationRepository;

    public List<Organisation> getAllOrganisations() {
        return (List<Organisation>) organisationRepository.findAll();
    }

    public Optional<Organisation> getOrganisationById(Long id){
        return organisationRepository.findById(id);
    }
    public Optional<Organisation> getOrganisationByName(String name){
        return organisationRepository.findByName(name);
    }

    public void deleteOrganisationById(Long id){
        organisationRepository.deleteById(id);
    }

    public void createOrganisation(Organisation organisation){
        organisationRepository.save(organisation);
    }

    public boolean updateOrganisation(Long id, Organisation organisation) {
        Optional<Organisation> opt = getOrganisationById(id);
        if(opt.isPresent()){
            opt.get().setName(organisation.getName());
            opt.get().setAdmin(organisation.getAdmin());
            organisationRepository.save(opt.get());
            return true;
        }else{
            return false;
        }
    }
}
