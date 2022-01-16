package ru.ifmo.egalkin.vought.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.egalkin.vought.controller.request.IncidentCreationRequest;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Incident;
import ru.ifmo.egalkin.vought.model.rrepository.IncidentRepository;

import java.util.List;

@Service
public class IncidentService {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private IncidentRepository incidentRepository;

    public Incident findFirstByActive(Boolean active) {
        return incidentRepository.findFirstByActive(active);
    }

    public List<Incident> findAllByActive(Boolean active) {
        return incidentRepository.findAllByActive(active);
    }

    @Transactional
    public void createIncident(String securityEmail, IncidentCreationRequest request) {
        Employee securityManager = employeeService.findByEmail(securityEmail);
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
