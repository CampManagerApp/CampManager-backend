package camp.CampManager;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationService;
import camp.CampManager.users.User;
import camp.CampManager.users.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CampManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampManagerApplication.class, args);
    }
    @Bean
    CommandLineRunner run(UserService userService, OrganisationService organisationService) {
        return args -> {
            User jim = User.builder().email("jim@dundermifflin.com").username("jimhalpert").build();
            userService.saveUser(jim);

            User dwight = User.builder().email("dwight@dundermifflin.com").username("dwighschrute").build();
            userService.saveUser(dwight);

            Organisation dunder = Organisation.builder().name("Dunder").build();
            organisationService.createOrganisation(dunder);

            Organisation mifflin = Organisation.builder().name("Mifflin").build();
            organisationService.createOrganisation(mifflin);

            userService.addMembershipToUser(jim.getId(), dunder.getId(), "USER");
            userService.addMembershipToUser(jim.getId(), dunder.getId(), "BOSS");
            userService.addMembershipToUser(dwight.getId(), dunder.getId(), "USER");

            userService.addMembershipToUser(jim.getId(), mifflin.getId(), "USER");
            userService.addMembershipToUser(dwight.getId(), mifflin.getId(), "BOSS");
            userService.addMembershipToUser(dwight.getId(), mifflin.getId(), "USER");
        };
    }
}
