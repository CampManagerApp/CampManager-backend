package camp.CampManager.users;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MembershipKey implements Serializable {

    @Column
    Long userId;

    @Column
    Long orgId;
}
