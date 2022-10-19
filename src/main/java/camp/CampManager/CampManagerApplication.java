package camp.CampManager;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationService;
import camp.CampManager.users.Role;
import camp.CampManager.users.User;
import camp.CampManager.users.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.LinkedList;

@SpringBootApplication
public class CampManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampManagerApplication.class, args);
    }
    @Bean
    CommandLineRunner run(UserService userService, OrganisationService organisationService) {
        return args -> {
            var org1 = new Organisation(null, "organisation1", null, new LinkedList<>());
            var org2 = new Organisation(null, "organisation2", null, new LinkedList<>());
            organisationService.createOrganisation(org1);
            organisationService.createOrganisation(org2);

            var role1 = new Role(null, org1, null, "ROLE_BASIC");
            var role2 = new Role(null, org2, null, "ROLE_ADMIN");
            userService.saveRole(role1);
            userService.saveRole(role2);

            var user = User.builder().username("usuari").password("password").roles(new LinkedList<>()).build();
            userService.saveUser(user);

            userService.addRoleToUser(user, role1);
            userService.addRoleToUser(user, role2);
        };
    }
}
