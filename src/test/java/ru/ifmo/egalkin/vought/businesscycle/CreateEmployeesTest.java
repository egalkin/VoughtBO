package ru.ifmo.egalkin.vought.businesscycle;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.ifmo.egalkin.vought.controller.request.EmployeeCreationRequest;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.service.EmployeeService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/businesscycle/create-businesscycle-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@WithUserDetails(value = "edgar@vought.com")
public class CreateEmployeesTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeCreationRequest createEmployeeRequest(Department department) {
        return new EmployeeCreationRequest("Владилен", "Владиленов", department);
    }

    @Test
    public void test01_createEmployeeHEADTest(){
        assertFalse(employeeRepository.existsById(10L));
        employeeService.addEmployee(createEmployeeRequest(Department.HERO));
        assertTrue(employeeRepository.existsById(10L));
        assertEquals(employeeRepository.findById(10L).get().getDepartment(), Department.HERO);
    }

    @Test
    public void test02_createEmployeeLABORATORYTest() {
        assertFalse(employeeRepository.existsById(10L));
        employeeService.addEmployee(createEmployeeRequest(Department.LABORATORY));
        assertTrue(employeeRepository.existsById(10L));
        assertEquals(employeeRepository.findById(10L).get().getDepartment(), Department.LABORATORY);
    }

    @Test
    public void test03_createEmployeePRTest() {
        assertFalse(employeeRepository.existsById(10L));
        employeeService.addEmployee(createEmployeeRequest(Department.PR));
        assertTrue(employeeRepository.existsById(10L));
        assertEquals(employeeRepository.findById(10L).get().getDepartment(), Department.PR);
    }

    @Test
    public void test04_createEmployeeHEROTest() {
        assertFalse(employeeRepository.existsById(10L));
        employeeService.addEmployee(createEmployeeRequest(Department.HERO));
        assertTrue(employeeRepository.existsById(10L));
        assertEquals(employeeRepository.findById(10L).get().getDepartment(), Department.HERO);
    }

    @Test
    public void test05_createEmployeeSECURITYTest() {
        assertFalse(employeeRepository.existsById(10L));
        employeeService.addEmployee(createEmployeeRequest(Department.SECURITY));
        assertTrue(employeeRepository.existsById(10L));
        assertEquals(employeeRepository.findById(10L).get().getDepartment(), Department.SECURITY);
    }


}
