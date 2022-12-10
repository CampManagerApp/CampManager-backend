package camp.CampManager.organisation.campaign.tables.restrictions;

import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.tables.CampTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("7")
public class NoFirstYearOnlyRestriction extends Restriction {
    @Override
    public Set<Set<Counsellor>> filter(CampTable campTable, String next_slot, Set<Set<Counsellor>> possible_assignments) {
        Set<Set<Counsellor>> newAssignments = new HashSet<>();
        for (Set<Counsellor> assignment : possible_assignments) {
            boolean add = false;
            for (Counsellor counsellor : assignment) {
                if (!counsellor.is_first_year()) {
                    add = true;
                    break;
                }
            }
            if (add) {
                newAssignments.add(assignment);
            }
        }
        return newAssignments;
    }
}
