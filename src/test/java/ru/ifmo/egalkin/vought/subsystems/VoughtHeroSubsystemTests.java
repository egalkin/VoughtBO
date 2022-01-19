package ru.ifmo.egalkin.vought.subsystems;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.ifmo.egalkin.vought.controller.request.HeroApplicationCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.dto.EventDateTimeDto;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;
import ru.ifmo.egalkin.vought.model.rrepository.ApplicationRepository;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.service.ApplicationService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class VoughtHeroSubsystemTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    // Герой зашел в головную подсистему на главную страницу
    @Test
    @Sql(value = {"/create-employee-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-employee-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails(value = "petya@vought.com")
    public void headHomePageTest() throws Exception {
        this.mockMvc.perform(get("/hero/home"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Hero Subsystem"))
                .andExpect(xpath("/html/body/div/section/div/a[1]").string("Календарь"))
                .andExpect(xpath("/html/body/div/section/div/a[2]").string("Заявки"));
    }

    @Test
    @Sql(value = {"/create-employee-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-employee-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails(value = "petya@vought.com")
    public void headApplicationsPageTest() throws Exception {
        this.mockMvc.perform(get("/hero/applications"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Hero Subsystem"))
                .andExpect(xpath("/html/body/div/div/H1").string("Заявки на встречу с руководством"))
                .andExpect(xpath("/html/body/div/section/div[1]/a").string("Создать заявку на встречу"));
    }


    // Страница создания заявки на встречу с руководством
    @Test
    @Sql(value = {"/create-employee-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-employee-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails(value = "petya@vought.com")
    public void createHeroApplicationsPageTest() throws Exception {
        this.mockMvc.perform(get("/hero/applications/new"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Hero Subsystem"))
                .andExpect(xpath("/html/body/div/div/div[1]/H1").string("Создание заявки на встречу"))
                .andExpect(xpath("/html/body/div/div/div[2]/form/div[5]/button").string("Сохранить"));
    }

    // Создание заявки на встречу с руководством

    @Test
    @Sql(value = {"/create-employee-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-employee-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails(value = "petya@vought.com")
    public void createHeroApplicationsTest() throws Exception {
        EventDateTimeDto eventDateTimeDto = new EventDateTimeDto(LocalDate.of(2022,2,2), "12:12");
        System.out.println(eventDateTimeDto);
        HeroApplicationCreationRequest heroApplicationCreationRequest = new HeroApplicationCreationRequest();
        heroApplicationCreationRequest.setName("Заявка на встречу от героя");
        heroApplicationCreationRequest.setDescription("Описание заявки заполнено");
        heroApplicationCreationRequest.setEventDateTimeDto(eventDateTimeDto);
        heroApplicationCreationRequest.setMeetingAimEmployeeId(3L);

        applicationService.createHeroRequest("petya@vought.com", heroApplicationCreationRequest);
        Application application = applicationRepository.findById(100L).get();
        ApplicationType applicationType = application.getApplicationType();
        assertEquals(applicationType, ApplicationType.MEETING);

        Long petrId = employeeRepository.findByEmail("petya@vought.com").getId();
        Long applicationCreatorId = application.getCreator().getId();
        assertEquals(petrId, applicationCreatorId);

    }


}
