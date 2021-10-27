package ru.ifmo.egalkin.vought.model.rrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.egalkin.vought.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
