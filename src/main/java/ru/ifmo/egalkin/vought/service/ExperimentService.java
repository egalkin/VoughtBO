package ru.ifmo.egalkin.vought.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.egalkin.vought.controller.request.ExperimentCreateRequest;
import ru.ifmo.egalkin.vought.controller.request.SubjectCreationRequest;
import ru.ifmo.egalkin.vought.model.Experiment;
import ru.ifmo.egalkin.vought.model.Subject;
import ru.ifmo.egalkin.vought.model.rrepository.ExperimentRepository;

@Service
public class ExperimentService {

    @Autowired
    private ExperimentRepository experimentRepository;

    @Transactional
    public void addExperiment(ExperimentCreateRequest request) {
        Experiment experiment = Experiment.builder()
                .name(request.getName())
                .description(request.getDescription())
                .goal(request.getGoal())
                .build();
        experimentRepository.save(experiment);
    }
}
