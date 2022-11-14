package camp.CampManager.organisation;

import camp.CampManager.organisation.campaign.Campaign;
import camp.CampManager.organisation.campaign.StringListConverter;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "organisation")
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organisation_gen")
    @SequenceGenerator(name = "organisation_gen", sequenceName = "organisation_seq")
    @Column(name = "org_id", nullable = false)
    private Long id;
    @Column(unique = true)
    private String name = null;

    @Column
    private String manager = null;

    @Column
    private String description = null;

    @Convert(converter = StringListConverter.class)
    private List<Long> campaign_ids;

    public Organisation(String name) {
        this.name = name;
    }

    public Organisation(String name, String manager) {
        this.name = name;
        this.manager = manager;
        this.campaign_ids = new LinkedList<>();
    }

    public Organisation(String name, String manager, String description) {
        this.name = name;
        this.manager = manager;
        this.description = description;
        this.campaign_ids = new LinkedList<>();
    }

}
