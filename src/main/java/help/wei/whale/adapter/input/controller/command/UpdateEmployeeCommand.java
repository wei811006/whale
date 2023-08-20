package help.wei.whale.adapter.input.controller.command;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UpdateEmployeeCommand {

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate onboardingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate offboardingDate;

}
