package ru.ifmo.egalkin.vought.model;

import lombok.*;
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
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @OneToMany(mappedBy="creator")
    private Set<Application> createdApplications;

    // Подопечные у PR мэнеджеров
    @OneToMany(mappedBy = "prManager")
    private Set<Employee> wards = new HashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Event> createdEvents;

//    @OneToMany(mappedBy = "processor")
//    private Set<Event> processedEvents;

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

    public boolean isHero() {
        return this.department == Department.HERO;
    }

    public boolean isCeo() {
        return roles.stream().anyMatch(role -> role.getName().equals("ROLE_CEO"));
    }

    public String getAvatarFileName() {
        return this.department.toString().toLowerCase() + "-avatar.png";
    }
}