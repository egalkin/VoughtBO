package ru.ifmo.egalkin.vought.businesscycle;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.ifmo.egalkin.vought.controller.request.HeroApplicationCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.dto.EventDateTimeDto;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Event;
import ru.ifmo.egalkin.vought.model.enums.ApplicationStatus;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;
import ru.ifmo.egalkin.vought.model.rrepository.ApplicationRepository;
import ru.ifmo.egalkin.vought.model.rrepository.EventRepository;
import ru.ifmo.egalkin.vought.service.ApplicationService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/businesscycle/create-businesscycle-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/businesscycle/create-businesscycle-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ApprovalApplicationsHeadHeroTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private EventRepository eventRepository;

    @Test
    @WithUserDetails(value = "petya@vought.com")
    public void test01_createApplicationForMeetingWithCEO() {
        HeroApplicationCreationRequest request = new HeroApplicationCreationRequest(
                "Заявка на встречу с руководством",
                "Описание заявки",
                new EventDateTimeDto(LocalDate.of(2022, 2, 2), "12:12"), 1L);

        applicationService.createHeroRequest("petya@vought.com", request);
        assertTrue(applicationRepository.existsById(10L));
        Application beforeApprove = applicationRepository.findById(10L).get();
        assertEquals(beforeApprove.getCreator().getId(), 4L);
        assertEquals(beforeApprove.getApplicationType(), ApplicationType.MEETING);
        assertEquals(beforeApprove.getApplicationStatus(), ApplicationStatus.PENDING);
    }

    @Test
    @WithUserDetails(value = "edgar@vought.com")
    public void test02_approveHeroApplicationByCEO() {
        test01_createApplicationForMeetingWithCEO();
        Application beforeApprove = applicationRepository.findById(10L).get();
        assertEquals(beforeApprove.getApplicationStatus(), ApplicationStatus.PENDING);
        applicationService.approveApplication("edgar@vought.com", 10L);
        Application afterApprove = applicationRepository.findById(10L).get();
        assertEquals(afterApprove.getApplicationStatus(), ApplicationStatus.APPROVED);
    }

    @Test
    @WithUserDetails(value = "edgar@vought.com")
    public void test03_checkCEOHasNewEventForMeeting() {
        test02_approveHeroApplicationByCEO();
        assertTrue(eventRepository.existsById(11L));
        Event event = eventRepository.findById(11L).get();
        assertEquals(event.getCreator().getId(), 4L);
    }

    @Test
    @WithUserDetails(value = "petya@vought.com")
    public void test04_checkApproveApplicationFromHero() {
        test03_checkCEOHasNewEventForMeeting();
        Application application = applicationRepository.findById(10L).get();
        assertEquals(application.getApplicationStatus(), ApplicationStatus.APPROVED);
    }

    @Test
    @WithUserDetails(value = "petya@vought.com")
    public void test05_checkHeroHasNewEventForMeeting() {
        test03_checkCEOHasNewEventForMeeting();
        assertTrue(eventRepository.existsById(11L));
        Event event = eventRepository.findById(11L).get();
        assertEquals(event.getCreator().getId(), 4L);
        assertEquals(event.getMeetingAimEmployee().getId(), 1L);
    }


}
