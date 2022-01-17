package ru.ifmo.egalkin.vought.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.egalkin.vought.controller.request.EmployeeCreationRequest;
import ru.ifmo.egalkin.vought.controller.request.EmployeeUpdateRequest;
import ru.ifmo.egalkin.vought.controller.request.HeroUpdateRequest;
import ru.ifmo.egalkin.vought.controller.request.ApplicationRejectionRequest;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.Event;
import ru.ifmo.egalkin.vought.model.enums.ApplicationSortingType;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.model.Employee;
import ru.ifmo.egalkin.vought.model.enums.EmployeeSortingType;
import ru.ifmo.egalkin.vought.model.enums.EventAggregationType;
import ru.ifmo.egalkin.vought.service.EmployeeService;
import ru.ifmo.egalkin.vought.service.ApplicationService;
import ru.ifmo.egalkin.vought.service.EventService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by egalkin
 * Date: 12.10.2021
 */
@Controller
@RequestMapping("/head")
public class HeadController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private EventService eventService;


    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @GetMapping("/home")
    public String headHome() {
        return "head/home";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @GetMapping("/employees")
    public String employeeList(@PathParam("sortingType") @Nullable EmployeeSortingType sortingType,
                               Model model,
                               Principal principal) {
        Employee headEmployee = employeeService.findByEmail(principal.getName());
        EmployeeSortingType selectedSortingType;
        List<Employee> employees = headEmployee.isCeo() ?
                employeeService.findAllByIdNotIn(List.of(headEmployee.getId())) :
                employeeService.findByDepartmentNotIn(List.of(Department.HEAD));
        Comparator<Employee> employeeComparator;
        selectedSortingType = Objects.requireNonNullElse(sortingType, EmployeeSortingType.DEPARTMENT_ASC);
        switch (selectedSortingType) {
            case DEPARTMENT_DESC:
                employeeComparator = Comparator.comparing(emp -> emp.getDepartment().getDescription());
                employeeComparator = employeeComparator.reversed();
                break;
            case NAME_ASC:
                employeeComparator = Comparator.comparing(Employee::getFullName);
                break;
            case NAME_DESC:
                employeeComparator = Comparator.comparing(Employee::getFullName).reversed();
                break;
            case DEPARTMENT_ASC:
            default:
                employeeComparator = Comparator.comparing(emp -> emp.getDepartment().getDescription());
                break;
        }
        employees.sort(employeeComparator);
        List<EmployeeSortingType> sortingTypes = Arrays.stream(EmployeeSortingType.values())
                .filter(st -> st != selectedSortingType)
                .collect(Collectors.toList());
        sortingTypes.remove(selectedSortingType);
        model.addAttribute("selectedSortingType", selectedSortingType);
        model.addAttribute("sortingTypes", sortingTypes);
        model.addAttribute("employees", employees);
        return "head/employees-list";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @GetMapping("/employees/{id}")
    public String viewEmployee(@PathVariable("id") Long employeeId,
                               EmployeeUpdateRequest employeeUpdateRequest,
                               Model model) {
        Employee employee = employeeService.findById(employeeId);
        employeeUpdateRequest.setFirstName(employee.getFirstName());
        employeeUpdateRequest.setLastName(employee.getLastName());
        model.addAttribute("employee", employee);
        return "head/employee-edit";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @PostMapping("/employees/{id}")
    public String editEmployee(@PathVariable("id") Long employeeId,
                               @Valid EmployeeUpdateRequest request,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            Employee employee = employeeService.findById(employeeId);
            model.addAttribute("employee", employee);
            return "head/employee-edit";
        }
        employeeService.editEmployee(employeeId, request);
        return "redirect:/head/employees";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @GetMapping("/employees/hero/{id}")
    public String viewHero(@PathVariable("id") Long employeeId,
                           HeroUpdateRequest heroUpdateRequest,
                           Model model) {
        Employee hero = employeeService.findById(employeeId);
        heroUpdateRequest.setFirstName(hero.getFirstName());
        heroUpdateRequest.setLastName(hero.getLastName());
        heroUpdateRequest.setNickname(hero.getNickname());
        heroUpdateRequest.setPowerDescription(hero.getPowerDescription());
        model.addAttribute("hero", hero);
        return "head/hero-edit";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @PostMapping("/employees/hero/{id}")
    public String editHero(@PathVariable("id") Long heroId,
                           @Valid HeroUpdateRequest request,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            Employee hero = employeeService.findById(heroId);
            model.addAttribute("hero", hero);
            return "head/hero-edit";
        }
        employeeService.editHero(heroId, request);
        return "redirect:/head/employees";
    }


    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @PostMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long employeeId) {
        employeeService.deleteById(employeeId);
        return "redirect:/head/employees";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @GetMapping("/employees/new")
    public String addEmployeeView(EmployeeCreationRequest employeeCreationRequest, Model model) {
        model.addAttribute("departments", Arrays.asList(Department.values()));
        return "head/create-employee";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @PostMapping("/employees/new")
    public String addEmployee(@Valid EmployeeCreationRequest request,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", Arrays.asList(Department.values()));
            return "head/create-employee";
        }
        employeeService.addEmployee(request);
        return "redirect:/head/employees";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @GetMapping("/applications")
    public String applicationList(@PathParam("sortingType") @Nullable ApplicationSortingType sortingType,
                                  Model model,
                                  Principal principal) {
        ApplicationSortingType selectedSortingType;
        List<Application> applications = applicationService.getHeadEmployeeApplications(principal.getName());
        Comparator<Application> applicationComparator;
        selectedSortingType = Objects.requireNonNullElse(sortingType, ApplicationSortingType.DATE_ASC);
        switch (selectedSortingType) {
            case DATE_DESC:
                applicationComparator = Comparator.comparing(Application::getUpdateDate).reversed();
                break;
            case CREATOR_ASC:
                applicationComparator = Comparator.comparing(appl -> appl.getCreator().getFullName());
                break;
            case CREATOR_DESC:
                applicationComparator = Comparator.comparing(appl -> appl.getCreator().getFullName());
                applicationComparator = applicationComparator.reversed();
                break;
            case STATUS_ASC:
                applicationComparator = Comparator.comparing(appl -> appl.getApplicationStatus().getDescription());
                break;
            case STATUS_DESC:
                applicationComparator = Comparator.comparing(appl -> appl.getApplicationStatus().getDescription());
                applicationComparator = applicationComparator.reversed();
                break;
            case DATE_ASC:
            default:
                applicationComparator = Comparator.comparing(Application::getUpdateDate);
                break;
        }
        applications.sort(applicationComparator);
        List<ApplicationSortingType> sortingTypes = Arrays.stream(ApplicationSortingType.values())
                .filter(st -> st != selectedSortingType)
                .collect(Collectors.toList());
        model.addAttribute("selectedSortingType", selectedSortingType);
        model.addAttribute("sortingTypes", sortingTypes);
        model.addAttribute("applications", applications);
        return "head/application-list";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @GetMapping("/applications/{id}")
    public String applicationDescription(@PathVariable Long id, Model model) {
        model.addAttribute("appl", applicationService.getApplicationById(id));
        return "head/application-description";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @PostMapping("/applications/{id}/approve")
    public String approveApplication(@PathVariable Long id, Principal principal) {
        applicationService.approveApplication(principal.getName(), id);
        return "redirect:/head/applications";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @GetMapping("/applications/{id}/reject")
    public String rejectApplicationView(@PathVariable Long id,
                                        ApplicationRejectionRequest applicationRejectionRequest,
                                        Model model) {
        model.addAttribute("appl", applicationService.getApplicationById(id));
        return "head/application-reject";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @PostMapping("/applications/{id}/reject")
    public String rejectApplication(@PathVariable Long id,
                                    ApplicationRejectionRequest applicationRejectionRequest,
                                    Principal principal) {
        applicationService.rejectApplication(principal.getName(), id, applicationRejectionRequest);
        return "redirect:/head/applications";
    }

    @PreAuthorize("hasAnyRole('CEO', 'HEAD')")
    @GetMapping("/calendar")
    public String applications(@RequestParam("aggregationType") @Nullable EventAggregationType aggregationType,
                               Model model,
                               Principal principal) {
        EventAggregationType selectedAggregationType;
        selectedAggregationType = Objects.requireNonNullElse(aggregationType, EventAggregationType.DAY);
        List<Event> events = eventService.getEmployeeActualEventsByAggregationType(principal.getName(), selectedAggregationType);
        List<EventAggregationType> aggregationTypes = Arrays.stream(EventAggregationType.values())
                .filter(st -> st != selectedAggregationType)
                .collect(Collectors.toList());
        model.addAttribute("selectedAggregationType", selectedAggregationType);
        model.addAttribute("aggregationTypes", aggregationTypes);
        model.addAttribute("events", events);
        return "head/calendar";
    }

}
