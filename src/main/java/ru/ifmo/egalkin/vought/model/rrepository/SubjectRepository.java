package ru.ifmo.egalkin.vought.model.rrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.egalkin.vought.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
