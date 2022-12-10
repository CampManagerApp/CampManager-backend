package camp.CampManager.organisation.campaign;

import camp.CampManager.display.DisplayCampaign;
import camp.CampManager.display.DisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/organisation/")
public class CampaignEndpoint {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    @Autowired
    private CampaignService campaignService;
    @Autowired
    private DisplayService displayService;

    @GetMapping("/{id}/campaign")
    @ResponseBody
    public ResponseEntity<DisplayCampaign> findInformationOfCampaignOfOrganisation(@PathVariable("id") Long id,
                                                                                   @RequestParam("campaign_name") String campaign_name) {
        var campaign_opt = campaignService.findCampaignByNameAndOrganisationId(campaign_name, id);
        if (campaign_opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(displayService.campaignToDisplay(campaign_opt.get()));
        }
    }

    @PostMapping("/{id}/campaign")
    @ResponseBody
    public ResponseEntity<Campaign> createCampaignOfOrganisation(@PathVariable("id") Long id,
                                                                 @RequestBody Map<String, String> input,
                                                                 HttpServletResponse response) throws URISyntaxException {
        var campaign_builder = Campaign.builder();
        campaign_builder.organisationId(id);
        if (input.containsKey("campaign_name")) {
            campaign_builder.campaignName(input.get("campaign_name"));
        } else {
            response.setHeader("error", "Campaign Name Missing. Key: 'campaign_name'");
            return ResponseEntity.badRequest().build();
        }
        if (input.containsKey("start")) {
            try {
                campaign_builder.startDate(formatter.parse(input.get("start")));
            } catch (ParseException e) {
                response.setHeader("error", "Wrong format start date. Correct format is dd-MM-yyyy hh:mm:ss");
                return ResponseEntity.badRequest().build();
            }
        } else {
            response.setHeader("error", "Start Date Missing. Key: 'start'");
            return ResponseEntity.badRequest().build();
        }
        if (input.containsKey("end")) {
            try {
                campaign_builder.endDate(formatter.parse(input.get("end")));
            } catch (ParseException e) {
                response.setHeader("error", "Wrong format end date. Correct format is dd-MM-yyyy hh:mm:ss");
                return ResponseEntity.badRequest().build();
            }
        } else {
            response.setHeader("error", "End Date Missing. Key: 'end'");
            return ResponseEntity.badRequest().build();
        }
        Campaign campaign = campaign_builder.build();
        if (campaignService.saveCampaign(campaign)) {
            return ResponseEntity.created(new URI("/organisation/" + id + "/campaign/")).body(campaign);
        } else {
            response.setHeader("error", "Campaign Name Duplicated");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/campaign")
    @ResponseBody
    public ResponseEntity<String> modifyCampaignOfOrganisation(@PathVariable("id") Long id, @RequestBody Map<String, String> input) {
        Campaign campaign;
        if (input.containsKey("campaign_name")) {
            var campaign_opt = campaignService.findCampaignByNameAndOrganisationId(input.get("campaign_name"), id);
            if (campaign_opt.isEmpty()) {
                return ResponseEntity.badRequest().body("Campaign does not exist");
            }
            campaign = campaign_opt.get();
        } else {
            return ResponseEntity.badRequest().body("Campaign Name Missing. Key: 'campaign_name'");
        }
        if (input.containsKey("new_campaign_name")) {
            campaign.setCampaignName(input.get("campaign_name"));
        }
        if (input.containsKey("start")) {
            try {
                campaign.setStartDate(formatter.parse(input.get("start")));
            } catch (ParseException e) {
                return ResponseEntity.badRequest().body("Wrong format start date. Correct format is dd-MM-yyyy hh:mm:ss");
            }
        }
        if (input.containsKey("end")) {
            try {
                campaign.setEndDate(formatter.parse(input.get("end")));
            } catch (ParseException e) {
                return ResponseEntity.badRequest().body("Wrong format end date. Correct format is dd-MM-yyyy hh:mm:ss");
            }
        }
        if (campaignService.saveModifiedCampaign(campaign)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Campaign Name Duplicated");
        }
    }

    @DeleteMapping("/{id}/campaign")
    @ResponseBody
    public ResponseEntity<String> deleteCampaignOfOrganisation(@PathVariable("id") Long id, @RequestBody Map<String, String> input) {
        if (input.containsKey("campaign_name")) {
            var campaign_opt = campaignService.findCampaignByNameAndOrganisationId(input.get("campaign_name"), id);
            if (campaign_opt.isEmpty()) {
                return ResponseEntity.badRequest().body("Campaign does not exist");
            }
            campaignService.deleteCampaign(campaign_opt.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Campaign Name Missing. Key: 'campaign_name'");
        }
    }

    @GetMapping("/{id}/campaign/all")
    @ResponseBody
    public ResponseEntity<List<DisplayCampaign>> getAllCampaignOfOrganisation(@PathVariable("id") Long id) {
        List<DisplayCampaign> displays = new LinkedList<>();
        campaignService.findCampaignsByOrganisationId(id).forEach(e -> displays.add(displayService.campaignToDisplay(e)));
        return ResponseEntity.ok(displays);
    }
}
