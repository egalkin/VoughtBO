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
import ru.ifmo.egalkin.vought.controller.request.EventCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.PrApplicationCreationRequest;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.enums.ApplicationSortingType;
import ru.ifmo.egalkin.vought.service.EmployeeService;
import ru.ifmo.egalkin.vought.service.EventService;
import ru.ifmo.egalkin.vought.service.ApplicationService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pr")
public class PrController {

    private static final Set<ApplicationSortingType> NOT_USED_SORING_TYPES = Set.of(
            ApplicationSortingType.CREATOR_ASC,
            ApplicationSortingType.CREATOR_DESC
    );

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EventService eventService;
    @Autowired

    private ApplicationService applicationService;


    @PreAuthorize("hasRole('PR_MANAGER')")
    @GetMapping("/home")
    public String prHome() {
        return "pr/home";
    }

    @PreAuthorize("hasRole('PR_MANAGER')")
    @GetMapping("wards")
    public String wards(Model model, Principal principal) {
        Employee prManager = employeeService.findByEmail(principal.getName());
        model.addAttribute("wards", prManager.getWards());
        return "pr/wards-list";
    }

    @PreAuthorize("hasRole('PR_MANAGER')")
    @GetMapping("/wards/add")
    public String wardView(Model model) {
        List<Employee> unwardedHeroes = employeeService.findUnwardedHeroes();
        model.addAttribute("unwardedHeroes", unwardedHeroes);
        return "pr/add-wards";
    }

    @PreAuthorize("hasRole('PR_MANAGER')")
    @PostMapping("/wards/add")
    public String ward(@RequestParam List<Long> wardsIds, Principal principal) {
        employeeService.wardHeroes(principal.getName(), wardsIds);
        return "redirect:";
    }

    @PreAuthorize("hasRole('PR_MANAGER')")
    @GetMapping("/events/new")
    public String eventView(@RequestParam @Nullable List<Long> wardsIds, Model model) {
        if (wardsIds == null || wardsIds.isEmpty()) {
            return "redirect:/pr/wards";
        }
        EventCreationRequest request = new EventCreationRequest();
        request.setHeroesIds(wardsIds);
        model.addAttribute("eventCreationRequest", request);
        return "pr/create-event";
    }

    @PreAuthorize("hasRole('PR_MANAGER')")
    @PostMapping("/events/new")
    public String addEvent(@Valid EventCreationRequest request,
                           BindingResult result,
                           Principal principal) {
        if (result.hasErrors()) {
            return "pr/create-event";
        }
        eventService.createEvent(principal.getName(), request);
        return "redirect:/pr/wards";
    }

    @PreAuthorize("hasRole('PR_MANAGER')")
    @GetMapping("/applications")
    public String applications(@RequestParam("sortingType") @Nullable ApplicationSortingType sortingType,
                               Model model,
                               Principal principal) {
        ApplicationSortingType selectedSortingType;
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
        model.addAttribute("selectedSortingType", selectedSortingType);
        model.addAttribute("sortingTypes", sortingTypes);
        model.addAttribute("applications", applications);
        return "pr/application-list";
    }

    @PreAuthorize("hasRole('PR_MANAGER')")
    @GetMapping("/applications/{id}")
    public String applicationDescription(@PathVariable Long id, Model model) {
        model.addAttribute("appl", applicationService.getApplicationById(id));
        return "pr/application-description";
    }

    @PreAuthorize("hasRole('PR_MANAGER')")
    @GetMapping("/applications/new")
    public String applicationView(PrApplicationCreationRequest prApplicationCreationRequest) {
        return "pr/create-application";
    }

    @PreAuthorize("hasRole('PR_MANAGER')")
    @PostMapping("/applications/new")
    public String createRequest(@Valid PrApplicationCreationRequest request,
                                BindingResult result,
                                Principal principal) {
        if (result.hasErrors()) {
            return "pr/create-application";
        }
        applicationService.createPrRequest(principal.getName(), request);
        return "redirect:/pr/applications";
    }


}
