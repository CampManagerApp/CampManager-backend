package camp.CampManager.organisation;

import camp.CampManager.users.Membership;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "organisation")
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organisation_gen")
    @SequenceGenerator(name = "organisation_gen", sequenceName = "organisation_seq")
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(unique = true)
    private String name = null;

    @Column
    private String manager = null;

    @Column
    private String description = null;

    public Organisation(String name) {
        this.name = name;
    }

    public Organisation(String name, String manager) {
        this.name = name;
        this.manager = manager;
    }

    public Organisation(String name, String manager, String description) {
        this.name = name;
        this.manager = manager;
        this.description = description;
    }

}
