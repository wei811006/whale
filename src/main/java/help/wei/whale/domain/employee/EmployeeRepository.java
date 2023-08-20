package help.wei.whale.domain.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Set<Employee> findAllByOnboardingDateLessThanEqualAndOffBoardingDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);

}
