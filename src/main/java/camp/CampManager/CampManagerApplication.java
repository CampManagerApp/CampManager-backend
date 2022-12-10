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

    @Bean
    public StorageProvider storageProvider(JobMapper jobMapper) {
        InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }

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
            CampUser mireia = CampUser.builder()
                    .email("mireia@joel.com")
                    .username("mireiacalvet")
                    .full_name("Mireia Calvet")
                    .password("mireia")
                    .role("SUPERADMIN")
                    .build();
            CampUser mariona = CampUser.builder()
                    .email("mariona@joel.com")
                    .username("marionavillaro")
                    .full_name("Mariona Villaró")
                    .password("mariona")
                    .role("SUPERADMIN")
                    .build();
            CampUser robert = CampUser.builder()
                    .email("robert@joel.com")
                    .username("robertcreus")
                    .full_name("Robert Creus")
                    .password("robert")
                    .role("SUPERADMIN")
                    .build();

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

            nameService.addMembershipToName("Mariona Villaró Vicens", sio, false, true);
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

            Membership membershipMariona = nameService.findNameMembership("Mariona Villaró Vicens", sio).get();
            membershipMariona.setUserId(mariona.getId());
            membershipMariona.set_claimed(true);
            userService.saveMembership(membershipMariona);
            Membership membershipRobert = nameService.findNameMembership("Robert Creus Tella", sio).get();
            membershipRobert.setUserId(robert.getId());
            membershipRobert.set_claimed(true);
            userService.saveMembership(membershipRobert);

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

            Participant p1 = Participant.builder()
                    .fullName("Pau Fuster")
                    .name("Pau")
                    .surnames("Fuster")
                    .foodAffection("Lactose Intolerant")
                    .build();
            Participant p2 = Participant.builder()
                    .fullName("Jose Luis Muñoz")
                    .name("Jose Luis")
                    .surnames("Muñoz")
                    .ibuprofen(false)
                    .build();
            Participant p3 = Participant.builder()
                    .fullName("Manu Carrer")
                    .name("Manu")
                    .surnames("Carrer")
                    .birthday(new Date())
                    .build();
            Participant p4 = Participant.builder()
                    .fullName("Xavier Compai")
                    .name("Xavier")
                    .surnames("Compai")
                    .contactEmailOne("compais@joel.com")
                    .build();
            Participant p5 = Participant.builder()
                    .fullName("Guillem Cadí")
                    .name("Guillem")
                    .surnames("Cadí")
                    .nonFoodAffection("Leg Injury")
                    .build();

            participantService.addParticipantObjectToCampaign(xinoXano.getId(), xinoXanoColonies.getId(), p1);
            participantService.addParticipantObjectToCampaign(xinoXano.getId(), xinoXanoColonies.getId(), p2);
            participantService.addParticipantObjectToCampaign(xinoXano.getId(), xinoXanoJoves.getId(), p3);
            participantService.addParticipantObjectToCampaign(sio.getId(), sioColonies.getId(), p4);
            participantService.addParticipantObjectToCampaign(sio.getId(), sioColonies.getId(), p5);

            Counsellor joelCounsellor = Counsellor.builder()
                    .fullName("Joel Aumedes Serrano")
                    .name("Joel")
                    .build();
            Counsellor mireiaCounsellor = Counsellor.builder()
                    .fullName("Mireia Calvet Rubió")
                    .name("Mireia")
                    .build();
            Counsellor marionaCounsellor = Counsellor.builder()
                    .fullName("Mariona Villaró Vicens")
                    .name("Mariona")
                    .build();
            Counsellor robertCounsellor = Counsellor.builder()
                    .fullName("Robert Creus Tella")
                    .name("Robert")
                    .build();

            counsellorService.addNewCounsellorObjectToCampaign(xinoXano.getId(), xinoXanoColonies.getId(), joelCounsellor);
            counsellorService.addNewCounsellorObjectToCampaign(xinoXano.getId(), xinoXanoJoves.getId(), mireiaCounsellor);
            counsellorService.addNewCounsellorObjectToCampaign(sio.getId(), sioColonies.getId(), marionaCounsellor);
            counsellorService.addNewCounsellorObjectToCampaign(sio.getId(), sioColonies.getId(), robertCounsellor);

            Activity activityColonies = Activity.builder()
                    .activityName("Gimkana Guarra")
                    .dayOfActivity(formatter.parse("14-07-2023"))
                    .timeOfActivity(TimeOfActivity.MORNING)
                    .activityItems(List.of("Prova 1: Bassa de fang", "Prova2: Afeitar globus"))
                    .materialNeeded(List.of("Globus", "Fang"))
                    .build();
            Activity activityJoves = Activity.builder()
                    .activityName("Slideshow")
                    .description("Presentacions sense saber el contingut")
                    .dayOfActivity(formatter.parse("17-08-2023"))
                    .timeOfActivity(TimeOfActivity.MORNING)
                    .activityItems(List.of("Presentacions"))
                    .materialNeeded(List.of("4 o 5 presentacions"))
                    .build();
            Activity sioActivity = Activity.builder()
                    .activityName("Slideshow")
                    .description("Presentacions sense saber el contingut")
                    .dayOfActivity(formatter.parse("17-08-2023"))
                    .timeOfActivity(TimeOfActivity.MORNING)
                    .activityItems(List.of("Presentacions"))
                    .materialNeeded(List.of("4 o 5 presentacions"))
                    .build();

            activitiesService.createNewActivityObjectInCampaign(xinoXano.getId(), xinoXanoColonies.getId(), activityColonies);
            activitiesService.createNewActivityObjectInCampaign(xinoXano.getId(), xinoXanoJoves.getId(), activityJoves);
            activitiesService.createNewActivityObjectInCampaign(sio.getId(), sioColonies.getId(), sioActivity);
        };
    }
}
