package camp.CampManager.security;

import camp.CampManager.filter.CustomAuthenticationFilter;
import camp.CampManager.users.User;
import camp.CampManager.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserRepository userRepository;


    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new UsersDetailsService();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationConfiguration authenticationConfiguration = http.getSharedObject(AuthenticationConfiguration.class);
        http.authenticationManager(authenticationManager(authenticationConfiguration));
        CustomAuthenticationFilter customFilter = new CustomAuthenticationFilter(authenticationManager((authenticationConfiguration)));
        customFilter.setFilterProcessesUrl("/api/login/");

        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/login/**").permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();

        http.addFilter(customFilter);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    CommandLineRunner run() {
        return args -> {
            PasswordEncoder bCrypt = passwordEncoder();
            User admin = User.builder().email("admin@admin.com").username("admin")
                    .password(passwordEncoder().encode("admin")).role("ADMIN").build();
            User user1 = User.builder().email("jim@dundermifflin.com").username("user1")
                    .password(passwordEncoder().encode("user1")).role("USER").build();
            User user2 = User.builder().email("dwight@dundermifflin.com").username("user2")
                    .password(passwordEncoder().encode("user2")).role("USER").build();

            userRepository.saveAll(List.of(admin, user1, user2));
        };
    }
}
