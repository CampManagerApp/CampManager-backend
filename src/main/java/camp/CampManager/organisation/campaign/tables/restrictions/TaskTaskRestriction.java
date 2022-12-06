package camp.CampManager.organisation.campaign.tables.restrictions;

import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.tables.CampTable;
import camp.CampManager.organisation.campaign.tables.Task;

import java.util.HashSet;
import java.util.Set;

public class TaskTaskRestriction extends Restriction {
    public static RestrictionType restrictionType = RestrictionType.TASK_TASK;
    public Task t1;
    public Task t2;

    public TaskTaskRestriction(Task t1, Task t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public Set<Set<Counsellor>> filter(CampTable campTable, String next_slot, Set<Set<Counsellor>> possible_assignments) {
        Set<Set<Counsellor>> newAssignments = new HashSet<>();
        String day = next_slot.split(":")[0];
        String task = next_slot.split(":")[1];
        if (!task.equals(t1.name) && !task.equals(t2.name)) {
            return possible_assignments;
        }
        if (task.equals(t1.name)) {
            Set<String> task2assigned = campTable.getGrid().get(day + ":" + t2.name);
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
            Set<String> task2assigned = campTable.getGrid().get(day + ":" + t1.name);
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
