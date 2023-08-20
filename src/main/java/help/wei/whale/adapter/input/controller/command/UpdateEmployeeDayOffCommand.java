package help.wei.whale.adapter.input.controller.command;

import help.wei.whale.domain.employee.DayOffType;
import help.wei.whale.domain.employee.EmployeeDayOff;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@ToString
public class UpdateEmployeeDayOffCommand {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;

    private DayOffType type;

    public EmployeeDayOff toEmployeeDayOff() {
        return new EmployeeDayOff(day, type);
    }

}
