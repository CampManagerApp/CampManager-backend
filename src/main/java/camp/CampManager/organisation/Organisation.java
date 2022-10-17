package camp.CampManager.organisation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
