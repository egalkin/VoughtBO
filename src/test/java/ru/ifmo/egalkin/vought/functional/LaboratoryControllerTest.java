package ru.ifmo.egalkin.vought.functional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.ifmo.egalkin.vought.controller.request.*;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Experiment;
import ru.ifmo.egalkin.vought.model.Subject;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;
import ru.ifmo.egalkin.vought.model.rrepository.ApplicationRepository;
import ru.ifmo.egalkin.vought.model.rrepository.ExperimentRepository;
import ru.ifmo.egalkin.vought.model.rrepository.SubjectRepository;
import ru.ifmo.egalkin.vought.service.ApplicationService;
import ru.ifmo.egalkin.vought.service.ExperimentService;
import ru.ifmo.egalkin.vought.service.SubjectService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/functional/create-lab-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/functional/create-lab-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails(value = "ant@vought.com")
public class LaboratoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExperimentService experimentService;

    @Test
    public void labApplicationsPageTest() throws Exception {
        this.mockMvc.perform(get("/lab/applications"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/H1").string("????????????"))
                .andExpect(status().isOk());

        assertNotNull(applicationRepository.findAll());
    }

    @Test
    public void labApplicationPageTest() throws Exception {
        this.mockMvc.perform(get("/lab/applications/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/H1").string("????????????"))
                .andExpect(status().isOk());

        assertTrue(applicationRepository.existsById(1L));
    }

    @Test
    public void createLabApplicationsTest() throws Exception {
        this.mockMvc.perform(get("/lab/applications/new"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/div[1]/H1").string("???????????????? ????????????"));

        Long id = 100L;
        ScientistApplicationRequest scientistApplicationRequest = new ScientistApplicationRequest();
        scientistApplicationRequest.setName("???????????? ???? ????????????????????????");
        scientistApplicationRequest.setDescription("????????????????");
        scientistApplicationRequest.setApplicationType(ApplicationType.EQUIPMENT);
        applicationService.createScientistRequest("ant@vought.com", scientistApplicationRequest);
        assertTrue(applicationRepository.existsById(id));
        Application application = applicationRepository.findById(id).get();
        assertEquals(application.getApplicationType(), ApplicationType.EQUIPMENT);
    }

    @Test
    public void labSubjectsPageTest() throws Exception {
        this.mockMvc.perform(get("/lab/subjects"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/H1").string("???????????? ????????????????????"))
                .andExpect(status().isOk());

        assertNotNull(subjectRepository.findAll());
    }

    @Test
    public void labSubjectPageTest() throws Exception {
        this.mockMvc.perform(get("/lab/subjects/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/div[1]/H1").string("?????????????? ??????????????????????"))
                .andExpect(status().isOk());

        assertTrue(subjectRepository.existsById(1L));
    }

    @Test
    public void createSubjectTest() throws Exception {
        this.mockMvc.perform(get("/lab/subject/new"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        SubjectCreationRequest subjectCreationRequest = new SubjectCreationRequest();
        subjectCreationRequest.setNickname("??????");
        subjectService.addSubject("ant@vought.com", subjectCreationRequest);
        assertTrue(subjectRepository.existsById(100L));
    }

    @Test
    public void editSubjectTest() throws Exception {
        this.mockMvc.perform(get("/lab/subjects/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Long id = 1L;
        SubjectUpdateRequest subjectUpdateRequest = new SubjectUpdateRequest();
        subjectUpdateRequest.setNickname("?????????? ??????");
        subjectService.editSubject(1L, subjectUpdateRequest);
        Subject subject = subjectRepository.findById(id).get();
        assertEquals(subject.getNickname(), "?????????? ??????");
    }

    @Test
    public void labExperimentsPageTest() throws Exception {
        this.mockMvc.perform(get("/lab/experiments"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/H1").string("???????????? ??????????????????????????"))
                .andExpect(status().isOk());

        assertNotNull(subjectRepository.findAll());
    }

    @Test
    public void labExperimentPageTest() throws Exception {
        this.mockMvc.perform(get("/lab/experiments/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/H1").string("???????????????? ????????????????????????"))
                .andExpect(status().isOk());

        assertTrue(subjectRepository.existsById(1L));
    }

    @Test
    public void createExperimentTest() throws Exception {
        this.mockMvc.perform(get("/lab/experiment/new"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        ExperimentCreateRequest experimentCreateRequest = new ExperimentCreateRequest();
        experimentCreateRequest.setName("???????????????? ????????????????????????");
        experimentCreateRequest.setGoal("???????? ????????????????????????");
        experimentCreateRequest.setDescription("???????????????? ????????????????????????");
        experimentService.addExperiment("ant@vought.com", experimentCreateRequest);
        assertTrue(experimentRepository.existsById(100L));
    }

    @Test
    public void editExperimentTest() throws Exception {
        this.mockMvc.perform(get("/lab/experiment/new"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Long id = 1L;
        ExperimentUpdateRequest experimentUpdateRequest = new ExperimentUpdateRequest();
        experimentUpdateRequest.setGoal("???????????? ????????");
        experimentUpdateRequest.setDescription("???????????? ???????????????? ");
        experimentService.editExperiment(id, experimentUpdateRequest);
        Experiment experiment = experimentRepository.findById(id).get();
        assertEquals(experiment.getGoal(), "???????????? ????????");
    }

    @Test
    public void addSubjectsToExperiment() throws Exception {
        this.mockMvc.perform(get("/lab/experiments/1/subjects/add"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Long expId = 1L;
        Long subId = 3L;
        Assertions.assertEquals(experimentRepository.findById(expId).get().getSubjects().size(), 2);
        experimentService.addSubjectsToExperiment(expId, Arrays.asList(subId));
        Assertions.assertEquals(experimentRepository.findById(expId).get().getSubjects().size(), 3);

    }

    @Test
    public void addMembersToExperiment() throws Exception {
        this.mockMvc.perform(get("/lab/experiments/1/members/add"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Long expId = 1L;
        Long memId = 3L;
        experimentService.addMembersToExperiment(expId, Arrays.asList(memId));
    }
}
