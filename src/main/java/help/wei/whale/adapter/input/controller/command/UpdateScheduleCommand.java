package help.wei.whale.adapter.input.controller.command;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@ToString
public class UpdateScheduleCommand {

    private String employeeID;

    private String  projectName;

    private String shift;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}
