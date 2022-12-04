package camp.CampManager.organisation.campaign.tables;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    public String name;
    public int minPlaces;
    public int maxPlaces;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    public Task(String name, int minPlaces, int maxPlaces) {
        this.name = name;
        this.minPlaces = minPlaces;
        this.maxPlaces = maxPlaces;
    }
}

