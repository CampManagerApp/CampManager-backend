package camp.CampManager.organisation.campaign.activities;

import camp.CampManager.organisation.campaign.StringListConverter;
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
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long campaignId;

    private String activityName;
    private Date dayOfActivity;
    private TimeOfActivity timeOfActivity;

    private String description;

    @Convert(converter = StringStringConverter.class)
    private List<String> materialNeeded;
    @Convert(converter = StringStringConverter.class)
    private List<String> activityItems;
}
