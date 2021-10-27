package ru.ifmo.egalkin.vought.model.rrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.model.Employee;

import java.util.List;

/**
 * Created by egalkin
 * Date: 12.10.2021
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmail(String email);

    List<Employee> findByDepartmentNotIn(List<Department> departmentList);
    List<Employee> findByDepartmentIn(List<Department> departmentList);
    List<Employee> findAllByIdNotIn(List<Long> ids);

    @Query("SELECT emp from Employee emp where emp.prManager IS NULL AND emp.department = 'HERO'")
    List<Employee> findUnwardedHeroes();

    List<Employee> findAllByDepartment(Department department);



}
