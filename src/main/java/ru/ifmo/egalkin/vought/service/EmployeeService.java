package ru.ifmo.egalkin.vought.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.egalkin.vought.controller.request.EmployeeCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.EmployeeUpdateRequest;
import ru.ifmo.egalkin.vought.controller.request.HeroUpdateRequest;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Role;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.model.rrepository.RoleRepository;
import ru.ifmo.egalkin.vought.utils.EmailUtils;
import ru.ifmo.egalkin.vought.utils.TransliterateUtils;

import java.util.List;

import static ru.ifmo.egalkin.vought.utils.TransliterateUtils.transliterate;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;

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
        Employee hero = employeeRepository.getById(heroId);
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
        for (Employee hero : warderHeroes) {
            hero.setPrManager(prManager);
            prManager.addWard(hero);
            employeeRepository.save(hero);
        }
        employeeRepository.save(prManager);
    }

}
