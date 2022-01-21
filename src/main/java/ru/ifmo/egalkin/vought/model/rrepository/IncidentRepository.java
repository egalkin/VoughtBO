package ru.ifmo.egalkin.vought.model.rrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ifmo.egalkin.vought.model.Incident;

import java.util.List;

/**
 * Created by egalkin
 * Date: 13.10.2021
 */
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findAllByActive(Boolean active);

    Incident findFirstByActive(boolean active);

    Integer countAllByActive(Boolean active);

}
