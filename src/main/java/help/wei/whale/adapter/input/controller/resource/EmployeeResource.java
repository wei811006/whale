package help.wei.whale.adapter.input.controller.resource;

import help.wei.whale.domain.employee.Employee;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeResource {

    private String employeeID;

    private String name;

    private String onboardingDate;

    private String offboardingDate;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    public EmployeeResource(Employee employee) {
        this.employeeID = employee.getEmployeeId();
        this.name = employee.getName();
        this.onboardingDate = employee.getOnboardingDate().toString();
        this.offboardingDate = employee.getOffBoardingDate().toString();
        this.createdDate = employee.getCreatedDate();
        this.lastModifiedDate = employee.getLastModifiedDate();
    }
    
}
