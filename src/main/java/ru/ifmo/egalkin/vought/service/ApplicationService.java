package ru.ifmo.egalkin.vought.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.egalkin.vought.controller.request.HeroApplicationCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.ScientistApplicationRequest;
import ru.ifmo.egalkin.vought.controller.request.PrApplicationCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.ApplicationRejectionRequest;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.enums.ApplicationStatus;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;
import ru.ifmo.egalkin.vought.model.rrepository.ApplicationRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EventService eventService;

    @Transactional
    public void createPrRequest(String prManagerEmail, PrApplicationCreationRequest request) {
        Employee prManager = employeeService.findByEmail(prManagerEmail);
        Application application = Application.builder()
                .name(request.getName())
                .description(request.getDescription())
                .updateDate(LocalDate.now())
                .applicationType(ApplicationType.PR_STRATEGY)
                .applicationStatus(ApplicationStatus.PENDING)
                .creator(prManager)
                .build();
        applicationRepository.save(application);

    }

    @Transactional
    public void createHeroRequest(String heroEmail, HeroApplicationCreationRequest request) {
        Employee hero = employeeService.findByEmail(heroEmail);
        Employee meetingAimEmployee = employeeService.findById(request.getMeetingAimEmployeeId());
        Application application = Application.builder()
                .name(request.getName())
                .description(request.getDescription())
                .updateDate(LocalDate.now())
                .meetingTime(request.getEventDateTimeDto().toLocalDateTime())
                .applicationType(ApplicationType.MEETING)
                .applicationStatus(ApplicationStatus.PENDING)
                .meetingAimEmployee(meetingAimEmployee)
                .creator(hero)
                .build();
        applicationRepository.save(application);
    }

    @Transactional
    public void createScientistRequest(String scientistEmail, ScientistApplicationRequest request) {
        Employee scientist = employeeService.findByEmail(scientistEmail);
        Application application = Application.builder()
                .name(request.getName())
                .description(request.getDescription())
                .updateDate(LocalDate.now())
                .applicationType(request.getApplicationType())
                .applicationStatus(ApplicationStatus.PENDING)
                .creator(scientist)
                .build();
        applicationRepository.save(application);
    }


    public List<Application> getEmployeeActiveApplications(String employeeEmail) {
        Employee employee = employeeService.findByEmail(employeeEmail);
        return applicationRepository.findAllByCreatorAndApplicationStatus(employee, ApplicationStatus.PENDING);
    }

    public List<Application> getEmployeeApplications(String employeeEmail) {
        Employee creator = employeeService.findByEmail(employeeEmail);
        return applicationRepository.findAllByCreator(creator);
    }

    public List<Application> getHeadEmployeeApplications(String employeeEmail) {
        Employee head = employeeService.findByEmail(employeeEmail);
        return applicationRepository.findByMeetingAimEmployeeIsNullOrMeetingAimEmployee(head);
    }

    public List<Application> getActiveApplications() {
        return applicationRepository.findAllByApplicationStatus(ApplicationStatus.PENDING);
    }

    public List<Application> findAllApplications() {
        return applicationRepository.findAll();
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id).get();
    }

    @Transactional
    public void approveApplication(String headEmail, Long applicationId) {
        Application application = applicationRepository.findById(applicationId).get();
        Employee approver = employeeService.findByEmail(headEmail);
        if (application.getApplicationType() == ApplicationType.MEETING) {
            eventService.createMeetingEvent(headEmail, application);
        }
        application.setApplicationStatus(ApplicationStatus.APPROVED);
        application.setUpdateDate(LocalDate.now());
//        application.setProcessor(approver);
        applicationRepository.save(application);
    }

    @Transactional
    public void rejectApplication(String headEmail, Long requestId, ApplicationRejectionRequest request) {
        Application application = applicationRepository.findById(requestId).get();
        Employee rejector = employeeService.findByEmail(headEmail);
        application.setApplicationStatus(ApplicationStatus.REJECTED);
        application.setUpdateDate(LocalDate.now());
        application.setRejectReason(request.getReason());
//        application.setProcessor(rejector);
        applicationRepository.save(application);
    }

}
