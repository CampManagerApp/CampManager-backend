package camp.CampManager.organisation.campaign.tables.restrictions;

import camp.CampManager.organisation.campaign.tables.CampTable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public abstract class Restriction {
    public RestrictionType restrictionType = RestrictionType.NONE;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Given a list of possible assignments, return the list after reorder or eliminating the ones that don't fit
     *
     * @param possible_assignments List of possible assignments
     */
    public abstract Set<Set<String>> filter(CampTable campTable, String next_slot, Set<Set<String>> possible_assignments);
}
