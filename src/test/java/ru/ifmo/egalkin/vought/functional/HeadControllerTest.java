package ru.ifmo.egalkin.vought.functional;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.ifmo.egalkin.vought.controller.request.*;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.enums.ApplicationStatus;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.model.rrepository.ApplicationRepository;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.service.ApplicationService;
import ru.ifmo.egalkin.vought.service.EmployeeService;

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
@WithUserDetails(value = "edgar@vought.com")
public class HeadControllerTest {

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

    @Test
    public void headEmployeesPageTest() throws Exception {
        this.mockMvc.perform(get("/head/employees"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Head Subsystem"))
                .andExpect(xpath("/html/body/div/section/div[2]/a[1]").string("Персонал"))
                .andExpect(xpath("/html/body/div/section/div[1]/a").string("Создать сотрудника"))
                .andExpect(status().isOk());;

        Assertions.assertNotNull(employeeRepository.findAll());
    }

    @Test
    public void editEmployeeTest() throws Exception {
        this.mockMvc.perform(get("/head/employees/hero/2"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Long id = 2L;
        Employee employee = employeeRepository.findById(id).get();
        EmployeeUpdateRequest employeeUpdateRequest = new EmployeeUpdateRequest();
        employeeUpdateRequest.setFirstName("Энни");
        employeeService.editEmployee(id, employeeUpdateRequest);
        Employee employee1 = employeeRepository.findById(id).get();
        assertEquals(employee1.getFirstName(), "Энни");
        assertNotEquals(employee.getFirstName(), employee1.getFirstName());

    }

    @Test
    public void editHeroTest() throws Exception {
        this.mockMvc.perform(get("/head/employees/hero/4"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Long id = 4L;
        Employee employee = employeeRepository.findById(id).get();
        HeroUpdateRequest heroUpdateRequest = new HeroUpdateRequest();
        heroUpdateRequest.setNickname("Никнейм");
        heroUpdateRequest.setPowerDescription("Описание способностей");
        assertNull(employee.getNickname());
        assertNull(employee.getPowerDescription());
        employeeService.editHero(id, heroUpdateRequest);
        employee = employeeRepository.findById(id).get();
        assertNotNull(employee.getNickname());
        assertNotNull(employee.getPowerDescription());
    }

    @Test
    public void deleteEmployeeTest() throws Exception {
        this.mockMvc.perform(get("/head/employees/2/delete"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Long id = 2L;
        assertTrue(employeeRepository.existsById(id));
        employeeService.deactivate(id);
        assertFalse(employeeRepository.existsById(id));
    }

    @Test
    public void createEmployeeTest() throws Exception {

        this.mockMvc.perform(get("/head/employees/new"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Long id = 100L;
        assertFalse(employeeRepository.existsById(id));
        EmployeeCreationRequest employeeCreationRequest = new EmployeeCreationRequest();
        employeeCreationRequest.setFirstName("Владилен");
        employeeCreationRequest.setLastName("Владиленов");
        employeeCreationRequest.setDepartment(Department.HEAD);
        employeeService.addEmployee(employeeCreationRequest);
        assertTrue(employeeRepository.existsById(id));
    }

    @Test
    public void headApplicationsPageTest() throws Exception {
        this.mockMvc.perform(get("/head/applications"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Head Subsystem"))
                .andExpect(xpath("/html/body/div/div/H1").string("Заявки"))
                .andExpect(status().isOk());
    }

    @Test
    public void approveApplicationTest() throws Exception {
        this.mockMvc.perform(get("/head/applications/3"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Long id = 3L;
        Application application = applicationRepository.findById(id).get();
        assertEquals(application.getApplicationStatus(), ApplicationStatus.PENDING);
        applicationService.approveApplication("edgar@vought.com", id);
        application = applicationRepository.findById(id).get();
        assertEquals(application.getApplicationStatus(), ApplicationStatus.APPROVED);
    }

    @Test
    public void rejectedApplicationTest() throws Exception {
        this.mockMvc.perform(get("/head/applications/3"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Long id = 3L;
        Application application = applicationRepository.findById(3L).get();
        assertEquals(application.getApplicationStatus(), ApplicationStatus.PENDING);
        ApplicationRejectionRequest applicationRejectionRequest = new ApplicationRejectionRequest("Причина отмены");
        applicationService.rejectApplication("edgar@vought.com", id, applicationRejectionRequest);
        application = applicationRepository.findById(id).get();
        assertEquals(application.getApplicationStatus(), ApplicationStatus.REJECTED);
    }

    @Test
    public void calendarPageTest() throws Exception {
        this.mockMvc.perform(get("/head/calendar"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/H1").string("Календарь"))
                .andExpect(status().isOk());
    }

    @Test
    public void sensorsPageTest() throws Exception {
        this.mockMvc.perform(get("/head/sensors"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/section/div[1]/H2").string("Данные сенсоров"))
                .andExpect(xpath("/html/body/div/section/div[2]/H2").string("Список всех происшествий"))
                .andExpect(status().isOk());
    }

    @Test
    public void incidentPageTest() throws Exception {
        this.mockMvc.perform(get("/head/sensors/incidents/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/H1").string("Описание происшествия"))
                .andExpect(status().isOk());
    }
}
