package camp.CampManager.users;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/*
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Membership {
    @EmbeddedId MembershipKey id = new MembershipKey();

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("userId")
    @JoinColumn(name = "student_id")
    User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("orgId")
    @JoinColumn(name = "organisation_id")
    Organisation organisation;

    // "MEMBER", "ADMIN", or both
    @Column
    @ElementCollection(targetClass = String.class)
    Set<String> roles;
}
 */
