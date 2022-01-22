package ru.ifmo.egalkin.vought.model.rrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.egalkin.vought.model.Experiment;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
}
