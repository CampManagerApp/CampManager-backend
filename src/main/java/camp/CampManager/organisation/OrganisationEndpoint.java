package camp.CampManager.organisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/organisation/")
public class OrganisationEndpoint {

    @Autowired
    private OrganisationService organisationService;

    @GetMapping(path = "/")
    @ResponseBody
    public ResponseEntity<List<Organisation>> getAllOrganisations(){
        return ResponseEntity.ok(organisationService.getAllOrganisations());
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Organisation> getOrganisation(@PathVariable("id") Long id){
        Optional<Organisation> opt = organisationService.getOrganisationById(id);
        // This is just an if opt.isPresent return ok else return not found
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Organisation> deleteOrganisation(@PathVariable("id") Long id){
        organisationService.deleteOrganisationById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/")
    @ResponseBody
    public ResponseEntity<Organisation> createOrganisation(@RequestParam("name") String name){
        organisationService.createOrganisation(name);
        Optional<Organisation> opt = organisationService.getOrganisationByName(name);
        // This is just an if opt.isPresent return ok else return not found
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Organisation> updateOrganisation(@PathVariable("id") Long id,
                                                           @RequestParam("name") String name){
        if(organisationService.updateOrganisation(id, name)){
            Optional<Organisation> opt = organisationService.getOrganisationById(id);
            if (opt.isPresent()){
                return ResponseEntity.ok(opt.get());
            }
        }
        return ResponseEntity.notFound().build();
    }
}
