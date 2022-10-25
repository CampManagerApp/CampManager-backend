package camp.CampManager.display;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@Builder
public class DisplayMembershipOrg{
    String userName;

    private Long id;

    // "MEMBER", "ADMIN", or both
    public boolean is_member = false;
    public boolean is_admin = false;
}
