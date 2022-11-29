package camp.CampManager.organisation.campaign.participants;

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
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String fullName;
    private String name;
    private String surnames;
    private Gender gender;
    private Date birthday;
    private String school_year;
    private String parentOneFullName;
    private String parentTwoFullName;
    private String contactEmailOne;
    private String contactEmailTwo;
    private int phoneNumberOne;
    private int phoneNumberTwo;
    private int phoneNumberFix;
    private String bankIBAN;
    private String houseAddress;
    private int postalCode;
    private String townName;
    private String county;
    private String healthCardCIP;
    private String insuranceName;
    private String foodAffection;
    private String nonFoodAffection;
    private String medicalObservations;
    private boolean ibuprofen;
    private boolean paracetamol;
    private String specialMedication;
    private String medicationGuide;
    private String additionalInformation;
}
