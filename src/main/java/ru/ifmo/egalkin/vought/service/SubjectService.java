package ru.ifmo.egalkin.vought.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.egalkin.vought.controller.request.IncidentCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.SubjectCreationRequest;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Incident;
import ru.ifmo.egalkin.vought.model.Subject;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.model.rrepository.IncidentRepository;
import ru.ifmo.egalkin.vought.model.rrepository.SubjectRepository;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Transactional
    public void addSubject(SubjectCreationRequest request) {

        Subject subject = Subject.builder()
                .nickname(request.getNickname())
                .build();
        subjectRepository.save(subject);
    }
}
