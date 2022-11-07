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
import java.util.Map;

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
    public ResponseEntity<DisplayMembership> getRolesOfNameInOrg(@RequestBody Map<String, String> input) {
        var fullname = input.get("fullname");
        var orgname = input.get("orgname");
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var organisation = organisation_o.get();
        var membership = nameService.findNameMembership(fullname, organisation);
        return membership.map(value -> ResponseEntity.ok(displayService.nameMembershipToDisplay(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/role/")
    @ResponseBody
    public ResponseEntity<String> updateMembershipToName(@RequestBody Map<String, String> input) {
        var fullname = input.get("fullname");
        var orgname = input.get("orgname");
        var is_admin = Boolean.parseBoolean(input.get("is_admin"));
        var is_member = Boolean.parseBoolean(input.get("is_member"));
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var organisation = organisation_o.get();
        var memb = nameService.findNameMembership(fullname, organisation);
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
    public ResponseEntity<String> createMembershipOfName(@RequestBody Map<String, String> input) throws URISyntaxException {
        var fullname = input.get("fullname");
        var orgname = input.get("orgname");
        var is_admin = Boolean.parseBoolean(input.get("is_admin"));
        var is_member = Boolean.parseBoolean(input.get("is_member"));
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var organisation = organisation_o.get();
        var memb = nameService.findNameMembership(fullname, organisation);
        if (memb.isPresent()) {
            return ResponseEntity.badRequest().build();
        } else {
            nameService.addMembershipToName(fullname, organisation, is_admin, is_member);
            return ResponseEntity.created(new URI("/users/role")).build();
        }
    }

    @DeleteMapping(path = "/role/")
    @ResponseBody
    public ResponseEntity<String> deleteMembershipOfName(@RequestBody Map<String, String> input) {
        var fullname = input.get("fullname");
        var orgname = input.get("orgname");
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var organisation = organisation_o.get();
        var memb = nameService.findNameMembership(fullname, organisation);
        if (memb.isPresent()) {
            var memb_obj = memb.get();
            userService.deleteMembership(memb_obj);
            return ResponseEntity.ok("Deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/role/claim")
    @ResponseBody
    public ResponseEntity<String> claimMembershipOfName(@RequestBody Map<String, String> input) {
        var fullname = input.get("fullname");
        var orgname = input.get("orgname");
        var username = input.get("username");
        var organisation_o = organisationService.findOrganisationByName(orgname);
        var user_o = userService.findUserByUsername(username);
        if (user_o.isEmpty() || organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var organisation = organisation_o.get();
        var user = user_o.get();
        var memb = nameService.findNameMembership(fullname, organisation);
        if (memb.isPresent()) {
            var m = memb.get();
            m.setUserId(user.getId());
            m.set_claimed(true);
            userService.saveMembership(m);
            return ResponseEntity.ok("Claimed");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
