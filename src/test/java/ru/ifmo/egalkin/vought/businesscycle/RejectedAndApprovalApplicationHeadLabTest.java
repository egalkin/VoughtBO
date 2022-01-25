package ru.ifmo.egalkin.vought.businesscycle;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.ifmo.egalkin.vought.controller.request.ApplicationRejectionRequest;
import ru.ifmo.egalkin.vought.controller.request.ScientistApplicationRequest;
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
public class RejectedAndApprovalApplicationHeadLabTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    @Test
    @WithUserDetails(value = "ant@vought.com")
    public void test01_createLabResearchAndEquipmentAppTest() {

        ScientistApplicationRequest researchRequest = new ScientistApplicationRequest(
                "Заявка на исследование",
                ApplicationType.RESEARCH,
                "Описание заявки на исследование");


        ScientistApplicationRequest equipmentRequest = new ScientistApplicationRequest(
                "Заявка на оборудование",
                ApplicationType.EQUIPMENT,
                "Описание заявки на оборудование");

        applicationService.createScientistRequest("ant@vought.com", researchRequest);
        assertTrue(applicationRepository.existsById(10L));
        Application beforeApproveResearch = applicationRepository.findById(10L).get();
        assertEquals(beforeApproveResearch.getCreator().getId(), 2L);
        assertEquals(beforeApproveResearch.getApplicationType(), ApplicationType.RESEARCH);
        assertEquals(beforeApproveResearch.getApplicationStatus(), ApplicationStatus.PENDING);

        applicationService.createScientistRequest("ant@vought.com", equipmentRequest);
        assertTrue(applicationRepository.existsById(11L));
        Application beforeApproveEquipment = applicationRepository.findById(11L).get();
        assertEquals(beforeApproveEquipment.getCreator().getId(), 2L);
        assertEquals(beforeApproveEquipment.getApplicationType(), ApplicationType.EQUIPMENT);
        assertEquals(beforeApproveEquipment.getApplicationStatus(), ApplicationStatus.PENDING);
    }

    @Test
    @WithUserDetails(value = "edgar@vought.com")
    public void test02_approveLabResearchApplicationByCEO() {
        test01_createLabResearchAndEquipmentAppTest();
        Application beforeApprove = applicationRepository.findById(10L).get();
        assertEquals(beforeApprove.getApplicationStatus(), ApplicationStatus.PENDING);
        applicationService.approveApplication("edgar@vought.com", 10L);
        Application afterApprove = applicationRepository.findById(10L).get();
        assertEquals(afterApprove.getApplicationStatus(), ApplicationStatus.APPROVED);
    }

    @Test
    @WithUserDetails(value = "ant@vought.com")
    public void test03_checkApprovedResearchApplicationFromLab() {
        test02_approveLabResearchApplicationByCEO();
        Application application = applicationRepository.findById(10L).get();
        assertEquals(application.getApplicationStatus(), ApplicationStatus.APPROVED);
    }

    @Test
    @WithUserDetails(value = "edgar@vought.com")
    public void test04_RejectedLabEquipmentApplicationByCEO() {
        test01_createLabResearchAndEquipmentAppTest();
        Application beforeApprove = applicationRepository.findById(10L).get();
        assertEquals(beforeApprove.getApplicationStatus(), ApplicationStatus.PENDING);
        ApplicationRejectionRequest rejectionRequest = new ApplicationRejectionRequest("Причина отмены заявки");
        applicationService.rejectApplication("edgar@vought.com", 10L, rejectionRequest);
        Application afterApprove = applicationRepository.findById(10L).get();
        assertEquals(afterApprove.getApplicationStatus(), ApplicationStatus.REJECTED);
    }

    @Test
    @WithUserDetails(value = "ant@vought.com")
    public void test05_checkRejectedEquipmentApplicationFromHero() {
        test04_RejectedLabEquipmentApplicationByCEO();
        Application application = applicationRepository.findById(10L).get();
        assertEquals(application.getApplicationStatus(), ApplicationStatus.REJECTED);
    }

}
