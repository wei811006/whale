package help.wei.whale.domain.employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class EmployeeManager {

    private transient EmployeeRepository employeeRepository;

    public EmployeeManager(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Set<Employee> findAllEmployed(LocalDate startDate, LocalDate endDate) {
        return this.employeeRepository.findAllByOnboardingDateLessThanEqualAndOffBoardingDateGreaterThanEqual(startDate, endDate);
    }

}
