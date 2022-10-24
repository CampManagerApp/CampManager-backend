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
    @Column
    private String name = null;

    @Column
    private String admin = null;

    public Organisation(String name) {
        this.name = name;
    }

    public Organisation(String name, String admin) {
        this.name = name;
        this.admin = admin;
    }
}
