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
@DiscriminatorValue("2")
public class CounsellorTaskDayRestriction extends Restriction {
    public static RestrictionType restrictionType = RestrictionType.COUNSELLOR_TASK_DAY;
    public String counsellor;
    public String taskName;
    public String day;

    public CounsellorTaskDayRestriction(String counsellor, String t, String day) {
        this.counsellor = counsellor;
        this.taskName = t;
        this.day = day;
    }

    @Override
    public Set<Set<Counsellor>> filter(CampTable campTable, String next_slot, Set<Set<Counsellor>> possible_assignments) {
        Set<Set<Counsellor>> newAssignments = new HashSet<>();
        String day = next_slot.split(":")[0];
        String task = next_slot.split(":")[1];
        if (!day.equals(this.day) || !task.equals(taskName)) {
            return possible_assignments;
        }
        for (Set<Counsellor> assignment : possible_assignments) {
            Set<String> assignmentOfStrings = new HashSet<>();
            assignment.forEach(e -> assignmentOfStrings.add(e.getFullName()));
            if (!assignmentOfStrings.contains(counsellor)) {
                newAssignments.add(assignment);
            }
        }
        return newAssignments;
    }
}
