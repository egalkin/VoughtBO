package ru.ifmo.egalkin.vought.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ifmo.egalkin.vought.controller.request.IncidentCreationRequest;
import ru.ifmo.egalkin.vought.model.Incident;
import ru.ifmo.egalkin.vought.model.enums.IncidentType;
import ru.ifmo.egalkin.vought.sensors.SensorHandler;
import ru.ifmo.egalkin.vought.sensors.model.SensorData;
import ru.ifmo.egalkin.vought.service.IncidentService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created by egalkin
 * Date: 13.10.2021
 */
@Controller
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private IncidentService incidentService;
    @Autowired
    private SensorHandler sensorHandler;

    @PreAuthorize("hasRole('SECURITY_MANAGER')")
    @GetMapping("/home")
    public String securityHome() {
        return "security/home";
    }

    @PreAuthorize("hasRole('SECURITY_MANAGER')")
    @GetMapping("/incident/new")
    public String accidentCreationView(IncidentCreationRequest incidentCreationRequest, Model model) {
        model.addAttribute("incidentTypes", IncidentType.values());
        return "security/create-incident";
    }

    @PreAuthorize("hasRole('SECURITY_MANAGER')")
    @PostMapping("/incident/new")
    public String createIncident(@Valid IncidentCreationRequest request,
                                 BindingResult result,
                                 Model model,
                                 Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute("incidentTypes", IncidentType.values());
            return "security/create-incident";
        }
        incidentService.createIncident(principal.getName(), request);
        return "redirect:/security/home";
    }

    @PreAuthorize("hasRole('SECURITY_MANAGER')")
    @GetMapping("/sensors")
    public String sensors(Model model) {
        SensorData sensorData = sensorHandler.getSensorData();
        model.addAttribute("sensor", sensorData);
        return "security/sensors";
    }

}
