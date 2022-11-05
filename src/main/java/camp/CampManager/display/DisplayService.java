package camp.CampManager.display;

import camp.CampManager.organisation.OrganisationService;
import camp.CampManager.users.CampUser;
import camp.CampManager.users.Membership;
import camp.CampManager.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class DisplayService {
    @Autowired
    private UserService userService;
    @Autowired
    private OrganisationService organisationService;

    public DisplayUser userToDisplay(CampUser user) {
        var display = DisplayUser.builder().
                id(user.getId()).
                username(user.getUsername())
                .email(user.getEmail())
                .date_of_birth(user.getDate_of_birth())
                .full_name(user.getFull_name())
                .gender(user.getGender());
        var displayMemberships = new LinkedList<DisplayMembershipUser>();
        var memberships = userService.findUserMemberships(user);
        for (Membership membership : memberships) {
            var displayMemb = DisplayMembershipUser.builder();
            var org = organisationService.getOrganisationById(membership.getOrganisationId());
            displayMemb.organisationName(org.get().getName());
            displayMemb.id(membership.getId());
            displayMemb.is_member(membership.is_member);
            displayMemb.is_admin(membership.is_admin);
            displayMemberships.add(displayMemb.build());
        }
        display.organisations(displayMemberships);
        return display.build();
    }

    public DisplayMembership membershipToDisplay(Membership membership) {
        return DisplayMembership.builder()
                .userName(userService.findUserById(membership.getUserId()).get().getUsername())
                .organisationName(organisationService.findOrganisationById(membership.getOrganisationId()).get().getName())
                .is_member(membership.is_member)
                .is_admin(membership.is_admin)
                .id(membership.getId()).build();
    }

    public DisplayUser nameMembershipToDisplayUser(Membership membership){
        return DisplayUser.builder()
                .full_name(membership.getFullname())
                .build();
    }
}
