package ru.ifmo.egalkin.vought.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.enums.ApplicationSortingType;
import ru.ifmo.egalkin.vought.service.ApplicationService;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/lab")
public class LaboratoryController {

    private static final Set<ApplicationSortingType> NOT_USED_SORING_TYPES = Set.of(
            ApplicationSortingType.CREATOR_ASC,
            ApplicationSortingType.CREATOR_DESC
    );

    @Autowired
    private ApplicationService applicationService;


    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/home")
    public String home() {
        return "lab/home";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/applications")
    public String applications(@PathParam("sortingType") @Nullable ApplicationSortingType sortingType,
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
        return "lab/application-list";
    }
}
