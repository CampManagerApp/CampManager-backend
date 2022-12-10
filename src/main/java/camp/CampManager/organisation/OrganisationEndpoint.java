package camp.CampManager.organisation;

import camp.CampManager.display.DisplayService;
import camp.CampManager.display.DisplayUser;
import camp.CampManager.security.HashCreator;
import camp.CampManager.users.Membership;
import camp.CampManager.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/organisation/")
public class OrganisationEndpoint {

    @Autowired
    private OrganisationService organisationService;
    @Autowired
    private UserService userService;
    @Autowired
    private DisplayService displayService;
    @Autowired
    private HashCreator hashCreator;

    @GetMapping(path = "/")
    @ResponseBody
    public ResponseEntity<List<Organisation>> getAllOrganisations() {
        return ResponseEntity.ok(organisationService.getAllOrganisations());
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Organisation> getOrganisation(@PathVariable("id") Long id) {
        Optional<Organisation> opt = organisationService.getOrganisationById(id);
        // This is just an if opt.isPresent return ok else return not found
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{id}/code")
    @ResponseBody
    public ResponseEntity<String> getOrganisationCode(@PathVariable("id") Long id) {
        Optional<Organisation> opt = organisationService.getOrganisationById(id);
        // This is just an if opt.isPresent return ok else return not found
        if (opt.isPresent()) {
            try {
                return ResponseEntity.ok(hashCreator.createMD5Hash(opt.get().getId().toString()));
            } catch (NoSuchAlgorithmException ignored) {
            }
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/code")
    @ResponseBody
    public ResponseEntity<Organisation> getOrganisationByCode(@RequestParam("code") String code) {
        for (Organisation org : organisationService.getAllOrganisations()) {
            try {
                if (code.equals(hashCreator.createMD5Hash(org.getId().toString()))) {
                    return ResponseEntity.ok(org);
                }
            } catch (NoSuchAlgorithmException ignored) {
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/name")
    @ResponseBody
    public ResponseEntity<Organisation> getOrganisationByName(@RequestParam("name") String name) {
        var org = organisationService.findOrganisationByName(name);
        return org.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Organisation> deleteOrganisation(@PathVariable("id") Long id) {
        organisationService.deleteOrganisationById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/")
    @ResponseBody
    public ResponseEntity<Organisation> createOrganisation(@RequestBody Organisation organisation) throws Exception {
        organisationService.createOrganisation(organisation);
        Optional<Organisation> opt = organisationService.getOrganisationByName(organisation.getName());
        // This is just an if opt.isPresent return ok else return not found
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Organisation> updateOrganisation(@PathVariable("id") Long id,
                                                           @RequestBody Organisation organisation) {
        if (organisationService.updateOrganisation(id, organisation)) {
            Optional<Organisation> opt = organisationService.getOrganisationById(id);
            if (opt.isPresent()) {
                return ResponseEntity.ok(opt.get());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/{id}/members")
    @ResponseBody
    public ResponseEntity<List<DisplayUser>> findMembersOfOrganisation(@PathVariable("id") Long orgId) {
        var organisation_o = organisationService.getOrganisationById(orgId);
        if (organisation_o.isPresent()) {
            var org = organisation_o.get();
            var memberships = userService.findOrganisationMemberships(org);
            var user_list = new LinkedList<DisplayUser>();
            for (Membership membership : memberships) {
                if (membership.is_claimed()) {
                    var user = userService.findUserById(membership.getUserId());
                    if (user.isEmpty()) {
                        return ResponseEntity.internalServerError().build();
                    }
                    user_list.add(displayService.userToDisplay(user.get()));
                } else {
                    user_list.add(displayService.nameMembershipToDisplayUser(membership));
                }
            }
            return ResponseEntity.ok(user_list);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{id}/users")
    @ResponseBody
    public ResponseEntity<List<DisplayUser>> findUsersOfOrganisation(@PathVariable("id") Long orgId) {
        var organisation_o = organisationService.getOrganisationById(orgId);
        if (organisation_o.isPresent()) {
            var org = organisation_o.get();
            var memberships = userService.findOrganisationMemberships(org);
            var user_list = new LinkedList<DisplayUser>();
            for (Membership membership : memberships) {
                if (membership.is_claimed()) {
                    var user = userService.findUserById(membership.getUserId());
                    if (user.isEmpty()) {
                        return ResponseEntity.internalServerError().build();
                    }
                    user_list.add(displayService.userToDisplay(user.get()));
                }
            }
            return ResponseEntity.ok(user_list);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{id}/names")
    @ResponseBody
    public ResponseEntity<List<DisplayUser>> findNamesOfOrganisation(@PathVariable("id") Long orgId) {
        var organisation_o = organisationService.getOrganisationById(orgId);
        if (organisation_o.isPresent()) {
            var org = organisation_o.get();
            var memberships = userService.findOrganisationMemberships(org);
            var user_list = new LinkedList<DisplayUser>();
            for (Membership membership : memberships) {
                if (!membership.is_claimed()) {
                    user_list.add(displayService.nameMembershipToDisplayUser(membership));
                }
            }
            return ResponseEntity.ok(user_list);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
