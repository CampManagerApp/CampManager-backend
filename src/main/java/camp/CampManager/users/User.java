package camp.CampManager.users;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String username;
    private String password;
    private String email;
    private Date date_of_birth;
    private String full_name;
    private Gender gender;

    @OneToMany(mappedBy = "user", targetEntity = Membership.class, fetch = FetchType.EAGER)
    Set<Membership> organisations;
}
