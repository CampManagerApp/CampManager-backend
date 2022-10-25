package camp.CampManager.display;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.users.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
public class DisplayUser {

    private Long id;

    private String username;
    private String password;
    private String email;
    private Date date_of_birth;
    private String full_name;
    private Gender gender;

    List<DisplayMembershipUser> organisations;
}
