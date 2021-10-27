package ru.ifmo.egalkin.vought.model.rrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.enums.ApplicationStatus;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;

import java.util.List;

/**
 * Created by egalkin
 * Date: 13.10.2021
 */
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByCreatorAndApplicationStatus(Employee creator, ApplicationStatus status);

    List<Application> findAllByApplicationStatus(ApplicationStatus status);

    List<Application> findAllByCreator(Employee creator);

    List<Application> findByMeetingAimEmployeeIsNullOrMeetingAimEmployee(Employee employee);

}
