package ru.ifmo.egalkin.vought.subsystems;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.ifmo.egalkin.vought.controller.HomeController;
import ru.ifmo.egalkin.vought.controller.request.ApplicationRejectionRequest;
import ru.ifmo.egalkin.vought.controller.request.EmployeeCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.EmployeeUpdateRequest;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.enums.ApplicationStatus;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.model.rrepository.ApplicationRepository;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.model.rrepository.RoleRepository;
import ru.ifmo.egalkin.vought.service.ApplicationService;
import ru.ifmo.egalkin.vought.service.EmployeeService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
//@Sql(value = {"/create-employee-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/create-employee-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails(value = "edgar@vought.com")

public class VoughtHeadSubsystemTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HomeController homeController;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    // Сео зашел в головную подсистему на главную страницу
    @Test
    @Sql(value = {"/create-employee-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-employee-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void headHomePageTest() throws Exception {
        this.mockMvc.perform(get("/head/home"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Head Subsystem"))
                .andExpect(xpath("/html/body/div/section/div/a[1]").string("Персонал"))
                .andExpect(xpath("/html/body/div/section/div/a[2]").string("Просмотр состояния"))
                .andExpect(xpath("/html/body/div/section/div/a[3]").string("Календарь"))
                .andExpect(xpath("/html/body/div/section/div/a[4]").string("Заявки"));
    }


    // Сео перешел на страницу Персонал

    @Test
    public void headEmployeePageTest() throws Exception {
        this.mockMvc.perform(get("/head/employees"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Head Subsystem"))
                .andExpect(xpath("/html/body/div/section/div[2]/a[1]").string("Персонал"))
                .andExpect(xpath("/html/body/div/section/div[1]/a").string("Создать сотрудника"));
    }


    // Главный создает сотрудника

    protected Employee createEmployee(String firstname, String lastName, Department department) {
        EmployeeCreationRequest employeeCreationRequest = new EmployeeCreationRequest();
        employeeCreationRequest.setFirstName(firstname);
        employeeCreationRequest.setLastName(lastName);
        employeeCreationRequest.setDepartment(department);
        employeeService.addEmployee(employeeCreationRequest);
        Employee employee = employeeRepository.findById(100L).get();
        return employee;
    }

    @Test
    @Sql(value = {"/create-employee-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-employee-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createEmployeeTest() throws Exception {

        this.mockMvc.perform(get("/head/employees/new"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Head Subsystem"))
                .andExpect(xpath("/html/body/div/div/a[1]").string("Персонал"))
                .andExpect(xpath("/html/body/div/form/button").string("Добавить"));

        Employee employee = createEmployee("Владилен", "Владиленов", Department.HEAD);

        Employee employee1 = employeeRepository.findById(employee.getId()).get();

        assertNotNull(employee1);

        String firstName = employee1.getFirstName();
        assertEquals(firstName, "Владилен");

        String lastName = employee1.getLastName();
        assertEquals(lastName, "Владиленов");

        assertEquals(employeeRepository.countEmployeeByEmailAndPassword(employee.getEmail(), employee.getPassword()), 1);

    }


    // Редактирование данных сотрудника

    @Test
    @Sql(value = {"/create-employee-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-employee-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void editEmployeeTest() throws Exception {

        Employee employee = createEmployee("Владилен", "Владиленов", Department.HEAD);

        this.mockMvc.perform(get("/head/employees/hero/100"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Head Subsystem"))
                .andExpect(xpath("/html/body/div/div/a[1]").string("Персонал"))
                .andExpect(xpath("/html/body/div/form/button").string("Изменить"));

        Long idEmployee = employee.getId();
        String oldEmail = employee.getEmail();
        String oldPassword = employee.getPassword();

        EmployeeUpdateRequest employeeUpdateRequest = new EmployeeUpdateRequest();
        employeeUpdateRequest.setFirstName("Жора");
        employeeService.editEmployee(idEmployee, employeeUpdateRequest);

        Employee employee1 = employeeRepository.findById(idEmployee).get();

        String newEmail = employee1.getEmail();
        String newPassword = employee1.getPassword();

        String newFirstName = employee1.getFirstName();
        assertEquals(newFirstName, "Жора");

        String lastName = employee.getLastName();
        assertEquals(lastName, "Владиленов");

        assertEquals(employeeRepository.countEmployeeByEmailAndPassword(oldEmail, oldPassword), 0);
        assertEquals(employeeRepository.countEmployeeByEmailAndPassword(newEmail, newPassword), 1);

    }

    // Просмотр вкладки с заявками со страницы главного

    @Test
    @Sql(value = {"/create-employee-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-employee-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void headApplicationsPageTest() throws Exception {
        this.mockMvc.perform(get("/head/applications"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Head Subsystem"))
                .andExpect(xpath("/html/body/div/div/H1").string("Заявки"))
                .andExpect(xpath("/html/body/div/section/div[2]/a[3]").string("Заявки"));
    }

    // Отклонение заявки сео

    @Test
    @Sql(value = {"/create-application-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-application-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void rejectedApplicationTest() {

        Application application = applicationRepository.findById(3L).get();
        ApplicationStatus applicationStatus = application.getApplicationStatus();
        assertEquals(applicationStatus, ApplicationStatus.PENDING);

        String reason = "Причина отмены";
        ApplicationRejectionRequest applicationRejectionRequest = new ApplicationRejectionRequest(reason);

        applicationService.rejectApplication("edgar@vought.com", 3L, applicationRejectionRequest);

        Application application2 = applicationRepository.findById(3L).get();
        ApplicationStatus applicationStatus2 = application2.getApplicationStatus();

        assertEquals(applicationStatus2, ApplicationStatus.REJECTED);
    }

    // Одобрение заявки сео

    @Test
    @Sql(value = {"/create-application-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-application-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void approveApplicationTest() {

        Application application = applicationRepository.findById(3L).get();
        ApplicationStatus applicationStatus = application.getApplicationStatus();
        assertEquals(applicationStatus, ApplicationStatus.PENDING);

        applicationService.approveApplication("edgar@vought.com", 3L);

        Application application2 = applicationRepository.findById(3L).get();
        ApplicationStatus applicationStatus2 = application2.getApplicationStatus();

        assertEquals(applicationStatus2, ApplicationStatus.APPROVED);
    }

}