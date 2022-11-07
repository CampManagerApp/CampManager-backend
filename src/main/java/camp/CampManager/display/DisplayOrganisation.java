package camp.CampManager.display;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class DisplayOrganisation {
    private Long id;

    private String name;
    private String admin;

    private List<DisplayMembershipOrg> members;
}
