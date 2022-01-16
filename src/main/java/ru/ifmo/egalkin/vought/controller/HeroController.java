package ru.ifmo.egalkin.vought.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ifmo.egalkin.vought.controller.request.HeroApplicationCreationRequest;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Event;
import ru.ifmo.egalkin.vought.model.Incident;
import ru.ifmo.egalkin.vought.model.enums.ApplicationSortingType;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.model.enums.EventAggregationType;
import ru.ifmo.egalkin.vought.service.ApplicationService;
import ru.ifmo.egalkin.vought.service.EmployeeService;
import ru.ifmo.egalkin.vought.service.EventService;
import ru.ifmo.egalkin.vought.service.IncidentService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by egalkin
 * Date: 13.10.2021
 */
@Controller
@RequestMapping("/hero")
public class HeroController {

    private static final Set<ApplicationSortingType> NOT_USED_SORING_TYPES = Set.of(
            ApplicationSortingType.CREATOR_ASC,
            ApplicationSortingType.CREATOR_DESC
    );


    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private EventService eventService;

    @PreAuthorize("hasRole('HERO')")
    @GetMapping("/home")
    public String home(Model model) {
        Incident incident = incidentService.findFirstByActive(true);
        model.addAttribute("anyIncident", incident != null);
        return "hero/home";
    }

    @PreAuthorize("hasRole('HERO')")
    @GetMapping("/applications")
    public String applications(@PathParam("sortingType") @Nullable ApplicationSortingType sortingType,
                               Model model,
                               Principal principal) {
        ApplicationSortingType selectedSortingType;
        Incident incident = incidentService.findFirstByActive(true);
        List<Application> applications = applicationService.getEmployeeApplications(principal.getName());
        Comparator<Application> applicationComparator;
        selectedSortingType = Objects.requireNonNullElse(sortingType, ApplicationSortingType.DATE_ASC);
        switch (selectedSortingType) {
            case DATE_DESC:
                applicationComparator = Comparator.comparing(Application::getUpdateDate).reversed();
                break;
            case STATUS_ASC:
                applicationComparator = Comparator.comparing(appl -> appl.getApplicationStatus().getDescription());
                break;
            case STATUS_DESC:
                applicationComparator = Comparator.comparing(appl -> appl.getApplicationStatus().getDescription());
                applicationComparator = applicationComparator.reversed();
                break;
            case DATE_ASC:
            case CREATOR_ASC:
            case CREATOR_DESC:
            default:
                applicationComparator = Comparator.comparing(Application::getUpdateDate);
                break;
        }
        applications.sort(applicationComparator);
        List<ApplicationSortingType> sortingTypes = Arrays.stream(ApplicationSortingType.values())
                .filter(st -> st != selectedSortingType && !NOT_USED_SORING_TYPES.contains(st))
                .collect(Collectors.toList());
        model.addAttribute("anyIncident", incident != null);
        model.addAttribute("selectedSortingType", selectedSortingType);
        model.addAttribute("sortingTypes", sortingTypes);
        model.addAttribute("applications", applications);
        return "hero/application-list";
    }

    @PreAuthorize("hasRole('HERO')")
    @GetMapping("/applications/{id}")
    public String applicationDescription(@PathVariable Long id, Model model) {
        Incident incident = incidentService.findFirstByActive(true);
        model.addAttribute("anyIncident", incident != null);
        model.addAttribute("appl", applicationService.getApplicationById(id));
        return "hero/application-description";
    }

    @PreAuthorize("hasRole('HERO')")
    @GetMapping("/applications/new")
    public String applicationView(HeroApplicationCreationRequest heroApplicationCreationRequest,
                                  Model model) {
        List<Employee> headEmployees = employeeService.findAllByDepartment(Department.HEAD);
        Incident incident = incidentService.findFirstByActive(true);
        model.addAttribute("anyIncident", incident != null);
        model.addAttribute("headEmployees", headEmployees);
        return "hero/create-application";
    }

    @PreAuthorize("hasRole('HERO')")
    @PostMapping("/applications/new")
    public String createRequest(@Valid HeroApplicationCreationRequest request,
                                BindingResult result,
                                Principal principal) {
        if (result.hasErrors()) {
            return "hero/create-application";
        }
        applicationService.createHeroRequest(principal.getName(), request);
        return "redirect:/hero/applications";
    }


    @PreAuthorize("hasRole('HERO')")
    @GetMapping("/incidents")
    public String incidentsList(Model model) {
        Incident incident = incidentService.findFirstByActive(true);
        model.addAttribute("anyIncident", incident != null);
        model.addAttribute("incidents", incidentService.findAllByActive(true));
        return "hero/incident-list";
    }

    @PreAuthorize("hasRole('HERO')")
    @PostMapping("/incidents/{id}/react")
    public String reactOnIncident(@PathVariable("id") Long incidentId) {
        incidentService.reactOnIncident(incidentId);
        return "redirect:/hero/home";
    }

    @PreAuthorize("hasRole('HERO')")
    @GetMapping("/calendar")
    public String applications(@RequestParam("aggregationType") @Nullable EventAggregationType aggregationType,
                               Model model,
                               Principal principal) {
        EventAggregationType selectedAggregationType;
        Incident incident = incidentService.findFirstByActive(true);
        selectedAggregationType = Objects.requireNonNullElse(aggregationType, EventAggregationType.DAY);
        List<Event> events = eventService.getEmployeeActualEventsByAggregationType(principal.getName(), selectedAggregationType);
        List<EventAggregationType> aggregationTypes = Arrays.stream(EventAggregationType.values())
                .filter(st -> st != selectedAggregationType)
                .collect(Collectors.toList());
        model.addAttribute("anyIncident", incident != null);
        model.addAttribute("selectedAggregationType", selectedAggregationType);
        model.addAttribute("aggregationTypes", aggregationTypes);
        model.addAttribute("events", events);
        return "hero/calendar";
    }


}
