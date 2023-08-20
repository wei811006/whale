package help.wei.whale.adapter.input.controller.command;

import help.wei.whale.domain.project.ProjectShift;
import lombok.Data;

import java.time.DayOfWeek;

@Data
public class AddProjectShiftCommand {

    private String shift;

    private DayOfWeek day;

    private Long priority;

    public ProjectShift toProjectShift() {
        return new ProjectShift(shift, day, priority);
    }

}
