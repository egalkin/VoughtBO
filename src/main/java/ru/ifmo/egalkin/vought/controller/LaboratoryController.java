package ru.ifmo.egalkin.vought.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.egalkin.vought.controller.request.ExperimentCreateRequest;
import ru.ifmo.egalkin.vought.controller.request.ExperimentUpdateRequest;
import ru.ifmo.egalkin.vought.controller.request.ScientistApplicationRequest;
import ru.ifmo.egalkin.vought.controller.request.SubjectCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.SubjectUpdateRequest;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.Experiment;
import ru.ifmo.egalkin.vought.model.Subject;
import ru.ifmo.egalkin.vought.model.enums.ApplicationSortingType;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;
import ru.ifmo.egalkin.vought.model.enums.Department;
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
import java.util.stream.Stream;

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
    private SubjectService subjectService;

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
        return "lab/application-description";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/applications/new")
    public String applicationView(ScientistApplicationRequest scientistApplicationRequest, Model model) {
        model.addAttribute("applicationTypes", ApplicationType.getLabApplicationType());
        return "lab/create-application";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @PostMapping("/applications/new")
    public String createApplication(@Valid ScientistApplicationRequest request,
                                    BindingResult result,
                                    Model model,
                                    Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute("applicationTypes", ApplicationType.getLabApplicationType());
            return "lab/create-application";
        }
        applicationService.createScientistRequest(principal.getName(), request);
        return "redirect:/lab/applications";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("subjects")
    public String subjects(Model model, Principal principal) {
        Employee scientist = employeeService.findByEmail(principal.getName());
        List<Subject> subjects = Stream.concat(
                scientist.getMentoredSubjects().stream(),
                scientist.getExperiments()
                        .stream()
                        .flatMap(exp -> exp.getSubjects().stream())
        ).distinct().collect(Collectors.toList());
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
                                 Principal principal) {
        if (result.hasErrors()) {
            return "lab/create-subject";
        }
        subjectService.addSubject(principal.getName(), request);
        return "redirect:/lab/subjects";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/subjects/{id}")
    public String viewSubject(@PathVariable("id") Long subjectId,
                              SubjectUpdateRequest request,
                              Model model,
                              Principal principal) {
        Subject subject = subjectService.findById(subjectId);
        Employee scientist = employeeService.findByEmail(principal.getName());
        request.setNickname(subject.getNickname());
        model.addAttribute("sbj", subject);
        model.addAttribute(
                "currentUserIsMentor",
                subject.getMentor().getId().equals(scientist.getId())
        );
        return "lab/subject";
    }


    @PreAuthorize("hasRole('SCIENTIST')")
    @PostMapping("/subjects/{id}")
    public String editExperiment(@PathVariable("id") Long subjectId,
                                 @Valid SubjectUpdateRequest request,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            Subject subject = subjectService.findById(subjectId);
            model.addAttribute("sbj", subject);
            return "lab/subject";
        }
        subjectService.editSubject(subjectId, request);
        return "redirect:/lab/subjects";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/experiments")
    public String experiments(Model model, Principal principal) {
        List<Experiment> experiments = experimentService.getEmployeeExperiments(principal.getName());
        model.addAttribute("experiments", experiments);
        return "lab/experiment-list";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/experiments/{id}")
    public String viewExperiment(@PathVariable("id") Long experimentId,
                                 ExperimentUpdateRequest request,
                                 Model model,
                                 Principal principal) {
        Experiment experiment = experimentService.findById(experimentId);
        Employee scientist = employeeService.findByEmail(principal.getName());
        request.setGoal(experiment.getGoal());
        request.setDescription(experiment.getDescription());
        model.addAttribute("exp", experiment);
        model.addAttribute(
                "currentUserIsCreator",
                experiment.getCreator().getId().equals(scientist.getId())
        );
        return "lab/experiment";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @PostMapping("/experiments/{id}")
    public String editExperiment(@PathVariable("id") Long experimentId,
                                 @Valid ExperimentUpdateRequest request,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            Experiment experiment = experimentService.findById(experimentId);
            model.addAttribute("exp", experiment);
            return "lab/experiment";
        }
        experimentService.editExperiment(experimentId, request);
        return "redirect:/lab/experiments";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/experiments/{id}/members/add")
    public String addExperimentMembersView(@PathVariable("id") Long experimentId,
                                           Model model,
                                           Principal principal) {
        Experiment experiment = experimentService.findById(experimentId);
        Employee scientist = employeeService.findByEmail(principal.getName());
        List<Employee> possibleMembers = employeeService.findAllPossibleExperimentMembers(
                        Department.LABORATORY,
                        List.of(scientist.getId())
                )
                .stream()
                .filter(emp -> !emp.getExperiments().contains(experiment))
                .collect(Collectors.toList());
        model.addAttribute("exp", experiment);
        model.addAttribute("possibleMembers", possibleMembers);
        return "lab/add-members";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @PostMapping("/experiments/{id}/members/add")
    public String addExperimentMembers(@PathVariable("id") Long experimentId,
                                       @RequestParam @Nullable List<Long> membersIds) {
        if (membersIds == null || membersIds.isEmpty()) {
            return String.format("redirect:/lab/experiments/%d/members/add", experimentId);
        }
        experimentService.addMembersToExperiment(experimentId, membersIds);
        return "redirect:/lab/experiments";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/experiments/{id}/subjects/add")
    public String addExperimentSubjectsView(@PathVariable("id") Long experimentId,
                                            Model model) {
        Experiment experiment = experimentService.findById(experimentId);
        List<Subject> possibleSubjects = subjectService.findAllPossibleExperimentSubjects(experimentId);
        model.addAttribute("exp", experiment);
        model.addAttribute("possibleSubjects", possibleSubjects);
        return "lab/add-subjects";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @PostMapping("/experiments/{id}/subjects/add")
    public String addExperimentSubjects(@PathVariable("id") Long experimentId,
                                        @RequestParam @Nullable List<Long> subjectsIds) {
        if (subjectsIds == null || subjectsIds.isEmpty()) {
            return String.format("redirect:/lab/experiments/%d/subjects/add", experimentId);
        }
        experimentService.addSubjectsToExperiment(experimentId, subjectsIds);
        return "redirect:/lab/experiments";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/experiment/new")
    public String addExperimentView(ExperimentCreateRequest experimentCreateRequest, Model model) {
        return "lab/create-experiment";
    }

    @PreAuthorize("hasRole('SCIENTIST')")
    @PostMapping("/experiment/new")
    public String addExperiment(@Valid ExperimentCreateRequest request,
                                BindingResult result,
                                Principal principal) {
        if (result.hasErrors()) {
            return "lab/create-experiment";
        }
        experimentService.addExperiment(principal.getName(), request);
        return "redirect:/lab/experiments";
    }
}
