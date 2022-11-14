package camp.CampManager.organisation.campaign;

import camp.CampManager.organisation.Organisation;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id", nullable = false)
    private Long id;

    private Long organisation_id;

    private String campaign_name;
    private Date start_date;
    private Date end_date;

    /*
    private List<Participant> participants;
    private List<Counsellor> counsellors;
     */
}
