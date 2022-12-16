package camp.CampManager.organisation.campaign.tables.restrictions;

import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.tables.CampTable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "restriction_d_type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Restriction {
    public RestrictionType restrictionType = RestrictionType.NONE;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    /**
     * Given a list of possible assignments, return the list after reorder or eliminating the ones that don't fit
     *
     * @param possible_assignments List of possible assignments
     */
    public abstract Set<Set<Counsellor>> filter(CampTable campTable, String next_slot, Set<Set<Counsellor>> possible_assignments);
}
