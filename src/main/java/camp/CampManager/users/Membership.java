package camp.CampManager.users;

import camp.CampManager.organisation.Organisation;
import lombok.*;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "membership")
public class Membership {
    //@EmbeddedId public MembershipKey id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    boolean is_claimed = false;

    Long userId;
    String fullname;

    Long organisationId;

    // "MEMBER", "ADMIN", or both
    @Column
    public boolean is_member = false;
    public boolean is_admin = false;
}
