package camp.CampManager.users;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class MembershipKey implements Serializable {

    public MembershipKey(Long userId, Long orgId){
        super();
        this.userId = userId;
        this.orgId = orgId;
    }

    @Column
    Long userId;

    @Column
    Long orgId;
}
