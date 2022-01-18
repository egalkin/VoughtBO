package ru.ifmo.egalkin.vought.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.egalkin.vought.controller.request.EmployeeCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.EmployeeUpdateRequest;
import ru.ifmo.egalkin.vought.controller.request.HeroUpdateRequest;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Role;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.model.rrepository.RoleRepository;
import ru.ifmo.egalkin.vought.utils.EmailUtils;

import java.util.List;

import static ru.ifmo.egalkin.vought.utils.TransliterateUtils.transliterate;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;

    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    public List<Employee> findAllByIdNotIn(List<Long> ids) {
        return employeeRepository.findAllByIdNotIn(ids);
    }

    public List<Employee> findByDepartmentNotIn(List<Department> departments) {
        return employeeRepository.findByDepartmentNotIn(departments);
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id).get();
    }

    @Transactional
    public void deactivate(Long id) {
        Employee employee = employeeRepository.findById(id).get();
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    public List<Employee> findAllByDepartment(Department department) {
        return employeeRepository.findAllByDepartment(department);
    }

    public List<Employee> findAllPossibleExperimentMembers(Department department,
                                                           List<Long> ids) {
        return employeeRepository.findAllByDepartmentAndIdNotIn(department, ids);
    }

    public Integer countActiveHeroes() {
        return employeeRepository.countAllByDepartment(Department.HERO);
    }

    public List<Employee> findUnwardedHeroes() {
        return employeeRepository.findUnwardedHeroes();
    }

    public List<Employee> findAllById(List<Long> ids) {
        return employeeRepository.findAllById(ids);
    }

    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    public void saveAll(List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }

    @Transactional
    public void editEmployee(Long employeeId, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.getById(employeeId);
        boolean needToUpdateEmail = false;
        if (request.getFirstName() != null && !request.getFirstName().equals(employee.getFirstName())) {
            employee.setFirstName(request.getFirstName());
            needToUpdateEmail = true;
        }
        if (request.getLastName() != null && !request.getLastName().equals(employee.getLastName())) {
            employee.setLastName(request.getLastName());
            needToUpdateEmail = true;
        }
        if (needToUpdateEmail) {
            employee.setEmail(EmailUtils.generateEmailForEmployee(
                    transliterate(employee.getFirstName()), transliterate(employee.getLastName()))
            );
        }
        employeeRepository.save(employee);
    }

    @Transactional
    public void editHero(Long heroId, HeroUpdateRequest request) {
        Employee hero = employeeRepository.findById(heroId).get();
        boolean needToUpdateEmail = false;
        if (request.getFirstName() != null && !request.getFirstName().equals(hero.getFirstName())) {
            hero.setFirstName(request.getFirstName());
            needToUpdateEmail = true;
        }
        if (request.getLastName() != null && !request.getLastName().equals(hero.getLastName())) {
            hero.setLastName(request.getLastName());
            needToUpdateEmail = true;
        }
        if (request.getNickname() != null && !request.getNickname().equals(hero.getNickname())) {
            hero.setNickname(request.getNickname());
        }
        if (request.getPowerDescription() != null && !request.getPowerDescription().equals(hero.getPowerDescription())) {
            hero.setPowerDescription(request.getPowerDescription());
        }
        if (needToUpdateEmail) {
            hero.setEmail(EmailUtils.generateEmailForEmployee(
                    transliterate(hero.getFirstName()), transliterate(hero.getLastName()))
            );
        }
        employeeRepository.save(hero);
    }

    @Transactional
    public void addEmployee(EmployeeCreationRequest request) {
        String emailForEmployee = EmailUtils.generateEmailForEmployee(
                transliterate(request.getFirstName()), transliterate(request.getLastName())
        );
        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(emailForEmployee)
                .password(emailForEmployee)
                .department(request.getDepartment())
                .build();

        Role role = roleRepository.findByName(employee.getDepartment().getCorrespondentRole());
        employee.setRoles(List.of(role));
        employeeRepository.save(employee);
    }

    @Transactional
    public void wardHeroes(String prManagerEmail, List<Long> wardsIds) {
        Employee prManager = employeeRepository.findByEmail(prManagerEmail);
        List<Employee> warderHeroes = employeeRepository.findAllById(wardsIds);
        warderHeroes.forEach(hero -> {
            hero.setPrManager(prManager);
            prManager.addWard(hero);
            employeeRepository.save(hero);
        });
        employeeRepository.save(prManager);
    }

}
