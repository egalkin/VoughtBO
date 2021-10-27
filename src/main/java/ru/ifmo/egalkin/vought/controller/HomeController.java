package ru.ifmo.egalkin.vought.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ifmo.egalkin.vought.model.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;

/**
 * Created by egalkin
 * Date: 12.10.2021
 */
@Controller
public class HomeController {

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value = "/")
    public String index() {
        return "redirect:/home";
    }


    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/home")
    public String loginPageRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {

        String role = authResult.getAuthorities().toString();

        if (role.contains("ROLE_HEAD") || role.contains("ROLE_CEO")) {
            return "redirect:/head/home";
        } else if (role.contains("HERO")) {
            return "redirect:/hero/home";
        } else if (role.contains("PR_MANAGER")) {
            return "redirect:/pr/home";
        } else if (role.contains("SECURITY_MANAGER")) {
            return "redirect:/security/home";
        } else {
            return "redirect:/lab/home";
        }
    }

}
