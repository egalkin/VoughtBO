package ru.ifmo.egalkin.vought.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.utils.EmailUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.ifmo.egalkin.vought.utils.TransliterateUtils.transliterate;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
//@TestPropertySource("/application-test.properties")
@Sql(value = {"/businesscycle/create-businesscycle-after.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/businesscycle/create-businesscycle-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VolumeTest {

    @Autowired
    private EmployeeRepository employeeRepository;

//    @BeforeEach
//    @AfterEach
//    void tearDown() {
//        employeeRepository.deleteAll();
//    }

    void createEmployees(int count) {
        List<Employee> employees = new ArrayList<>();
        String firstNave = "Эдвард";
        String lastName = "Свонс";
        String emailForEmployee = EmailUtils.generateEmailForEmployee(
                transliterate(firstNave),
                transliterate(lastName)
        );
        for (int i = 0; i < count; i++) {
            Employee employee = Employee.builder()
                    .firstName(firstNave)
                    .lastName(lastName)
                    .email(emailForEmployee)
                    .password(emailForEmployee)
                    .department(Department.HEAD)
                    .build();
            employees.add(employee);
        }
        employeeRepository.saveAll(employees);
    }

    @Test
    void test01_1000_users() {
        int actual = 1000;
        createEmployees(actual);

        int expected = employeeRepository.findAll().size();

        Assertions.assertEquals(expected, actual);

        long start = System.nanoTime();
        employeeRepository.findByEmail("Edvard.Svons1@vought.com");
        long time = System.nanoTime() - start;

        System.out.println("Время выполнения запроса:" + time/1_000_000_000.0 + " секунд");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test01_1000_users\nВремя выполнения запроса: " + time_count + " секунд\n";
        appendToFile(out);
    }

    @Test
    void test02_10000_users() {
        int actual = 10000;
        createEmployees(actual);

        int expected = employeeRepository.findAll().size();

        Assertions.assertEquals(expected, actual);

        long start = System.nanoTime();
        employeeRepository.findByEmail("Edvard.Svons1@vought.com");
        long time = System.nanoTime() - start;

        System.out.println("Время выполнения запроса:" + time/1_000_000_000.0 + " секунд");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test02_10000_users\nВремя выполнения запроса: " + time_count + " секунд\n";
        appendToFile(out);
    }

    @Test
    void test03_100000_users() {
        int actual = 100000;
        createEmployees(actual);

        int expected = employeeRepository.findAll().size();

        Assertions.assertEquals(expected, actual);

        long start = System.nanoTime();
        employeeRepository.findByEmail("Edvard.Svons1@vought.com");
        long time = System.nanoTime() - start;

        System.out.println("Время выполнения запроса:" + time/1_000_000_000.0 + " секунд");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test03_100000_users\nВремя выполнения запроса: " + time_count + " секунд\n";
        appendToFile(out);
    }

    @Test
    void test04_500000_users() {
        int actual = 500000;
        createEmployees(actual);

        int expected = employeeRepository.findAll().size();

        Assertions.assertEquals(expected, actual);

        long start = System.nanoTime();
        employeeRepository.findByEmail("Edvard.Svons1@vought.com");
        long time = System.nanoTime() - start;

        System.out.println("Время выполнения запроса:" + time/1_000_000_000.0 + " секунд");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test04_500000_users\nВремя выполнения запроса: " + time_count + " секунд\n";
        appendToFile(out);
    }

    @Test
    void test05_1000000_users() {
        int actual = 1000000;
        createEmployees(actual);

        int expected = employeeRepository.findAll().size();

        Assertions.assertEquals(expected, actual);

        long start = System.nanoTime();
        employeeRepository.findByEmail("Edvard.Svons1@vought.com");
        long time = System.nanoTime() - start;

        System.out.println("Время выполнения запроса:" + time/1_000_000_000.0 + " секунд");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test05_2000000_users\nВремя выполнения запроса: " + time_count + " секунд\n";
        appendToFile(out);
    }

    private static void appendToFile(String data) {
        try
        {
            String filename= "output.txt";
            FileWriter fw = new FileWriter(filename,true);
            fw.write(data);
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
}
