package camp.CampManager;

//import camp.CampManager.users.UserService;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationService;
import camp.CampManager.organisation.campaign.Campaign;
import camp.CampManager.organisation.campaign.CampaignService;
import camp.CampManager.organisation.campaign.activities.ActivitiesService;
import camp.CampManager.organisation.campaign.activities.Activity;
import camp.CampManager.organisation.campaign.activities.TimeOfActivity;
import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.counsellors.CounsellorService;
import camp.CampManager.organisation.campaign.participants.Participant;
import camp.CampManager.organisation.campaign.participants.ParticipantService;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class CampManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampManagerApplication.class, args);
    }

//     @Bean
//     public StorageProvider storageProvider(JobMapper jobMapper) {
//         InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
//         storageProvider.setJobMapper(jobMapper);
//         return storageProvider;
//     }

    @Bean
    CommandLineRunner initializeData(OrganisationService organisationService,
                                     NameService nameService,
                                     UserService userService,
                                     CampaignService campaignService,
                                     ParticipantService participantService,
                                     CounsellorService counsellorService,
                                     ActivitiesService activitiesService) {
        return args -> {
            CampUser joel = CampUser.builder()
                    .email("joel@joel.com")
                    .username("joelaumedes")
                    .full_name("Joel Aumedes")
                    .password("joel")
                    .role("SUPERADMIN")
                    .build();
            CampUser miquel = CampUser.builder()
                    .email("miquel@joel.com")
                    .username("miquelaumedes")
                    .full_name("Miquel Aumedes")
                    .password("miquel")
                    .role("ADMIN")
                    .build();
            CampUser mireia = CampUser.builder()
                    .email("mireia@joel.com")
                    .username("mireiacalvet")
                    .full_name("Mireia Calvet")
                    .password("mireia")
                    .role("USER")
                    .build();

            userService.saveUser(joel);
            userService.saveUser(miquel);
            userService.saveUser(mireia);


            /*
            CampUser luis = CampUser.builder()
                .email("luis@joel.com")
                .username("luis")
                .full_name("Luis")
                .password("luis")
                .role("SUPERADMIN")
                .build();

            CampUser moi = CampUser.builder()
                .email("moi@joel.com")
                .username("moi")
                .full_name("Moi")
                .password("moi")
                .role("ADMIN")
                .build();
            CampUser yareli = CampUser.builder()
                .email("yareli@joel.com")
                .username("yareli")
                .full_name("Yareli")
                .password("yareli")
                .role("ADMIN")
                .build();

            CampUser marc = CampUser.builder()
                .email("marc@joel.com")
                .username("marc")
                .full_name("marc")
                .password("marc")
                .role("ADMIN")
                .build();

            userService.saveUser(luis);
            userService.saveUser(moi);
            userService.saveUser(yareli);
            userService.saveUser(marc);
            */

            Organisation xinoXano = new Organisation("Xino-Xano", "Miquel Aumedes", "Esplai Xino-Xano Bellvís");
            Organisation sio = new Organisation("Sió", "Ares Miró", "Esplai Sió Agramunt");
            organisationService.createOrganisation(xinoXano);
            organisationService.createOrganisation(sio);

            nameService.addMembershipToName("Joel Aumedes Serrano", xinoXano, true, true);
            nameService.addMembershipToName("Miquel Aumedes Serrano", xinoXano, true, true);
            nameService.addMembershipToName("Mireia Calvet Rubió", xinoXano, false, false);

            
            Membership membershipMiquel = nameService.findNameMembership("Miquel Aumedes Serrano", xinoXano).get();
            membershipMiquel.setUserId(miquel.getId());
            membershipMiquel.set_claimed(true);
            userService.saveMembership(membershipMiquel);
            Membership membershipMireia = nameService.findNameMembership("Mireia Calvet Rubió", xinoXano).get();
            membershipMireia.setUserId(mireia.getId());
            membershipMireia.set_claimed(true);
            userService.saveMembership(membershipMireia);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            Campaign xinoXanoColonies = Campaign.builder()
                    .campaignName("Colònies Xino-Xano")
                    .startDate(formatter.parse("04-07-2023"))
                    .endDate(formatter.parse("20-07-2023"))
                    .organisationId(xinoXano.getId())
                    .build();

            Campaign xinoXanoJoves = Campaign.builder()
                    .campaignName("Joves Xino-Xano")
                    .startDate(formatter.parse("14-08-2023"))
                    .endDate(formatter.parse("20-08-2023"))
                    .organisationId(xinoXano.getId())
                    .build();

            Campaign sioColonies = Campaign.builder()
                    .campaignName("Colònies Sió")
                    .startDate(formatter.parse("04-07-2022"))
                    .endDate(formatter.parse("20-07-2022"))
                    .organisationId(sio.getId())
                    .build();

            campaignService.saveCampaign(xinoXanoColonies);
            campaignService.saveCampaign(xinoXanoJoves);
            campaignService.saveCampaign(sioColonies);

            Counsellor joelCounsellor = Counsellor.builder()
                    .fullName("Joel Aumedes Serrano")
                    .name("Joel")
                    .emergencyPhone(621215112)
                    .surnames("Aumedes")
                    .build();
            Counsellor mireiaCounsellor = Counsellor.builder()
                    .fullName("Mireia Calvet Rubió")
                    .name("Mireia")
                    .emergencyPhone(621215115)
                    .surnames("Mireia")
                    .build();

            counsellorService.addNewCounsellorObjectToCampaign(xinoXano.getId(), xinoXanoColonies.getId(), joelCounsellor);
            counsellorService.addNewCounsellorObjectToCampaign(xinoXano.getId(), xinoXanoColonies.getId(), mireiaCounsellor);
            counsellorService.addNewCounsellorObjectToCampaign(xinoXano.getId(), xinoXanoJoves.getId(), mireiaCounsellor);
        };
    }
}
