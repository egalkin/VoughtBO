package ru.ifmo.egalkin.vought.model.rrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.egalkin.vought.model.Event;
import ru.ifmo.egalkin.vought.model.Incident;
import ru.ifmo.egalkin.vought.model.Subject;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
