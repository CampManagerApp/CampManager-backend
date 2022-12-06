package camp.CampManager.organisation.campaign.tables.restrictions;

import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.tables.CampTable;
import camp.CampManager.organisation.campaign.tables.Task;

import java.util.HashSet;
import java.util.Set;

public class CounsellorTaskRestriction extends Restriction {
    public static RestrictionType restrictionType = RestrictionType.COUNSELLOR_TASK;
    public String counsellor;
    public String taskName;

    public CounsellorTaskRestriction(String counsellor, Task t) {
        this.counsellor = counsellor;
        this.taskName = t.getName();
    }

    @Override
    public Set<Set<Counsellor>> filter(CampTable campTable, String next_slot, Set<Set<Counsellor>> possible_assignments) {
        Set<Set<Counsellor>> newAssignments = new HashSet<>();
        String task = next_slot.split(":")[1];
        if (!task.equals(taskName)) {
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
