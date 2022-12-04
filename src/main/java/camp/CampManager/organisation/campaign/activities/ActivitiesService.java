package camp.CampManager.organisation.campaign.activities;

import camp.CampManager.organisation.OrganisationRepository;
import camp.CampManager.organisation.campaign.Campaign;
import camp.CampManager.organisation.campaign.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ActivitiesService {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    private ActivitiesRepository activitiesRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private OrganisationRepository organisationRepository;

    public ResponseEntity<List<Activity>> findAllOfOrganisation(Long orgId) {
        var org_o = organisationRepository.findById(orgId);
        if (org_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var org = org_o.get();
        var activities = new LinkedList<Activity>();
        for (Campaign campaign : campaignRepository.findByOrganisationIdEquals(org.getId())) {
            activitiesRepository.findByCampaignIdEquals(campaign.getId()).forEach(activities::add);
        }
        return ResponseEntity.ok(activities);
    }

    public ResponseEntity<List<Activity>> findAllOfCampaign(Long orgId, Long campId) {
        var org_o = organisationRepository.findById(orgId);
        var camp_o = campaignRepository.findById(campId);
        if (org_o.isEmpty() || camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var camp = camp_o.get();
        var activities = new LinkedList<Activity>();
        activitiesRepository.findByCampaignIdEquals(camp.getId()).forEach(activities::add);
        return ResponseEntity.ok(activities);
    }

    public ResponseEntity<String> createNewActivityInCampaign(Long orgId, Long campId, Map<String, String> input) throws URISyntaxException {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var activity_builder = Activity.builder();
        if (input.containsKey("name")) {
            if (activitiesRepository.existsByActivityName(input.get("name"))) {
                return ResponseEntity.badRequest().body("Activity with same name already exists");
            }
            activity_builder.activityName(input.get("name"));
        } else {
            return ResponseEntity.badRequest().body("Name of activity missing");
        }
        activity_builder.campaignId(camp_o.get().getId());
        if (input.containsKey("start")) {
            try {
                activity_builder.dayOfActivity(formatter.parse(input.get("start")));
            } catch (ParseException e) {
                return ResponseEntity.badRequest().body("Wrong format start date. Correct format is dd-MM-yyyy");
            }
        } else {
            return ResponseEntity.badRequest().body("Day of activity missing");
        }
        if (input.containsKey("time_of_activity")) {
            try {
                activity_builder.timeOfActivity(TimeOfActivity.valueOf(input.get("time_of_activity")));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Bad time_of_activity. Valid values are MORNING, AFTERNOON, NIGHT, WHOLE_DAY");
            }
        } else {
            return ResponseEntity.badRequest().body("Time of activity meeting");
        }
        if (input.containsKey("description")) {
            activity_builder.description(input.get("description"));
        }
        if (input.containsKey("material")) {
            activity_builder.materialNeeded(new LinkedList<>(
                    List.of(input.get("material").split(";")))
            );
        }
        if (input.containsKey("items")) {
            activity_builder.activityItems(new LinkedList<>(
                    List.of(input.get("items").split(";")))
            );
        }
        var activity = activity_builder.build();
        activitiesRepository.save(activity);
        return ResponseEntity.created(new URI("/organisation/id/campaign/id/activities")).build();
    }

    public void createNewActivityObjectInCampaign(Long orgId, Long campId, Activity activity) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return;
        }
        activity.setCampaignId(camp_o.get().getId());
        activitiesRepository.save(activity);
    }

    public ResponseEntity<String> modifyActivityInCampaign(Long orgId, Long campId, Map<String, String> input) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (input.containsKey("name")) {
            if (!activitiesRepository.existsByActivityName(input.get("name"))) {
                return ResponseEntity.badRequest().body("Activity does not exist");
            }
        } else {
            return ResponseEntity.badRequest().body("Name of activity missing");
        }
        var activity_o = activitiesRepository.findByCampaignIdEqualsAndActivityNameEquals(campId, input.get("name"));
        if (activity_o.isEmpty()) {
            return ResponseEntity.badRequest().body("Activity not found");
        }
        var activity = activity_o.get();
        if (input.containsKey("start")) {
            try {
                activity.setDayOfActivity(formatter.parse(input.get("start")));
            } catch (ParseException e) {
                return ResponseEntity.badRequest().body("Wrong format start date. Correct format is dd-MM-yyyy");
            }
        }
        if (input.containsKey("time_of_activity")) {
            try {
                activity.setTimeOfActivity(TimeOfActivity.valueOf(input.get("time_of_activity")));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Bad time_of_activity. Valid values are MORNING, AFTERNOON, NIGHT, WHOLE_DAY");
            }
        }
        if (input.containsKey("description")) {
            activity.setDescription(input.get("description"));
        }
        if (input.containsKey("material")) {
            activity.setMaterialNeeded(new LinkedList<>(
                    List.of(input.get("material").split(";")))
            );
        }
        if (input.containsKey("items")) {
            activity.setActivityItems(new LinkedList<>(
                    List.of(input.get("items").split(";")))
            );
        }
        activitiesRepository.save(activity);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> deleteActivityInACampaign(Long orgId, Long campId, Map<String, String> input) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (input.containsKey("name")) {
            if (!activitiesRepository.existsByActivityName(input.get("name"))) {
                return ResponseEntity.badRequest().body("Activity does not exist");
            }
        } else {
            return ResponseEntity.badRequest().body("Name of activity missing");
        }
        var activity_o = activitiesRepository.findByCampaignIdEqualsAndActivityNameEquals(campId, input.get("name"));
        if (activity_o.isEmpty()) {
            return ResponseEntity.badRequest().body("Activity not found");
        }
        var activity = activity_o.get();
        activitiesRepository.deleteById(activity.getId());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Activity> findActivityOfCampaign(Long orgId, Long campId, Map<String, String> input) {
        var org_o = organisationRepository.findById(orgId);
        var camp_o = campaignRepository.findById(campId);
        if (org_o.isEmpty() || camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var camp = camp_o.get();
        if (!input.containsKey("name")) {
            return ResponseEntity.badRequest().build();
        }
        var activity = activitiesRepository.findByCampaignIdEqualsAndActivityNameEquals(
                camp.getId(), input.get("name"));
        return activity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
