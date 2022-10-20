package camp.CampManager.users;

import camp.CampManager.organisation.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/user/")
public class UserEndpoint {

    @Autowired
    public UserService userService;

    @GetMapping(path = "/")
    @ResponseBody
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
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
