package camp.CampManager.organisation.campaign.tables.restrictions;

import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.counsellors.Group;
import camp.CampManager.organisation.campaign.tables.CampTable;
import camp.CampManager.users.Gender;
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
@DiscriminatorValue("5")
public class SpecialRestriction extends Restriction {
    public static RestrictionType restrictionType = RestrictionType.SPECIAL;

    String taskName;

    String attributeName;
    String desiredAttributeValue;

    public SpecialRestriction(String tName, String attributeName, String attributeDesiredValue) {
        this.taskName = tName;
        this.attributeName = attributeName;
        this.desiredAttributeValue = attributeDesiredValue;
    }

    @Override
    public Set<Set<Counsellor>> filter(CampTable campTable, String next_slot, Set<Set<Counsellor>> possible_assignments) {
        Set<Set<Counsellor>> newAssignments = new HashSet<>();
        for (Set<Counsellor> assignment : possible_assignments) {
            boolean add = true;
            for (Counsellor counsellor : assignment) {
                switch (attributeName) {
                    case "gender" -> {
                        if (!counsellor.getGender().equals(Gender.valueOf(desiredAttributeValue))) {
                            add = false;
                        }
                    }
                    case "is_first_year" -> {
                        if (!counsellor.is_first_year() == Boolean.parseBoolean(desiredAttributeValue)) {
                            add = false;
                        }
                    }
                    case "big_group" -> {
                        if (!counsellor.getBigGroup().equals(Group.valueOf(desiredAttributeValue))) {
                            add = false;
                        }
                    }
                }
                if (!add) {
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
