package ru.ifmo.egalkin.vought.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Experiment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;

    private String description;

    private String goal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private Employee creator;

    @ManyToMany(mappedBy = "experiments", fetch = FetchType.EAGER)
    private Collection<Subject> subjects;

    @ManyToMany(mappedBy = "experiments")
    private Collection<Employee> members;

    public boolean notEmptyGoal() {
        return goal != null && !goal.isEmpty();
    }

    public boolean notEmptyDescription() {
        return description != null && !description.isEmpty();
    }

}
