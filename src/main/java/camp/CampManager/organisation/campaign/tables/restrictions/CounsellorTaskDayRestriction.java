package camp.CampManager.organisation.campaign.tables.restrictions;

import camp.CampManager.organisation.campaign.tables.CampTable;
import camp.CampManager.organisation.campaign.tables.Task;

import java.util.HashSet;
import java.util.Set;

public class CounsellorTaskDayRestriction extends Restriction {
    public static RestrictionType restrictionType = RestrictionType.COUNSELLOR_TASK_DAY;
    public String counsellor;
    public String taskName;
    public String day;

    public CounsellorTaskDayRestriction(String counsellor, Task t, String day) {
        this.counsellor = counsellor;
        this.taskName = t.getName();
        this.day = day;
    }

    @Override
    public Set<Set<String>> filter(CampTable campTable, String next_slot, Set<Set<String>> possible_assignments) {
        Set<Set<String>> newAssignments = new HashSet<>();
        String day = next_slot.split(":")[0];
        String task = next_slot.split(":")[1];
        if (!day.equals(this.day) || !task.equals(taskName)) {
            return possible_assignments;
        }
        for (Set<String> assignment : possible_assignments) {
            if (!assignment.contains(counsellor)) {
                newAssignments.add(assignment);
            }
        }
        return newAssignments;
    }
}
