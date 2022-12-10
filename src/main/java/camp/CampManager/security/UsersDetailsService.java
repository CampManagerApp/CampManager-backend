package camp.CampManager.security;

import camp.CampManager.users.CampUser;
import camp.CampManager.users.Membership;
import camp.CampManager.users.MembershipRepository;
import camp.CampManager.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class UsersDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MembershipRepository membershipRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CampUser> user_o = userRepository.findByUsername(username);
        if (user_o.isPresent()) {
            CampUser user = user_o.get();
            List<Membership> memberships = membershipRepository.findByUserIdEquals(user.getId());
            // TODO Obtenir totes les authorities necessaries
            Collection<SimpleGrantedAuthority> authorities = new LinkedList<>();
            for (Membership membership : memberships) {
                authorities.add(new SimpleGrantedAuthority(membership.getOrganisationId().toString() + "TEST"));
            }
            System.out.println("AUTHORITIES: " + authorities);
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(authorities)
                    .build();
        }
        throw new UsernameNotFoundException("Username not in db");
    }
}
