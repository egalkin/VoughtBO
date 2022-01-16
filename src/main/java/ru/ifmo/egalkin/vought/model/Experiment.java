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


    @ManyToMany
    @JoinTable(
            name = "subject_experiments",
            joinColumns = @JoinColumn(
                    name = "experiment_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "subject_id", referencedColumnName = "id"))
    private Collection<Subject> subjects;

    @ManyToMany(mappedBy = "experiments")
    private Collection<Employee> participants;

}
