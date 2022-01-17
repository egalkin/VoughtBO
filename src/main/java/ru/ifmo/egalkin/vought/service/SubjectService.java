package ru.ifmo.egalkin.vought.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.egalkin.vought.controller.request.SubjectCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.SubjectUpdateRequest;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Experiment;
import ru.ifmo.egalkin.vought.model.Subject;
import ru.ifmo.egalkin.vought.model.rrepository.SubjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> findAllPossibleExperimentSubjects(Long experimentId) {
        Experiment experiment = experimentService.findById(experimentId);
        return subjectRepository.findAll()
                .stream()
                .filter(subj -> !subj.getExperiments().contains(experiment))
                .collect(Collectors.toList());
    }

    public Subject findById(Long id) {
        return subjectRepository.findById(id).get();
    }

    public List<Subject> findAllById(List<Long> ids) {
        return subjectRepository.findAllById(ids);
    }

    @Transactional
    public void addSubject(String mentorEmail, SubjectCreationRequest request) {
        Employee mentor = employeeService.findByEmail(mentorEmail);
        Subject subject = Subject.builder()
                .nickname(request.getNickname())
                .mentor(mentor)
                .build();
        subjectRepository.save(subject);
    }


    @Transactional
    public void editSubject(Long subjectId, SubjectUpdateRequest request) {
        Subject subject = subjectRepository.findById(subjectId).get();
        if (request.getNickname() != null && !request.getNickname().equals(subject.getNickname())) {
            subject.setNickname(request.getNickname());
        }
        subjectRepository.save(subject);
    }

    public void save(Subject subject) {
        subjectRepository.save(subject);
    }

    public void saveAll(List<Subject> subjects) {
        subjectRepository.saveAll(subjects);
    }

}
