package help.wei.whale.domain.employee;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode(of = {"projectName", "shift"})
public class EmployeeShift {

    private String projectName;

    private String shift;

    // for JPA
    private EmployeeShift() {}

    public EmployeeShift(String projectName, String shift) {
        this.projectName = projectName;
        this.shift = shift;
    }
}
