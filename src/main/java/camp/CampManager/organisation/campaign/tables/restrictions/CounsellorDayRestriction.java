package camp.CampManager.organisation.campaign.tables.restrictions;

import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.tables.CampTable;
import camp.CampManager.organisation.campaign.tables.Task;

import java.util.HashSet;
import java.util.Set;

public class CounsellorDayRestriction extends Restriction {
    public static RestrictionType restrictionType = RestrictionType.COUNSELLOR_DAY;
    String counsellor;
    String day;

    @Override
    public Set<Set<Counsellor>> filter(CampTable campTable, String next_slot, Set<Set<Counsellor>> possible_assignments) {
        Set<Set<Counsellor>> newAssignments = new HashSet<>();
        String day = next_slot.split(":")[0];
        String task = next_slot.split(":")[1];
        if (!day.equals(this.day)) {
            return possible_assignments;
        }
        for (Set<Counsellor> assignment : possible_assignments) {
            CampTable copy = campTable.copy();
            Set<String> assignmentOfStrings = new HashSet<>();
            assignment.forEach(e -> assignmentOfStrings.add(e.getFullName()));
            copy.getGrid().put(next_slot, assignmentOfStrings);
            boolean add = true;
            for (Task t1 : copy.getTasks()) {
                Set<String> taskAssigned = copy.getGrid().get(day + ":" + t1.name);
                if (taskAssigned.contains(counsellor)) {
                    add = false;
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
