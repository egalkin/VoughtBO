package ru.ifmo.egalkin.vought.model.rrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.model.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Created by egalkin
 * Date: 12.10.2021
 */

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmail(String email);


    List<Employee> findByDepartmentNotIn(List<Department> departmentList);
    List<Employee> findByDepartmentIn(List<Department> departmentList);
    List<Employee> findAllByIdNotIn(List<Long> ids);

    @Query("SELECT emp from Employee emp where emp.prManager IS NULL AND emp.department = 'HERO'")
    List<Employee> findUnwardedHeroes();

    Integer countAllByDepartment(Department department);

    List<Employee> findAllByDepartment(Department department);

    List<Employee> findAllByDepartmentAndIdNotIn(Department department, List<Long> ids);

    Long countEmployeeByEmailAndPassword(String email, String password);
 //   Optional<Employee> findByEmailOptional(String email);

}
