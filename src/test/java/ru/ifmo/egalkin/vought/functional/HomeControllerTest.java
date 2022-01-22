package ru.ifmo.egalkin.vought.functional;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/create-home-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-home-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource("/application-test.properties")
public class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/home"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "edgar@vought.com")
    public void headHomePageTest() throws Exception {
        this.mockMvc.perform(get("/head/home"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Head Subsystem"))
                .andExpect(xpath("/html/body/div/section/div/a[1]").string("Персонал"))
                .andExpect(xpath("/html/body/div/section/div/a[2]").string("Просмотр состояния"))
                .andExpect(xpath("/html/body/div/section/div/a[3]").string("Календарь"))
                .andExpect(xpath("/html/body/div/section/div/a[4]").string("Заявки"))
                .andExpect(status().isOk());;
    }

    @Test
    @WithUserDetails(value = "petya@vought.com")
    public void heroHomePageTest() throws Exception {
        this.mockMvc.perform(get("/hero/home"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Hero Subsystem"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "ant@vought.com")
    public void labHomePageTest() throws Exception {
        this.mockMvc.perform(get("/lab/home"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Laboratory Subsystem"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "vas@vought.com")
    public void prHomePageTest() throws Exception {
        this.mockMvc.perform(get("/pr/home"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought PR Subsystem"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "pasha@vought.com")
    public void securityHomePageTest() throws Exception {
        this.mockMvc.perform(get("/security/home"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/head/nav/div/a").string("Vought Security Subsystem"))
                .andExpect(status().isOk());
    }

}
