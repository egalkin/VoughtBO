package ru.ifmo.egalkin.vought.model.rrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.egalkin.vought.model.Experiment;

public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
}
