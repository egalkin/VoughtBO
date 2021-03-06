package ru.ifmo.egalkin.vought.model;

import lombok.*;
import org.hibernate.annotations.Where;
import ru.ifmo.egalkin.vought.model.enums.Department;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by egalkin
 * Date: 12.10.2021
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Where(clause = "active = true")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String nickname;

    private String powerDescription;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Department department;

    // У героев указывает на то связанного менеджера
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee prManager;

    @OneToMany(mappedBy="creator", fetch = FetchType.LAZY)
    private Set<Application> createdApplications;

    // Подопечные у PR мэнеджеров
    @OneToMany(mappedBy = "prManager")
    private Set<Employee> wards = new HashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Event> createdEvents;

    @OneToMany(mappedBy = "creator")
    private Set<Experiment> createdExperiments;

    @OneToMany(mappedBy = "mentor")
    private Set<Subject> mentoredSubjects;

    @OneToMany(mappedBy = "meetingAimEmployee")
    private Set<Application> meetingRequests;

    @OneToMany(mappedBy = "meetingAimEmployee")
    private Set<Event> meetingEvents;

    @OneToMany(mappedBy = "creator")
    private Set<Incident> createdIncidents;

    @ManyToMany
    @JoinTable(
            name = "employees_roles",
            joinColumns = @JoinColumn(
                    name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @ManyToMany
    @JoinTable(
            name="employee_events",
            joinColumns = @JoinColumn(
                    name = "employee_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "event_id", referencedColumnName = "id"
            )
    )
    private Collection<Event> events = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name="employee_experiments",
            joinColumns = @JoinColumn(
                    name = "employee_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "experiment_id", referencedColumnName = "id"
            )
    )
    private Collection<Experiment> experiments = new ArrayList<>();


    public String getFullName() {
        if (isHero() && nickname != null) {
            return nickname;
        }
        return firstName + " " + lastName;
    }

    public void addWard(Employee employee) {
        this.wards.add(employee);
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void addExperiment(Experiment experiment) {
        this.experiments.add(experiment);
    }

    public boolean isHero() {
        return this.department == Department.HERO;
    }

    public boolean isCeo() {
        return roles.stream().anyMatch(role -> role.getName().equals("ROLE_CEO"));
    }

    public String getAvatarFileName() {
        return this.department.toString().toLowerCase() + "-avatar.png";
    }

    @PreRemove
    public void preRemove() {
        if (this.prManager != null) {
            this.prManager.wards.remove(this);
        }

    }

}