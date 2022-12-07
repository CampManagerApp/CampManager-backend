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
@DiscriminatorValue("6")
public class TaskTaskRestriction extends Restriction {
    public static RestrictionType restrictionType = RestrictionType.TASK_TASK;
    public String t1name;
    public String t2name;

    public TaskTaskRestriction(String t1name, String t2name) {
        this.t1name = t1name;
        this.t2name = t2name;
    }

    @Override
    public Set<Set<Counsellor>> filter(CampTable campTable, String next_slot, Set<Set<Counsellor>> possible_assignments) {
        Set<Set<Counsellor>> newAssignments = new HashSet<>();
        String day = next_slot.split(":")[0];
        String task = next_slot.split(":")[1];
        if (!task.equals(t1name) && !task.equals(t2name)) {
            return possible_assignments;
        }
        if (task.equals(t1name)) {
            Set<String> task2assigned = campTable.getGrid().get(day + ":" + t2name);
            if (task2assigned == null) {
                return possible_assignments;
            } else {
                for (Set<Counsellor> assignment : possible_assignments) {
                    boolean add = true;
                    Set<String> assignmentOfStrings = new HashSet<>();
                    assignment.forEach(e -> assignmentOfStrings.add(e.getFullName()));
                    for (String assigned : assignmentOfStrings) {
                        if (task2assigned.contains(assigned)) {
                            add = false;
                            break;
                        }
                    }
                    if (add) {
                        newAssignments.add(assignment);
                    }
                }
            }
        } else {
            Set<String> task2assigned = campTable.getGrid().get(day + ":" + t1name);
            if (task2assigned == null) {
                return possible_assignments;
            } else {
                for (Set<Counsellor> assignment : possible_assignments) {
                    boolean add = true;
                    Set<String> assignmentOfStrings = new HashSet<>();
                    assignment.forEach(e -> assignmentOfStrings.add(e.getFullName()));
                    for (String assigned : assignmentOfStrings) {
                        if (task2assigned.contains(assigned)) {
                            add = false;
                            break;
                        }
                    }
                    if (add) {
                        newAssignments.add(assignment);
                    }
                }
            }
        }
        return newAssignments;
    }
}
