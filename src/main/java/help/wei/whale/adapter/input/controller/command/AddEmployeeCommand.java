package help.wei.whale.adapter.input.controller.command;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@ToString
public class AddEmployeeCommand {

    private String employeeID;

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate onboardingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate offboardingDate;

}
