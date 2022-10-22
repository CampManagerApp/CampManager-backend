package camp.CampManager;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationService;
import camp.CampManager.users.User;
//import camp.CampManager.users.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CampManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampManagerApplication.class, args);
    }
    /*
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     */
}
