package ru.ifmo.egalkin.vought.functional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.ifmo.egalkin.vought.controller.request.HeroApplicationCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.dto.EventDateTimeDto;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Incident;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;
import ru.ifmo.egalkin.vought.model.rrepository.ApplicationRepository;
import ru.ifmo.egalkin.vought.model.rrepository.IncidentRepository;
import ru.ifmo.egalkin.vought.service.ApplicationService;
import ru.ifmo.egalkin.vought.service.IncidentService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-head-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-head-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails(value = "petya@vought.com")
public class HeroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentService incidentService;

    @Test
    public void heroApplicationsPageTest() throws Exception {
        this.mockMvc.perform(get("/hero/applications"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Hero Subsystem"))
                .andExpect(xpath("/html/body/div/div/H1").string("Заявки на встречу с руководством"))
                .andExpect(status().isOk());

        assertNotNull(applicationRepository.findAll());
    }

    @Test
    public void heroApplicationPageTest() throws Exception {
        this.mockMvc.perform(get("/hero/applications/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Hero Subsystem"))
                .andExpect(xpath("/html/body/div/div/H1").string("Заявки на встречу с руководством"))
                .andExpect(status().isOk());

        assertTrue(applicationRepository.existsById(1L));
    }

    @Test
    public void createHeroApplicationsTest() throws Exception {
        this.mockMvc.perform(get("/hero/applications/new"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Hero Subsystem"))
                .andExpect(xpath("/html/body/div/div/div[1]/H1").string("Создание заявки на встречу"));

        Long id = 100L;
        EventDateTimeDto eventDateTimeDto = new EventDateTimeDto(LocalDate.of(2022,2,2), "12:12");
        HeroApplicationCreationRequest heroApplicationCreationRequest = new HeroApplicationCreationRequest();
        heroApplicationCreationRequest.setName("Заявка на встречу от героя");
        heroApplicationCreationRequest.setDescription("Описание заявки заполнено");
        heroApplicationCreationRequest.setEventDateTimeDto(eventDateTimeDto);
        heroApplicationCreationRequest.setMeetingAimEmployeeId(3L);
        applicationService.createHeroRequest("petya@vought.com", heroApplicationCreationRequest);
        assertTrue(applicationRepository.existsById(id));
        Application application = applicationRepository.findById(id).get();
        assertEquals(application.getApplicationType(), ApplicationType.MEETING);
    }

    @Test
    public void heroIncidentsPageTest() throws Exception {
        this.mockMvc.perform(get("/hero/incidents"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Hero Subsystem"))
                .andExpect(xpath("/html/body/div/div/H1").string("Активные события"))
                .andExpect(status().isOk());

        assertNotNull(incidentRepository.findAll());
    }
    @Test
    public void reactOnIncidentTest() throws Exception {
        this.mockMvc.perform(get("/hero/incidents/2"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Hero Subsystem"))
                .andExpect(xpath("/html/body/div/section/div[2]/form/button").string("Среагировать"))
                .andExpect(status().isOk());

        Long id = 2L;
        Incident incident = incidentRepository.findById(id).get();
        assertTrue(incident.isActive());
        incidentService.reactOnIncident(id);
        incident = incidentRepository.findById(id).get();
        assertFalse(incident.isActive());
    }



}
