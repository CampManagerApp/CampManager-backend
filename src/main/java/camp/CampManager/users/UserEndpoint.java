package camp.CampManager.users;

import camp.CampManager.display.DisplayMembership;
import camp.CampManager.display.DisplayService;
import camp.CampManager.display.DisplayUser;
import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/users/")
public class UserEndpoint {

    @Autowired
    public UserService userService;
    @Autowired
    public OrganisationService organisationService;
    @Autowired
    public DisplayService displayService;

    @GetMapping(path = "/")
    @ResponseBody
    public ResponseEntity<DisplayUser> findUser(@RequestParam("username") String username) {
        var user_o = userService.findUserByUsername(username);
        if (user_o.isPresent()){
            return ResponseEntity.ok(displayService.userToDisplay(user_o.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/all/")
    @ResponseBody
    public ResponseEntity<List<DisplayUser>> findAllUsers() {
        var users = userService.getUsers();
        var usersList = new LinkedList<DisplayUser>();
        users.forEach(
                user -> {
                    usersList.add(displayService.userToDisplay(user));
                }
        );
        return ResponseEntity.ok(usersList);
    }

    @PutMapping(path = "/role/")
    @ResponseBody
    public ResponseEntity<String> updateMembershipToUser(@RequestParam("username") String username,
                                                         @RequestParam("orgname") String orgname,
                                                         @RequestParam("is_admin") boolean is_admin,
                                                         @RequestParam("is_member") boolean is_member) throws URISyntaxException {
        var user_o = userService.findUserByUsername(username);
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (user_o.isEmpty() || organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var user = user_o.get();
        var organisation = organisation_o.get();
        var memb = userService.findUserMembership(user, organisation);
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
    public ResponseEntity<String> createMembershipOfUser(@RequestParam("username") String username,
                                                         @RequestParam("orgname") String orgname,
                                                         @RequestParam("is_admin") boolean is_admin,
                                                         @RequestParam("is_member") boolean is_member) throws URISyntaxException {
        var user_o = userService.findUserByUsername(username);
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (user_o.isEmpty() || organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var user = user_o.get();
        var organisation = organisation_o.get();
        var memb = userService.findUserMembership(user, organisation);
        if (memb.isPresent()) {
            return ResponseEntity.badRequest().build();
        } else {
            userService.addMembershipToUser(user, organisation, is_admin, is_member);
            return ResponseEntity.created(new URI("/users/role")).build();
        }
    }

    @DeleteMapping(path = "/role/")
    @ResponseBody
    public ResponseEntity<String> deleteMembershipOfUser(@RequestParam("username") String username,
                                                         @RequestParam("orgname") String orgname) throws URISyntaxException {
        var user_o = userService.findUserByUsername(username);
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (user_o.isEmpty() || organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var user = user_o.get();
        var organisation = organisation_o.get();
        var memb = userService.findUserMembership(user, organisation);
        if (memb.isPresent()) {
            var memb_obj = memb.get();
            userService.deleteMembership(memb_obj);
            return ResponseEntity.ok("Deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/role/")
    @ResponseBody
    public ResponseEntity<DisplayMembership> getRolesOfUserInOrg(@RequestParam("username") String username,
                                                                 @RequestParam("orgname") String orgname) {
        var user_o = userService.findUserByUsername(username);
        var organisation_o = organisationService.findOrganisationByName(orgname);
        if (user_o.isEmpty() || organisation_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var user = user_o.get();
        var organisation = organisation_o.get();
        var membership = userService.findUserMembership(user, organisation);
        return membership.map(value -> ResponseEntity.ok(displayService.membershipToDisplay(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/")
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody CampUser user) throws URISyntaxException {
        var user_o = userService.findUserByUsername(user.getUsername());
        if (user_o.isPresent()) {
            return ResponseEntity.badRequest().build();
        } else {
            userService.saveUser(user);
            return ResponseEntity.created(new URI("/users/")).build();
        }
    }

    @GetMapping(path = "/memberships/")
    @ResponseBody
    public ResponseEntity<List<DisplayMembership>> getAllUserMemberships(@RequestParam("username") String username) {
        var user_o = userService.findUserByUsername(username);
        if (user_o.isPresent()) {
            var membs = userService.findUserMemberships(user_o.get());
            var displayMembs = new LinkedList<DisplayMembership>();
            membs.forEach(e->displayMembs.add(displayService.membershipToDisplay(e)));
            return ResponseEntity.ok(displayMembs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
