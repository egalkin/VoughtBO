package ru.ifmo.egalkin.vought.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.egalkin.vought.model.enums.IncidentType;

import javax.persistence.*;

/**
 * Created by egalkin
 * Date: 13.10.2021
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentType incidentType;

    @Column(nullable = false)
    private Integer enemiesNumber;

    @Column(nullable = false)
    private Integer armamentLevel;

    private String info;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private Employee creator;
}
