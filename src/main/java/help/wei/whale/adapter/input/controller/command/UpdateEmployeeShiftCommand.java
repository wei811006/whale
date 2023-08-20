package help.wei.whale.adapter.input.controller.command;

import help.wei.whale.domain.employee.EmployeeShift;
import lombok.Data;

@Data
public class UpdateEmployeeShiftCommand {

    private String projectName;

    private String shift;

    public EmployeeShift toEmployeeShift() {
        return new EmployeeShift(projectName, shift);
    }

}
