package camp.CampManager.users;

import camp.CampManager.organisation.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/")
public class UserEndpoint {

    @Autowired
    public UserService userService;

    @GetMapping(path = "/")
    @ResponseBody
    public ResponseEntity<List<DisplayUser>> getUsers() {
        var users = userService.getUsers();
        var usersList = new LinkedList<DisplayUser>();
        users.forEach(
                user -> {
                    var usr = DisplayUser.builder().
                            id(user.getId()).
                            username(user.getUsername())
                            .password(user.getPassword())
                            .email(user.getEmail())
                            .date_of_birth(user.getDate_of_birth())
                            .full_name(user.getFull_name())
                            .gender(user.getGender());
                    var orgs = new LinkedList<String>();
                    user.organisations.forEach(o -> orgs.add(o.organisation.getName()));
                    usr.organisations(orgs);
                    usersList.add(usr.build());
                }
        );
        return ResponseEntity.ok(usersList);
    }

    @PostMapping(path = "/role/")
    @ResponseBody
    public ResponseEntity<String> membershipToUser(@RequestParam("user") Long user_id,
                                                   @RequestParam("org") Long org_id,
                                                   @RequestParam("role") String role) {
        userService.addMembershipToUser(user_id, org_id, role);
        return ResponseEntity.ok("Good");
    }
}
