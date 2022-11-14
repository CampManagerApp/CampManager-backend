package camp.CampManager.organisation.campaign;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/campaigns/")
public class CampaignEndpoint {

    @GetMapping("/{id}/campaigns/")
    public ResponseEntity<String> findCampaign(@PathVariable("id") Long id){
        System.out.println("REACHED ENDPOINT");
        return ResponseEntity.ok("Good: " + id.toString());
    }
}
