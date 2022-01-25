package ru.ifmo.egalkin.vought.businesscycle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.ifmo.egalkin.vought.controller.request.PrApplicationCreationRequest;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.enums.ApplicationStatus;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;
import ru.ifmo.egalkin.vought.model.rrepository.ApplicationRepository;
import ru.ifmo.egalkin.vought.service.ApplicationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/businesscycle/create-businesscycle-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CeationAndApprovedPRStrategyTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    @Test
    @WithUserDetails(value = "vas@vought.com")
    public void test01_createPRApplicationTest() {
        PrApplicationCreationRequest prApplicationCreationRequest = new PrApplicationCreationRequest(
                "Заявка на создание PR стратегии",
                "Описание заявки"
        );
        applicationService.createPrRequest("vas@vought.com", prApplicationCreationRequest);
        assertTrue(applicationRepository.existsById(10L));
        Application application = applicationRepository.findById(10L).get();
        assertEquals(application.getApplicationType(), ApplicationType.PR_STRATEGY);
        assertEquals(application.getApplicationStatus(), ApplicationStatus.PENDING);
    }

    @Test
    @WithUserDetails(value = "edgar@vought.com")
    public void test02_approvePRApplicationByCEO() {
        test01_createPRApplicationTest();
        Application beforeApprove = applicationRepository.findById(10L).get();
        assertEquals(beforeApprove.getApplicationStatus(), ApplicationStatus.PENDING);
        applicationService.approveApplication("edgar@vought.com", 10L);
        Application afterApprove = applicationRepository.findById(10L).get();
        assertEquals(afterApprove.getApplicationStatus(), ApplicationStatus.APPROVED);
    }

    @Test
    @WithUserDetails(value = "ant@vought.com")
    public void test03_checkApprovedApplicationFromPR() {
        test02_approvePRApplicationByCEO();
        Application application = applicationRepository.findById(10L).get();
        assertEquals(application.getApplicationStatus(), ApplicationStatus.APPROVED);
    }
}
