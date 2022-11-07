package camp.CampManager.display;

import camp.CampManager.users.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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
