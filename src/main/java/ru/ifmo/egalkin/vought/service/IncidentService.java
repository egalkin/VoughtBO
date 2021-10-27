package ru.ifmo.egalkin.vought.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.egalkin.vought.controller.request.IncidentCreationRequest;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Incident;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.model.rrepository.IncidentRepository;

import java.lang.annotation.Target;

@Service
public class IncidentService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private IncidentRepository incidentRepository;

    @Transactional
    public void createIncident(String securityEmail, IncidentCreationRequest request) {
        Employee securityManager = employeeRepository.findByEmail(securityEmail);
        Incident incident = Incident.builder()
                .address(request.getAddress())
                .incidentType(request.getIncidentType())
                .armamentLevel(request.getArmamentLevel())
                .enemiesNumber(request.getEnemiesNumber())
                .info(request.getInfo())
                .active(true)
                .creator(securityManager)
                .build();
        incidentRepository.save(incident);
    }

    @Transactional
    public void reactOnIncident(Long incidentId) {
        Incident incident = incidentRepository.findById(incidentId).get();
        incident.setActive(false);
        incidentRepository.save(incident);
    }

}
