package camp.CampManager.organisation.campaign.tables.restrictions;

import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.tables.CampTable;
import camp.CampManager.organisation.campaign.tables.Task;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@DiscriminatorValue("4")
public class SortByFavouriteRestriction extends Restriction {
    public static RestrictionType restrictionType = RestrictionType.SORT_FAVOURITE;

    /*
     *  Sort by both size and equity
     *  If it's bigger it will go first
     *  If it's smaller it will go last
     *  If they are the same size, the one that has the LEAST total deviation from the average goes first
     */
    @Override
    public Set<Set<Counsellor>> filter(CampTable campTable, String next_slot, Set<Set<Counsellor>> possible_assignments) {
        return possible_assignments.stream().sorted(new Comparator<Set<Counsellor>>() {
            @Override
            public int compare(Set<Counsellor> o1, Set<Counsellor> o2) {
                if (o1.size() != o2.size()) {
                    // if o1.size() > o2.size() it should go first, so it needs to return positive
                    return -Integer.compare(o1.size(), o2.size());
                } else {
                    return Float.compare(average(campTable.copy(), next_slot, o1), average(campTable.copy(), next_slot, o2));
                }
            }
        }).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    // The smaller, the better
    public float deviation(CampTable copy, String next_slot, Set<Counsellor> assignment) {
        Set<String> assignmentOfStrings = new HashSet<>();
        assignment.forEach(e -> assignmentOfStrings.add(e.getFullName()));
        copy.getGrid().put(next_slot, assignmentOfStrings);

        int repetition = 0;
        float discount = 1.0F;
        for (Counsellor counsellorObject : copy.getCounsellors()) {
            String counsellor = counsellorObject.getFullName();
            int total = 0;
            for (String day : copy.getDays()) {
                for (Task task : copy.getTasks()) {
                    Set<String> values = copy.getGrid().get(day + ":" + task.name);
                    if (values != null) {
                        for (String value : values) {
                            if (value.equals(counsellor)) {
                                total += discount;
                                discount = 1.0F;
                            }
                        }
                    }
                }
                discount -= 0.05;
            }
            repetition += total * total;
        }
        return repetition;
    }

    // The smaller, the better
    public float repetition(CampTable copy, String next_slot, Set<Counsellor> assignment) {
        Set<String> assignmentOfStrings = new HashSet<>();
        assignment.forEach(e -> assignmentOfStrings.add(e.getFullName()));
        copy.getGrid().put(next_slot, assignmentOfStrings);

        String task = next_slot.split(":")[1];

        int repetition = 0;
        for (Counsellor counsellorObject : copy.getCounsellors()) {
            String counsellor = counsellorObject.getFullName();
            int total = 0;
            for (String day : copy.getDays()) {
                Set<String> values = copy.getGrid().get(day + ":" + task);
                if (values != null) {
                    for (String value : values) {
                        if (value.equals(counsellor)) {
                            total += 1;
                        }
                    }
                }
            }
            repetition += total * total;
        }
        return repetition;
    }

    // The smaller, the better
    public float singularity(CampTable copy, String next_slot, Set<Counsellor> assignment) {
        Set<String> assignmentOfStrings = new HashSet<>();
        assignment.forEach(e -> assignmentOfStrings.add(e.getFullName()));
        copy.getGrid().put(next_slot, assignmentOfStrings);

        String day = next_slot.split(":")[0];

        int singularity = 0;
        for (Counsellor counsellorObject : copy.getCounsellors()) {
            String counsellor = counsellorObject.getFullName();
            int total = 0;
            for (Task task : copy.getTasks()) {
                Set<String> values = copy.getGrid().get(day + ":" + task.name);
                if (values != null) {
                    for (String value : values) {
                        if (value.equals(counsellor)) {
                            total += 1;
                        }
                    }
                }
            }
            singularity += total * total;
        }
        return singularity;
    }

    public float average(CampTable copy, String next_slot, Set<Counsellor> assignment) {
        return (float) (0.33 * deviation(copy, next_slot, assignment) +
                (0.33 * repetition(copy, next_slot, assignment)) +
                (0.33 * singularity(copy, next_slot, assignment)));
    }
}
