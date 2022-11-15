package camp.CampManager.organisation.campaign;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.campaign.participants.Participant;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id", nullable = false)
    private Long id;

    private Long organisationId;

    private String campaignName;
    private Date startDate;
    private Date endDate;

    @Convert(converter = StringListConverter.class)
    private List<Long> participant_ids;
    /*
    private List<Counsellor> counsellors;
     */
}
