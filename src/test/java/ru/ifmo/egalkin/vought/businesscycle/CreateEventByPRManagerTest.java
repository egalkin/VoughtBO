package ru.ifmo.egalkin.vought.businesscycle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.ifmo.egalkin.vought.controller.request.EventCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.dto.EventDateTimeDto;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Event;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.model.rrepository.EventRepository;
import ru.ifmo.egalkin.vought.service.EmployeeService;
import ru.ifmo.egalkin.vought.service.EventService;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/businesscycle/create-businesscycle-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CreateEventByPRManagerTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    @Test
    @WithUserDetails(value = "vas@vought.com")
    public void test01_addWardsOfHeroesTest() {
        Employee employee = employeeRepository.findUnwardedHeroes().get(0);
        Long heroId = employee.getId();
        assertNull(employeeRepository.findById(heroId).get().getPrManager());
        employeeService.wardHeroes("vas@vought.com", Arrays.asList(heroId));
        Employee pr = employeeRepository.findById(heroId).get().getPrManager();
        assertNotNull(pr);
        assertEquals(pr.getId(), 3L);
    }

    @Test
    @WithUserDetails(value = "petya@vought.com")
    public void test02_checkHeroHasPRTest() {
        assertNull(employeeRepository.findById(4L).get().getPrManager());
        test01_addWardsOfHeroesTest();
        assertNotNull(employeeRepository.findById(4L).get().getPrManager());
    }

    @Test
    @WithUserDetails(value = "vas@vought.com")
    public void test03_createEventByPRTest() {
        test01_addWardsOfHeroesTest();
        EventDateTimeDto eventDateTime = new EventDateTimeDto(LocalDate.of(2022, 2, 2), "12:12");
        EventCreationRequest eventCreationRequest = new EventCreationRequest(
                "Название мероприятия",
                5,
                eventDateTime,
                "Адрес мероприятия",
                "Описание мероприятия",
                Arrays.asList(4L)

        );
        eventService.createEvent("vas@vought.com", eventCreationRequest);
        assertTrue(eventRepository.existsById(10L));
        Event eventPR = eventRepository.findById(10L).get();
        assertEquals(eventPR.getCreator().getId(), 3L);
    }
}
