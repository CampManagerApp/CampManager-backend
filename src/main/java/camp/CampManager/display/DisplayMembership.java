package camp.CampManager.display;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DisplayMembership {
    String userName;
    String organisationName;

    private Long id;

    // "MEMBER", "ADMIN", or both
    public boolean is_member;
    public boolean is_admin;
}