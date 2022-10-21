package camp.CampManager.users;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganisationRepository organisationRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getUsers() {
        var users = new LinkedList<User>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public void addMembershipToUser(Long user_id, Long org_n, String role) {
        /*
        Optional<User> user_o = userRepository.findById(user_id);
        Optional<Organisation> org_o = organisationRepository.findById(org_n);
        if(user_o.isPresent() && org_o.isPresent()){
            User user = user_o.get();
            Organisation org = org_o.get();
            var memberships = user.getOrganisations();
            Membership memb = null;
            for (Membership m : memberships){
                if (m.organisation.equals(org)){
                    memb = m;
                    break;
                }
            }
            if (memb != null){
                memb.roles.add(role);
            }else{
                Membership member = new Membership(null, user, org, new LinkedHashSet<>(Collections.singleton(role)));
                user.organisations.add(member);
                org.getMembers().add(member);
            }
        }
        */
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user_o = userRepository.findByUsername(username);
        if (user_o.isPresent()) {
            User user = user_o.get();
            var authorities = new LinkedList<SimpleGrantedAuthority>(Collections.singleton(new SimpleGrantedAuthority("ALL")));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("Username not in db");
        }
    }
}
