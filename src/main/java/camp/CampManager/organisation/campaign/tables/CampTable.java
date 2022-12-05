package camp.CampManager.organisation.campaign.tables;

import camp.CampManager.organisation.campaign.StringListConverter;
import camp.CampManager.organisation.campaign.activities.StringStringConverter;
import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.tables.restrictions.Restriction;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long campaignId;

    @Convert(converter = StringStringConverter.class)
    private List<String> days;

    @Transient
    public List<Restriction> restrictions;
    @Convert(converter = StringListConverter.class)
    public List<Long> restrictions_ids;
    @Convert(converter = StringListConverter.class)
    private List<Long> task_ids;
    @Transient
    private List<Task> tasks;
    @Convert(converter = MapStringConverter.class)
    private Map<String, Set<String>> grid;
    private String tableName;
    @Convert(converter = StringListConverter.class)
    private List<Long> counsellor_ids;
    @Transient
    private List<Counsellor> counsellors;

    public CampTable copy() {
        CampTable copy = new CampTable();
        copy.days = days;
        copy.tasks = tasks;
        copy.counsellors = counsellors;
        copy.restrictions = restrictions;

        copy.grid = new HashMap<>();
        for (String key : grid.keySet()) {
            copy.grid.put(key, grid.get(key));
        }
        return copy;
    }

    public boolean solve() {
        return this._solve();
    }

    private boolean _solve() {
        String next_slot = this.isCompleted();
        if (next_slot == null) {
            return true;
        }
        System.out.println(next_slot);
        Set<Set<String>> possible_assignments = this.get_possible_assignments(next_slot);
        for (Restriction restriction : this.restrictions) {
            possible_assignments = restriction.filter(this.copy(), next_slot, possible_assignments);
            if (possible_assignments.size() == 0) {
                // No possible next assignments
                return false;
            }
        }
        int i = 0;
        for (Set<String> assignment : possible_assignments) {
            System.out.print(i + "/" + possible_assignments.size() + " ");
            i++;
            CampTable copy = this.copy();
            copy.grid.put(next_slot, assignment);
            if (copy._solve()) {
                this.grid = copy.grid;
                return true;
            }
        }
        return false;
    }

    public Set<Set<String>> get_possible_assignments(String next_slot) {
        String taskName = next_slot.split(":")[1];
        for (Task task : tasks) {
            if (task.name.equals(taskName)) {
                Set<Set<String>> assignments = new HashSet<>();
                for (int i = task.maxPlaces; i >= task.minPlaces; i--) {
                    assignments.addAll(this._get_possible_combinations(i));
                }
                return assignments;
            }
        }
        return new HashSet<>();
    }

    private Set<Set<String>> _get_possible_combinations(int length) {
        if (length <= 0) {
            return new HashSet<>();
        } else if (length == 1) {
            Set<Set<String>> combinations = new HashSet<>();
            for (Counsellor counsellorObject : counsellors) {
                String counsellor = counsellorObject.getFullName();
                Set<String> combination = new HashSet<>();
                combination.add(counsellor);
                combinations.add(combination);
            }
            return combinations;
        } else {
            Set<Set<String>> combinations = new HashSet<>();
            for (Counsellor counsellorObject : counsellors) {
                String counsellor = counsellorObject.getFullName();
                Set<Set<String>> subCombinations = new HashSet<>();
                for (Set<String> subCombination : this._get_possible_combinations(length - 1)) {
                    if (subCombination.contains(counsellor)) {
                        continue;
                    }
                    Set<String> combination = new HashSet<>();
                    combination.add(counsellor);
                    combination.addAll(subCombination);
                    subCombinations.add(combination);
                }
                combinations.addAll(subCombinations);
            }
            return combinations;
        }
    }

    private String isCompleted() {
        for (String day : this.days) {
            for (Task task : this.tasks) {
                if (this.grid.get(day + ":" + task.name) == null) {
                    return day + ":" + task.name;
                }
            }
        }
        return null;
    }
}
