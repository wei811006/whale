package help.wei.whale.domain.employee;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE) // for JPA
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Employee {

    @LastModifiedDate
    @Getter
    private LocalDateTime lastModifiedDate;

    @CreatedDate
    @Getter
    private LocalDateTime createdDate;

    @Id
    @Getter
    private String employeeId;

    @Getter
    private String name;

    @Getter
    private String level;

    @Getter
    private LocalDate onboardingDate;

    @Getter
    private LocalDate offBoardingDate;

    @ElementCollection
    private Set<EmployeeDayOff> dayOffTypes = new HashSet<>();

    @ElementCollection
    private Set<EmployeeShift> employeeShifts = new HashSet<>();

    public void dayOff(Set<EmployeeDayOff> dayOffs) {
        dayOffTypes.addAll(dayOffs);
    }

    public void updateShift(Set<EmployeeShift> shifts) {
        employeeShifts = shifts;
    }

    /**
     * Check if the employee is on day off on the given date
     * @param date
     * @return
     */
    public Optional<String> isDayOff(LocalDate date) {
        return dayOffTypes.stream()
                .filter(employeeDayOff -> employeeDayOff.getDay().equals(date))
                .map(EmployeeDayOff::getType)
                .map(DayOffType::getChineseName)
                .findFirst();
    }

    public boolean haveShift(String project, String shift) {
        EmployeeShift projectShift = new EmployeeShift(project, shift);
        return this.employeeShifts.stream()
                .anyMatch(employeeShift -> employeeShift.equals(projectShift));
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setOnboardingDate(@NonNull LocalDate onboardingDate) {
        this.onboardingDate = onboardingDate;
    }

    public void setOffBoardingDate(@NonNull LocalDate offBoardingDate) {
        this.offBoardingDate = offBoardingDate;
    }
}
