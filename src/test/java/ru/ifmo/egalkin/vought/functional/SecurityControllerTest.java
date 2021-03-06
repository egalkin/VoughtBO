package ru.ifmo.egalkin.vought.functional;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.ifmo.egalkin.vought.controller.request.IncidentCreationRequest;
import ru.ifmo.egalkin.vought.model.enums.IncidentType;
import ru.ifmo.egalkin.vought.model.rrepository.IncidentRepository;
import ru.ifmo.egalkin.vought.service.IncidentService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/functional/create-security-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/functional/create-security-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails(value = "vas@vought.com")
public class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentService incidentService;

    @Test
    public void securityHomePageTest() throws Exception {
        this.mockMvc.perform(get("/security/home"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Security Subsystem"))
                .andExpect(status().isOk());
    }

    @Test
    public void securitySensorsPageTest() throws Exception {
        this.mockMvc.perform(get("/security/sensors"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/H1").string("???????????? ????????????????"))

                .andExpect(xpath("/html/body/div/section/table/tbody/tr/td[2]/div/label/strong")
                        .string("?????????? ???????????????? ????????????????????????"))
                .andExpect(xpath("/html/body/div/section/table/tbody/tr/td[2]/div/p").string("1"))

                .andExpect(xpath("/html/body/div/section/table/tbody/tr/td[3]/div/label/strong")
                        .string("?????????????? ??????????????????????????"))
                .andExpect(xpath("/html/body/div/section/table/tbody/tr/td[3]/div/p").string("????????????"))

                .andExpect(xpath("/html/body/div/section/table/tbody/tr/td[4]/div/label/strong")
                        .string("?????????????? ????????????????????????"))
                .andExpect(xpath("/html/body/div/section/table/tbody/tr/td[4]/div/p").string("????????????"))

                .andExpect(status().isOk());
    }

    @Test
    public void createIncidentTest() throws Exception {
        this.mockMvc.perform(get("/security/incident/new"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        IncidentCreationRequest incidentCreationRequest = new IncidentCreationRequest();
        incidentCreationRequest.setAddress("?????????? ????????????????????????");
        incidentCreationRequest.setIncidentType(IncidentType.ROBBERY);
        incidentCreationRequest.setArmamentLevel(5);
        incidentCreationRequest.setEnemiesNumber(5);
        incidentCreationRequest.setInfo("???????????????????? ?? ????????????????????????");
        incidentService.createIncident("vas@vought.com", incidentCreationRequest);
        assertTrue(incidentRepository.existsById(100L));
    }

}
