package ru.ifmo.egalkin.vought.businesscycle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.ifmo.egalkin.vought.controller.request.IncidentCreationRequest;
import ru.ifmo.egalkin.vought.model.enums.IncidentType;
import ru.ifmo.egalkin.vought.model.rrepository.IncidentRepository;
import ru.ifmo.egalkin.vought.service.IncidentService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/businesscycle/create-businesscycle-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

public class HeroAndSecurityIncidentProcessingTest {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentService incidentService;

    @Test
    @WithUserDetails(value = "vas@vought.com")
    public void test01_createIncidentBySecurityTest() {
        IncidentCreationRequest incidentCreationRequest = new IncidentCreationRequest(
                "Адрес происшествия",
                IncidentType.ROBBERY,
                5,
                5,
                "Информация о преступлении"
        );
        assertFalse(incidentRepository.existsById(10L));
        assertEquals(incidentRepository.findAllByActive(true).size(), 0);
        incidentService.createIncident("vas@vought.com", incidentCreationRequest);
        assertTrue(incidentRepository.existsById(10L));
        assertEquals(incidentRepository.findAllByActive(true).size(), 1);
    }

    @Test
    @WithUserDetails(value = "petya@vought.com")
    public void test02_heroReactOnIncidentTest() {
        assertEquals(incidentRepository.findAllByActive(true).size(), 0);
        test01_createIncidentBySecurityTest();
        assertEquals(incidentRepository.findAllByActive(true).size(), 1);
        assertTrue(incidentRepository.findById(10L).get().isActive());
        incidentService.reactOnIncident(10L);
        assertFalse(incidentRepository.findById(10L).get().isActive());
    }

    @Test
    @WithUserDetails(value = "vas@vought.com")
    public void test03_securityCheckIncidentResolutionTest() {
        test02_heroReactOnIncidentTest();
        assertFalse(incidentRepository.findById(10L).get().isActive());
        assertEquals(incidentRepository.findAllByActive(true).size(), 0);
    }
}
