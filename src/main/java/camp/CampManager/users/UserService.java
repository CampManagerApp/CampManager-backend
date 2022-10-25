package camp.CampManager.users;

import camp.CampManager.organisation.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void addMembershipToUser(Long user_id, Long org_id, String role) {
        var usr = userRepository.findById(user_id);
        var orgo = organisationRepository.findById(org_id);
        if(usr.isPresent() && orgo.isPresent()){
            // TODO: Mirar de no duplicar la "clau primaria" de membership
            var user = usr.get();
            var org = orgo.get();
            var key = new MembershipKey(user_id, org_id);
            var membership = Membership.builder()
                    .userId(user_id)
                    .organisationId(org_id)
                    .is_member(true)
                    .is_admin(false)
                    .build();
            membershipRepository.save(membership);
        }
    }

    public List<Membership> findUserMemberships(CampUser user) {
        return membershipRepository.findByUserIdEquals(user.getId());
    }
}
