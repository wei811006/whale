package help.wei.whale.adapter.input.controller.command;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AddWorkDayCommand {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private boolean workingDay;

}
