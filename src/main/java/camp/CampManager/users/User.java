package camp.CampManager.users;

import camp.CampManager.organisation.Organisation;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
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

    @ManyToMany()
    @JoinTable(
        name = "users_organisations",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "organisation_id")
    )
    Set<Organisation> organisations;
}
