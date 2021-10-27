package ru.ifmo.egalkin.vought.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.egalkin.vought.controller.request.EventCreationRequest;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Event;
import ru.ifmo.egalkin.vought.model.enums.EventAggregationType;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.model.rrepository.EventRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void createEvent(String prManagerEmail, EventCreationRequest request) {
        Employee prManager = employeeRepository.findByEmail(prManagerEmail);
        Event event = Event.builder()
                .name(request.getName())
                .priority(request.getPriority())
                .eventTime(request.getEventDateTimeDto().toLocalDateTime())
                .address(request.getAddress())
                .description(request.getDescription())
                .creator(prManager)
                .build();
        eventRepository.save(event);
        List<Employee> heroes = employeeRepository.findAllById(request.getHeroesIds());
        for (Employee hero : heroes) {
            hero.addEvent(event);
            employeeRepository.save(hero);
        }
    }

    @Transactional
    public void createMeetingEvent(String headEmail, Application application) {
        Employee headManager = employeeRepository.findByEmail(headEmail);
        Employee meetingCreator = application.getCreator();
        Event event = Event.builder()
                .name(application.getName())
                .priority(3)
                .eventTime(application.getMeetingTime())
                .address("Головной офис Vought")
                .description(application.getDescription())
                .creator(meetingCreator)
                .meetingAimEmployee(headManager)
                .build();
        eventRepository.save(event);
        headManager.addEvent(event);
        meetingCreator.addEvent(event);
        employeeRepository.save(headManager);
        employeeRepository.save(meetingCreator);
    }

    public List<Event> getEmployeeActualEventsByAggregationType(String employeeEmail,
                                                                EventAggregationType aggregationType) {
        Employee employee = employeeRepository.findByEmail(employeeEmail);
        return getEmployeeActualEventsByAggregationType(employee.getEvents(), aggregationType);
    }

    public List<Event> getEmployeeActualEventsByAggregationType(Collection<Event> events,
                                                                EventAggregationType aggregationType) {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now();
        if (aggregationType == EventAggregationType.DAY) {
            to = from.plusDays(1);
        } else if (aggregationType == EventAggregationType.WEEK) {
            to = from.plusWeeks(1);
        } else if (aggregationType == EventAggregationType.MONTH) {
            to = from.plusMonths(1);
        }
        LocalDateTime finalTo = to;
        return events
                .stream()
                .filter(event -> event.getEventTime().isAfter(from)
                        && event.getEventTime().isBefore(finalTo))
                .collect(Collectors.toList());
    }

}
