package help.wei.whale.domain.employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface EmployeeService {

    void newEmployee(String employeeId, String name, LocalDate onboardingDate, LocalDate offboardingDate);

    void updateEmployee(String employeeId, String name, LocalDate onboardingDate, LocalDate offboardingDate);

    void updateEmployeeDayOff(String employeeId, Set<EmployeeDayOff> dayOffs);

    void updateEmployeeShift(String employeeId, Set<EmployeeShift> shifts);

    void deleteEmployee(String employeeId);

    Employee getEmployee(String employeeId);

    List<Employee> getAllEmployees();
}
