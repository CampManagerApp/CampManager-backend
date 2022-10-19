package camp.CampManager.organisation;

import camp.CampManager.users.User;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    private Collection<User> members;

    public Organisation(String name) {
        this.name = name;
    }

    public Organisation(String name, String admin) {
        this.name = name;
        this.admin = admin;
    }
}
