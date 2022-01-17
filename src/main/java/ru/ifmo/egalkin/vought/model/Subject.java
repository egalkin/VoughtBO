package ru.ifmo.egalkin.vought.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Employee mentor;

    @ManyToMany
    @JoinTable(
            name="subject_experiments",
            joinColumns = @JoinColumn(
                    name = "subject_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "experiment_id", referencedColumnName = "id"
            )
    )
    private Collection<Experiment> experiments = new ArrayList<>();

    public String getFullName() {
        return String.format("Объект №%d: %s", id, nickname);
    }

    public void addExperiment(Experiment experiment) {
        this.experiments.add(experiment);
    }

}
