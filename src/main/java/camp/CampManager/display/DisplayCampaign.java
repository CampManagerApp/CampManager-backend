package camp.CampManager.display;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class DisplayCampaign {
    private Long id;

    private Long organisationId;

    private String campaignName;
    private Date startDate;
    private Date endDate;
}
