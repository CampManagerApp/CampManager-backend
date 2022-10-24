package camp.CampManager.security;

import camp.CampManager.users.CampUser;
import camp.CampManager.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;


public class UsersDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CampUser> user_o = userRepository.findByUsername(username);
        if (user_o.isPresent()) {
            CampUser campUser = user_o.get();
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>(
                    Collections.singleton(new SimpleGrantedAuthority(campUser.getRole())));
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(campUser.getUsername())
                    .password(campUser.getPassword())
                    .authorities(authorities)
                    .build();
        }
        throw new UsernameNotFoundException("Username not in db");
    }
}
