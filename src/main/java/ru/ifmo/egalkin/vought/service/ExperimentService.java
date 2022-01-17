package ru.ifmo.egalkin.vought.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.egalkin.vought.controller.request.ExperimentCreateRequest;
import ru.ifmo.egalkin.vought.controller.request.ExperimentUpdateRequest;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Experiment;
import ru.ifmo.egalkin.vought.model.Subject;
import ru.ifmo.egalkin.vought.model.rrepository.ExperimentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExperimentService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ExperimentRepository experimentRepository;

    public Experiment findById(Long id) {
        return experimentRepository.findById(id).get();
    }

    public List<Experiment> getEmployeeExperiments(String employeeEmail) {
        Employee participant = employeeService.findByEmail(employeeEmail);
        return new ArrayList<>(participant.getExperiments());
    }

    @Transactional
    public void addExperiment(String scientistEmail, ExperimentCreateRequest request) {
        Employee scientist = employeeService.findByEmail(scientistEmail);
        Experiment experiment = Experiment.builder()
                .name(request.getName())
                .description(request.getDescription())
                .goal(request.getGoal())
                .creator(scientist)
                .build();
        experimentRepository.save(experiment);
        scientist.addExperiment(experiment);
        employeeService.save(scientist);
    }

    @Transactional
    public void editExperiment(Long experimentId, ExperimentUpdateRequest request) {
        Experiment experiment = experimentRepository.findById(experimentId).get();
        if (request.getGoal() != null && !request.getGoal().equals(experiment.getGoal())) {
            experiment.setGoal(request.getGoal());
        }
        if (request.getDescription() != null && !request.getDescription().equals(experiment.getDescription())) {
            experiment.setDescription(request.getDescription());
        }
        experimentRepository.save(experiment);
    }

    @Transactional
    public void addMembersToExperiment(Long experimentId, List<Long> membersIds) {
        Experiment experiment = experimentRepository.findById(experimentId).get();
        List<Employee> newMembers = employeeService.findAllById(membersIds);
        newMembers.forEach(scientist -> {
            scientist.addExperiment(experiment);
        });
        employeeService.saveAll(newMembers);
    }

    @Transactional
    public void addSubjectsToExperiment(Long experimentId, List<Long> subjectIds) {
        Experiment experiment = experimentRepository.findById(experimentId).get();
        List<Subject> newSubjects = subjectService.findAllById(subjectIds);
        newSubjects.forEach(subjects -> {
            subjects.addExperiment(experiment);
        });
        subjectService.saveAll(newSubjects);
    }
}
