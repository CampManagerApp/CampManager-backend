package camp.CampManager.organisation.campaign.counsellors;

import camp.CampManager.users.Gender;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Counsellor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String surnames;
    private String fullName;
    private Gender gender;
    private Date birthday;
    private Group bigGroup;

    private boolean is_first_year;

    private int emergencyPhone;

    private String foodAffection;
    private String nonFoodAffection;
    private String medicalObservations;
    private String specialMedication;
    private String medicationGuide;
    private String additionalInformation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Counsellor that = (Counsellor) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
