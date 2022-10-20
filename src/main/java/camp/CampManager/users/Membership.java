package camp.CampManager.users;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Membership {
    @EmbeddedId
    MembershipKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("orgId")
    @JoinColumn(name = "org_id")
    Organisation organisation;

    // "MEMBER", "DMIN", or both
    @Column
    @ElementCollection(targetClass = String.class)
    Set<String> roles;
}
