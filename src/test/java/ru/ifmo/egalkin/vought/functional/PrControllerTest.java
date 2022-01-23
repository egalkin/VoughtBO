package ru.ifmo.egalkin.vought.functional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.ifmo.egalkin.vought.controller.request.*;
import ru.ifmo.egalkin.vought.controller.request.dto.EventDateTimeDto;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;
import ru.ifmo.egalkin.vought.model.rrepository.ApplicationRepository;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.model.rrepository.EventRepository;
import ru.ifmo.egalkin.vought.service.ApplicationService;
import ru.ifmo.egalkin.vought.service.EmployeeService;
import ru.ifmo.egalkin.vought.service.EventService;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/functional/create-pr-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/functional/create-pr-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails(value = "vas@vought.com")
public class PrControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    @Test
    public void prWardsPageTest() throws Exception {
        this.mockMvc.perform(get("/pr/wards"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div[1]/H1").string("Герои"))
                .andExpect(xpath("/html/body/div/form/ul/li/span").string("Петр Свонс"))
                .andExpect(status().isOk());
    }

    @Test
    public void prAddWardsTest() throws Exception {
        this.mockMvc.perform(get("/pr/wards/add"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div[1]/H1").string("Герои без PR менеджера"))
                .andExpect(status().isOk());

        Long id = 4L;
        employeeService.wardHeroes("vas@vought.com", Arrays.asList(id));
        Employee pr = employeeRepository.findById(id).get().getPrManager();
        assertEquals(pr.getId(), 2L);
    }

    @Test
    public void prCreateEventTest() throws Exception {
        this.mockMvc.perform(get("/pr/events/new?wardsIds=3"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Long id = 3L;
        EventDateTimeDto eventDateTimeDto = new EventDateTimeDto(LocalDate.of(2022, 2, 2), "12:12");
        EventCreationRequest eventCreationRequest = new EventCreationRequest();
        eventCreationRequest.setHeroesIds(Arrays.asList(id));
        eventCreationRequest.setName("Мероприятие 2");
        eventCreationRequest.setAddress("Адрес 2");
        eventCreationRequest.setDescription("Описание 2");
        eventCreationRequest.setEventDateTimeDto(eventDateTimeDto);
        eventCreationRequest.setPriority(5);
        eventService.createEvent("vas@vought.com", eventCreationRequest);
        assertTrue(eventRepository.existsById(100L));
    }

    @Test
    public void proApplicationsPageTest() throws Exception {
        this.mockMvc.perform(get("/pr/applications"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/H1").string("Заявки на PR стратегии"))
                .andExpect(status().isOk());

        assertNotNull(applicationRepository.findAll());
    }

    @Test
    public void prApplicationPageTest() throws Exception {
        this.mockMvc.perform(get("/pr/applications/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/H1").string("Заявка на PR стратегию"))
                .andExpect(status().isOk());

        assertTrue(applicationRepository.existsById(1L));
    }

    @Test
    public void createPRApplicationsTest() throws Exception {
        this.mockMvc.perform(get("/pr/applications/new"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/div[1]/H1").string("Создание PR стратегии"))
                .andExpect(status().isOk());

        Long id = 100L;
        PrApplicationCreationRequest prApplicationCreationRequest = new PrApplicationCreationRequest();
        prApplicationCreationRequest.setName("Заявка на создание PR стратегии");
        prApplicationCreationRequest.setDescription("Описание");
        applicationService.createPrRequest("vas@vought.com", prApplicationCreationRequest);
        assertTrue(applicationRepository.existsById(id));
        Application application = applicationRepository.findById(id).get();
        assertEquals(application.getApplicationType(), ApplicationType.PR_STRATEGY);
    }
}
