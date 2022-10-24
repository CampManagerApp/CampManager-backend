package camp.CampManager.users;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MembershipKey implements Serializable {
    Long userId;
    Long organisationId;
}
