package camp.CampManager.users;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationRepository;
import camp.CampManager.organisation.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.RowSet;
import java.util.*;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(CampUser campUser) {
        campUser.setPassword(passwordEncoder.encode(campUser.getPassword()));
        userRepository.save(campUser);
    }

    public List<CampUser> getUsers(){
        var users = new LinkedList<CampUser>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public boolean addMembershipToUser(CampUser user, Organisation organisation, boolean is_admin, boolean is_member) {
        //Check if jpk exists, if it does return error
        var current_memb = membershipRepository.findByUserIdEqualsAndOrganisationIdEquals(user.getId(), organisation.getId());
        if (current_memb.isPresent()){
            return false;
        }
        //Create if ok
        var membership = Membership.builder()
            .userId(user.getId())
            .organisationId(organisation.getId())
            .is_admin(is_admin)
            .is_member(is_member)
            .build();
        membershipRepository.save(membership);
        return true;
    }

    public List<Membership> findUserMemberships(CampUser user) {
        return membershipRepository.findByUserIdEquals(user.getId());
    }

    public Optional<CampUser> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<Membership> findUserMembership(CampUser user, Organisation organisation) {
        return membershipRepository.findByUserIdEqualsAndOrganisationIdEquals(user.getId(), organisation.getId());
    }

    public Optional<CampUser> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public List<Membership> findOrganisationMemberships(Organisation org) {
        return membershipRepository.findByOrganisationIdEquals(org.getId());
    }
}
