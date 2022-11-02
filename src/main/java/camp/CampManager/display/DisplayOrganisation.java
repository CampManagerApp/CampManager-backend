package camp.CampManager.display;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
public class DisplayOrganisation {
    private Long id;

    private String name = null;
    private String admin = null;

    private List<DisplayMembershipOrg> members;
}
