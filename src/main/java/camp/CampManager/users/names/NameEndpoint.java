package camp.CampManager.users.names;

import camp.CampManager.display.DisplayMembership;
import camp.CampManager.display.DisplayService;
import camp.CampManager.organisation.OrganisationService;
import camp.CampManager.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@CrossOrigin
@RequestMapping("/names/")
public class NameEndpoint {
    @Autowired
    private UserService userService;
    @Autowired
    public OrganisationService organisationService;
    @Autowired
    public DisplayService displayService;
    @Autowired
    public NameService nameService;

    @GetMapping(path = "/role/")
    @ResponseBody
    public ResponseEntity<DisplayMembership> getRolesOfNameInOrg(@RequestParam("username") String username,
                                                                 @RequestParam("orgname") String orgname) {
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var organisation = organisation_o.get();
        var membership = nameService.findNameMembership(username, organisation);
        return membership.map(value -> ResponseEntity.ok(displayService.nameMembershipToDisplay(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/role/")
    @ResponseBody
    public ResponseEntity<String> updateMembershipToName(@RequestParam("username") String username,
                                                         @RequestParam("orgname") String orgname,
                                                         @RequestParam("is_admin") boolean is_admin,
                                                         @RequestParam("is_member") boolean is_member) throws URISyntaxException {
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var organisation = organisation_o.get();
        var memb = nameService.findNameMembership(username, organisation);
        if (memb.isPresent()) {
            var memb_obj = memb.get();
            memb_obj.set_admin(is_admin);
            memb_obj.set_member(is_member);
            userService.saveMembership(memb_obj);
            return ResponseEntity.ok("Updated");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/role/")
    @ResponseBody
    public ResponseEntity<String> createMembershipOfName(@RequestParam("username") String username,
                                                         @RequestParam("orgname") String orgname,
                                                         @RequestParam("is_admin") boolean is_admin,
                                                         @RequestParam("is_member") boolean is_member) throws URISyntaxException {
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var organisation = organisation_o.get();
        var memb = nameService.findNameMembership(username, organisation);
        if (memb.isPresent()) {
            return ResponseEntity.badRequest().build();
        } else {
            nameService.addMembershipToName(username, organisation, is_admin, is_member);
            return ResponseEntity.created(new URI("/users/role")).build();
        }
    }

    @DeleteMapping(path = "/role/")
    @ResponseBody
    public ResponseEntity<String> deleteMembershipOfName(@RequestParam("username") String username,
                                                         @RequestParam("orgname") String orgname) throws URISyntaxException {
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var organisation = organisation_o.get();
        var memb = nameService.findNameMembership(username, organisation);
        if (memb.isPresent()) {
            var memb_obj = memb.get();
            userService.deleteMembership(memb_obj);
            return ResponseEntity.ok("Deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
