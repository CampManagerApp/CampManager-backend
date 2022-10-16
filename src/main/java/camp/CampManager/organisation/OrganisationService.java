package camp.CampManager.organisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
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

    public void createOrganisation(String name){
        organisationRepository.save(new Organisation(name));
    }

    public boolean updateOrganisation(Long id, String name) {
        Optional<Organisation> opt = getOrganisationById(id);
        if(opt.isPresent()){
            opt.get().setName(name);
            organisationRepository.save(opt.get());
            return true;
        }else{
            return false;
        }
    }
}
