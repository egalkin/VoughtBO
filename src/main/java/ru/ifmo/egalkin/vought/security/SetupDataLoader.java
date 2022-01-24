package ru.ifmo.egalkin.vought.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.model.Role;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.rrepository.RoleRepository;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;

import java.util.Collections;

/**
 * Created by egalkin
 * Date: 12.10.2021
 */
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        createRoleIfNotFound("ROLE_CEO");
        createRoleIfNotFound("ROLE_HEAD");
        createRoleIfNotFound("ROLE_HERO");
        createRoleIfNotFound("ROLE_PR_MANAGER");
        createRoleIfNotFound("ROLE_SECURITY_MANAGER");
        createRoleIfNotFound("ROLE_SCIENTIST");
        createAdminIfNotFound();

        alreadySetup = true;
    }

    private void createAdminIfNotFound() {
        Employee admin = employeeRepository.findByEmail("edgar@vought.com");
        if (admin == null) {
            Role adminRole = roleRepository.findByName("ROLE_CEO");
            Employee employee = new Employee();
            employee.setFirstName("Эдгар");
            employee.setLastName("Свонс");
            employee.setPassword("test");
            employee.setEmail("edgar@vought.com");
            employee.setRoles(Collections.singletonList(adminRole));
            employee.setDepartment(Department.HEAD);
            employeeRepository.save(employee);
        }
    }

    @Transactional
    void createRoleIfNotFound(String name) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }

}
