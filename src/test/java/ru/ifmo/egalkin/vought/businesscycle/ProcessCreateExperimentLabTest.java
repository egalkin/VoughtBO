package ru.ifmo.egalkin.vought.businesscycle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.ifmo.egalkin.vought.controller.request.ExperimentCreateRequest;
import ru.ifmo.egalkin.vought.controller.request.SubjectCreationRequest;
import ru.ifmo.egalkin.vought.model.rrepository.ExperimentRepository;
import ru.ifmo.egalkin.vought.model.rrepository.SubjectRepository;
import ru.ifmo.egalkin.vought.service.ExperimentService;
import ru.ifmo.egalkin.vought.service.SubjectService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/businesscycle/create-businesscycle-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ProcessCreateExperimentLabTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExperimentService experimentService;

    @Test
    @WithUserDetails(value = "ant@vought.com")
    public void test01_createExperimentWithSubjectTest() {
        ExperimentCreateRequest experimentCreateRequest = new ExperimentCreateRequest(
                "Название эксперимента",
                "Цель эксперимента",
                "Описание эксперимента"
        );
        experimentService.addExperiment("ant@vought.com", experimentCreateRequest);
        assertTrue(experimentRepository.existsById(10L));
    }

    @Test
    @WithUserDetails(value = "ant@vought.com")
    public void test02_createSubjectTest() {
        test01_createExperimentWithSubjectTest();
        SubjectCreationRequest subject = new SubjectCreationRequest("Подопечный 1");
        assertEquals(subjectRepository.count(), 0);
        subjectService.addSubject("ant@vought.com", subject);
        assertEquals(subjectRepository.count(), 1);
        assertTrue(subjectRepository.existsById(11L));
    }

    @Test
    @WithUserDetails(value = "ant@vought.com")
    public void test03_addSubjectToExperimentTest() {
        test02_createSubjectTest();
        assertEquals(experimentRepository.findById(10L).get().getSubjects().size(), 0);
        experimentService.addSubjectsToExperiment(10L, Arrays.asList(11L));
        assertEquals(experimentRepository.findById(10L).get().getSubjects().size(), 1);
    }


}
