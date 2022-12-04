package camp.CampManager;

//import camp.CampManager.users.UserService;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationService;
import camp.CampManager.organisation.campaign.Campaign;
import camp.CampManager.users.CampUser;
import camp.CampManager.users.Membership;
import camp.CampManager.users.UserService;
import camp.CampManager.users.names.NameService;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
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
    public StorageProvider storageProvider(JobMapper jobMapper) {
        InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }

    @Bean
    CommandLineRunner initializeData(OrganisationService organisationService,
                                     NameService nameService,
                                     UserService userService) {
        return args -> {
            CampUser joel = CampUser.builder().email("joel@joel.com").username("joelaumedes").full_name("Joel Aumedes").build();
            CampUser mireia = CampUser.builder().email("mireia@joel.com").username("mireiacalvet").full_name("Mireia Calvet").build();
            CampUser mariona = CampUser.builder().email("mariona@joel.com").username("marionavillaro").full_name("Mariona Villaró").build();
            CampUser robert = CampUser.builder().email("robert@joel.com").username("robertcreus").full_name("Robert Creus").build();

            userService.saveUser(joel);
            userService.saveUser(mireia);
            userService.saveUser(mariona);
            userService.saveUser(robert);

            Organisation xinoXano = new Organisation("Xino-Xano", "Miquel Aumedes", "Esplai Xino-Xano Bellvís");
            Organisation sio = new Organisation("Sió", "Ares Miró", "Esplai Sió Agramunt");
            organisationService.createOrganisation(xinoXano);
            organisationService.createOrganisation(sio);

            nameService.addMembershipToName("Joel Aumedes Serrano", xinoXano, false, true);
            nameService.addMembershipToName("Miquel Aumedes Serrano", xinoXano, true, true);
            nameService.addMembershipToName("Mireia Calvet Rubió", xinoXano, true, false);

            nameService.addMembershipToName("Mariona Villaró", sio, false, true);
            nameService.addMembershipToName("Robert Creus Tella", sio, true, true);
            nameService.addMembershipToName("Ares Miró", sio, true, false);

            Membership membershipJoel = nameService.findNameMembership("Joel Aumedes Serrano", xinoXano).get();
            membershipJoel.setUserId(joel.getId());
            membershipJoel.set_claimed(true);
            userService.saveMembership(membershipJoel);
            Membership membershipMireia = nameService.findNameMembership("Mireia Calvet Rubió", xinoXano).get();
            membershipMireia.setUserId(mireia.getId());
            membershipMireia.set_claimed(true);
            userService.saveMembership(membershipMireia);

            Membership membershipMariona = nameService.findNameMembership("Mariona Villaró", sio).get();
            membershipMariona.setUserId(mariona.getId());
            membershipMariona.set_claimed(true);
            userService.saveMembership(membershipMariona);
            Membership membershipRobert = nameService.findNameMembership("Robert Creus Tella", sio).get();
            membershipRobert.setUserId(robert.getId());
            membershipRobert.set_claimed(true);
            userService.saveMembership(membershipRobert);

            Campaign xinoXanoColonies = Campaign.builder().build();
        };
    }
}
