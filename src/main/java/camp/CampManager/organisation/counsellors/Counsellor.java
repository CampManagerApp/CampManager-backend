package camp.CampManager.organisation.counsellors;

import camp.CampManager.users.Gender;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

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

    private int emergencyPhone;

    private String foodAffection;
    private String nonFoodAffection;
    private String medicalObservations;
    private String specialMedication;
    private String medicationGuide;
    private String additionalInformation;
}
