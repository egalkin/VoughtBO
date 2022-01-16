package ru.ifmo.egalkin.vought.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.egalkin.vought.controller.request.ExperimentCreateRequest;
import ru.ifmo.egalkin.vought.controller.request.IncidentCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.ScientistApplicationRequest;
import ru.ifmo.egalkin.vought.controller.request.SubjectCreationRequest;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Experiment;
import ru.ifmo.egalkin.vought.model.Subject;
import ru.ifmo.egalkin.vought.model.enums.ApplicationSortingType;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;
import ru.ifmo.egalkin.vought.model.enums.IncidentType;
import ru.ifmo.egalkin.vought.model.rrepository.EmployeeRepository;
import ru.ifmo.egalkin.vought.model.rrepository.ExperimentRepository;
import ru.ifmo.egalkin.vought.model.rrepository.SubjectRepository;
import ru.ifmo.egalkin.vought.service.ApplicationService;
import ru.ifmo.egalkin.vought.service.EmployeeService;
import ru.ifmo.egalkin.vought.service.ExperimentService;
import ru.ifmo.egalkin.vought.service.SubjectService;

import javax.validation.Valid;
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

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExperimentService experimentService;

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


    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/applications/{id}")
    public String applicationDescription(@PathVariable Long id, Model model) {
        model.addAttribute("appl", applicationService.getApplicationById(id));
        //    model.addAttribute("applicationTypes", ApplicationType.getApplicationTypeLab());
        return "lab/application-description";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/applications/new")
    public String applicationView(ScientistApplicationRequest scientistApplicationRequest, Model model) {
        model.addAttribute("applicationTypes", ApplicationType.getApplicationTypeLab());
        return "lab/create-application";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @PostMapping("/applications/new")
    public String createExperiment(@Valid ScientistApplicationRequest request,
                                   Model model,
                                   BindingResult result,
                                   Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute("applicationTypes", ApplicationType.getApplicationTypeLab());
            //     model.addAttribute("applicationTypes", ApplicationType.values());
            return "lab/create-application";
        }
        applicationService.createLabExperimentRequest(principal.getName(), request);
        return "redirect:/lab/applications";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("subjects")
    public String subjects(Model model, Principal principal) {
        List<Subject> subjects = subjectRepository.findAll();
        model.addAttribute("subjects", subjects);
        return "lab/subject-list";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/subject/new")
    public String subjectsCreationView(SubjectCreationRequest subjectCreationRequest, Model model) {
        return "lab/create-subject";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @PostMapping("/subject/new")
    public String createSubjects(@Valid SubjectCreationRequest request,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "lab/create-subject";
        }
        subjectService.addSubject(request);
        return "redirect:/lab/subjects";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("experiments")
    public String experiments(Model model, Principal principal) {
        List<Experiment> experiments = experimentRepository.findAll();
        model.addAttribute("experiments", experiments);
        return "lab/experiment-list";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/experiment/new")
    public String experimentCreationView(ExperimentCreateRequest experimentCreateRequest, Model model) {
        return "lab/create-experiment";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @PostMapping("/experiment/new")
    public String createExperiments(@Valid ExperimentCreateRequest request,
                                    BindingResult result,
                                    Model model) {
        if (result.hasErrors()) {
            return "lab/create-experiment";
        }
        experimentService.addExperiment(request);
        return "redirect:/lab/experiments";
    }
}
