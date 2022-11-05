package camp.CampManager.users.names;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationRepository;
import camp.CampManager.users.Membership;
import camp.CampManager.users.MembershipRepository;
import camp.CampManager.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class NameService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    private MembershipRepository membershipRepository;

    public Optional<Membership> findNameMembership(String username, Organisation organisation) {
        return membershipRepository.findByFullnameEqualsAndOrganisationIdEquals(username, organisation.getId());
    }

    public void addMembershipToName(String username, Organisation organisation, boolean is_admin, boolean is_member) {
        membershipRepository.save(Membership.builder()
                .fullname(username)
                .organisationId(organisation.getId())
                .is_admin(is_admin)
                .is_member(is_member)
                .is_claimed(false)
                .build());
    }
}
