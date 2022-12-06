package camp.CampManager.organisation.campaign.tables.restrictions;

import camp.CampManager.organisation.campaign.tables.CampTable;
import camp.CampManager.organisation.campaign.tables.Task;

import java.util.Set;

public class SpecialRestriction extends Restriction {
    public static RestrictionType restrictionType = RestrictionType.SPECIAL;

    Task t;

    String attributeName;
    String desiredAttributeValue;

    @Override
    public Set<Set<String>> filter(CampTable campTable, String next_slot, Set<Set<String>> possible_assignments) {
        // TODO
        return null;
    }
}
