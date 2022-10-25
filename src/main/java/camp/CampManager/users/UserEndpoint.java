package camp.CampManager.users;

import camp.CampManager.display.DisplayService;
import camp.CampManager.display.DisplayUser;
import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "/role/")
    @ResponseBody
    public ResponseEntity<String> createMembershipToUser(@RequestBody CampUser user,
                                                         @RequestBody Organisation organisation,
                                                         @RequestParam("is_admin") boolean is_admin,
                                                         @RequestParam("is_member") boolean is_member) {
        // !!!! Duplicating pk
        var membership = Membership.builder()
                .userId(user.getId())
                .organisationId(organisation.getId())
                .is_admin(is_admin)
                .is_member(is_member)
                .build();
        return ResponseEntity.ok("Membership created");
    }
}
