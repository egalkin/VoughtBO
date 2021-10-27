package ru.ifmo.egalkin.vought.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by egalkin
 * Date: 12.10.2021
 */
@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<Employee> employees;
}
